package com.dayz.shop.service;

import com.dayz.shop.exception.BalanceTooLowException;
import com.dayz.shop.jpa.entities.UserService;
import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.OrderItemRepository;
import com.dayz.shop.repository.OrderRepository;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.repository.UserServiceRepository;
import com.dayz.shop.utils.OrderUtils;
import com.dayz.shop.utils.Utils;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final SendToServerService sendToServerService;
	private final UserServiceRepository userServiceRepository;
	private final UserRepository userRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
	                    SendToServerService sendToServerService, UserServiceRepository userServiceRepository,
	                    UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.sendToServerService = sendToServerService;
		this.userServiceRepository = userServiceRepository;
		this.userRepository = userRepository;
	}

	public Order addOrderItem(Item item, Store store) {
		User user = Utils.getCurrentUser();
		Order order = OrderUtils.getCurrentOrder(user, store);
		OrderItem orderItem = OrderUtils.createOrderItem(item, user, order);
		orderItemRepository.save(orderItem);
		OrderUtils.recalculateOrder(order);
		return orderRepository.save(order);
	}

	public Order deleteOrderItem(Item item, Store store) {
		Order order = OrderUtils.getCurrentOrder(store);
		OrderItem orderItem = orderItemRepository.findFirstByItemAndOrder(item, order);
		order.getOrderItems().remove(orderItem);
		OrderUtils.recalculateOrder(order);
		orderItemRepository.delete(orderItem);
		return orderRepository.save(order);
	}

	public Order buyItemNow(Item item, Store store, Server server) throws BalanceTooLowException {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Order order = OrderUtils.createOrder(user, store);
		order.setServer(server);
		orderRepository.save(order);
		OrderItem orderItem = OrderUtils.createOrderItem(item, user, order);
		List<OrderItem> orderItems = new ArrayList<>(order.getOrderItems());
		orderItems.add(orderItemRepository.save(orderItem));
		order.setOrderItems(orderItems);
		return placeOrder(orderRepository.save(order));
	}

	public Order placeOrder(Store store) throws BalanceTooLowException {
		return placeOrder(OrderUtils.getCurrentOrder(Utils.getCurrentUser(), store));
	}

	public Order placeOrder(Order order) throws BalanceTooLowException {
		User user = userRepository.getById(order.getUser().getId());
		if (user.getBalance().compareTo(order.getOrderTotal()) < 0) {
			throw new BalanceTooLowException(user.getBalance(), order.getOrderTotal());
		}
		try {
			Map<ItemType, Order> separatedTypes = splitTypes(order);
			saveServices(separatedTypes);
			sendToServerService.sendOrder(order, separatedTypes);
			user.setBalance(user.getBalance().subtract(order.getOrderTotal()));
			order.setStatus(OrderStatus.COMPLETE);
			order.getOrderItems().forEach(orderItem -> orderItem.setStatus(OrderStatus.COMPLETE));
		} catch (JSchException | InterruptedException | SftpException | IOException e) {
			order.setStatus(OrderStatus.FAILED);
			order.getOrderItems().forEach(orderItem -> orderItem.setStatus(OrderStatus.COMPLETE));
			e.printStackTrace();
		}
		return orderRepository.save(order);
	}

	private void saveServices(Map<ItemType, Order> separatedTypes) {

		repeatSet:
		for (Map.Entry<ItemType, Order> itemTypeOrderEntry : separatedTypes.entrySet()) {
			switch (itemTypeOrderEntry.getKey()) {
				case SET:
				case VIP:
					OrderItem orderItem = itemTypeOrderEntry.getValue().getOrderItems().get(0);
					User user = orderItem.getUser();
					Item item = orderItem.getItem();
					ItemType itemType = item.getItemType();
					Server server = orderItem.getServer();
					LocalDateTime endDate;
					boolean repeat = false;
					UserService userService;
					do {
						userService = userServiceRepository.findByUserAndItemTypeAndServer(user, itemType, server);
						if (userService != null) {
							endDate = userService.getEndDate();
							if (itemTypeOrderEntry.getKey().equals(ItemType.SET)) {
								chargebackSet(userService);
								repeat = true;
							}
						} else {
							userService = new UserService();
							userService.setOrder(orderItem.getOrder());
							userService.setUser(orderItem.getUser());
							userService.setItemType(itemType);
							userService.setItemTypeStr(itemType.toString());
							userService.setServer(server);
							userService.setUser(user);
							userService.setUserId(user.getId());
							endDate = LocalDateTime.now().plusDays(30);
						}
					} while (repeat);
					if (itemTypeOrderEntry.getKey().equals(ItemType.VIP)) {
						endDate = endDate.plusDays(Integer.parseInt(item.getColor()));
					} else if (itemTypeOrderEntry.getKey().equals(ItemType.SET)) {
						endDate = LocalDateTime.now().plusDays(30);
					}
					userService.setEndDate(endDate);
					userServiceRepository.save(userService);
			}
		}
	}

	private void chargebackSet(UserService userService) {
		LocalDateTime end = userService.getEndDate();
		long days = ChronoUnit.DAYS.between(end, LocalDateTime.now());
		BigDecimal chargebackAmount = userService.getOrder().getOrderTotal().divide(BigDecimal.valueOf(30), 0, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(days));
		User user = userService.getUser();
		user.setBalance(user.getBalance().add(chargebackAmount));
		userRepository.save(user);
		userServiceRepository.delete(userService);
	}

	private Map<ItemType, Order> splitTypes(Order order) {
		Map<ItemType, Order> separatedItems = new HashMap<>();
		for (OrderItem orderItem : order.getOrderItems()) {
			ItemType itemType = orderItem.getItem().getItemType();
			Order splitOrder = separatedItems.getOrDefault(itemType, new Order());
			splitOrder.getOrderItems().add(orderItem);
			separatedItems.put(itemType, splitOrder);
		}
		return separatedItems;
	}
}
