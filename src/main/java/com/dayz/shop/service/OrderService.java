package com.dayz.shop.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OrderService {
	public static final String CLASSNAME = OrderService.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

	private static final List<ItemType> RECHARGEABLE = Arrays.asList(ItemType.SET, ItemType.CUSTOM_SET);

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

	public Order addOrderItem(Item item, Store store, int count) {
		User user = Utils.getCurrentUser();
		Order order = OrderUtils.getCurrentOrder(user, store);
		OrderItem orderItem = OrderUtils.createOrderItem(item, user, order, count);
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

	public Order buyItemNow(Item item, Store store, Server server, int count) throws InterruptedException {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Order order = OrderUtils.createOrder(user, store);
		order.setServer(server);
		orderRepository.save(order);
		OrderItem orderItem = OrderUtils.createOrderItem(item, user, order, count);
		List<OrderItem> orderItems = new ArrayList<>(order.getOrderItems());
		orderItems.add(orderItemRepository.save(orderItem));
		order.setOrderItems(orderItems);
		return placeOrder(orderRepository.save(order));
	}

	public Order placeOrder(Store store) throws InterruptedException {
		return placeOrder(OrderUtils.getCurrentOrder(Utils.getCurrentUser(), store));
	}

	public Order placeOrder(Order order) throws InterruptedException {
		User user = userRepository.getById(order.getUser().getId());
		OrderUtils.recalculateOrder(order);
		BigDecimal orderTotal = order.getOrderTotal();
		if (user.getBalance().compareTo(orderTotal) < 0) {
			LOGGER.log(Level.WARNING, "Insufficient funds to place order");
			order.setStatus(OrderStatus.FAILED);
			order.getOrderItems().forEach(orderItem -> orderItem.setStatus(OrderStatus.FAILED));
			order.getProperties().put("message", Utils.getMessage("order.failed.insufficient", order.getStore(), user.getBalance(), orderTotal));
		} else {
			Utils.updateUserBalance(user, orderTotal.negate());
			order.setTimePlaced(LocalDateTime.now());
			try {
				Map<ItemType, List<OrderItem>> separatedTypes = splitTypes(order);
				sendToServerService.sendOrder(order, separatedTypes);
				user.setBalance(user.getBalance().subtract(orderTotal));
				order.setStatus(OrderStatus.COMPLETE);
				order.getOrderItems().forEach(orderItem -> orderItem.setStatus(OrderStatus.COMPLETE));

				order.getProperties().put("message", Utils.getMessage(getKey(order), order.getStore()));
				order = orderRepository.save(order);
				saveServices(separatedTypes);
			} catch (JSchException | SftpException | IOException e) {
				LOGGER.log(Level.SEVERE, "Error during order placing", e);
				order.getProperties().put("message", Utils.getMessage("order.failed", order.getStore()));
				order.setStatus(OrderStatus.FAILED);
				order.getOrderItems().forEach(orderItem -> orderItem.setStatus(OrderStatus.FAILED));
				order = orderRepository.save(order);
				Utils.updateUserBalance(user, orderTotal);
			}
		}
		return order;
	}

	private static String getKey(Order order) {
		OrderItem orderItem = order.getOrderItems().stream().findFirst().get();
		return orderItem.getItem().getItemType() == ItemType.ITEM
				|| orderItem.getItem().getItemType() == ItemType.VEHICLE
				? "order.success" : "service.success";
	}

	private void saveServices(Map<ItemType, List<OrderItem>> separatedTypes) {
		for (Map.Entry<ItemType, List<OrderItem>> itemTypeOrderEntry : separatedTypes.entrySet()) {
			List<OrderItem> orderItems = itemTypeOrderEntry.getValue();
			OrderItem orderItem = orderItems.stream().findFirst().get();
			User user = orderItem.getUser();
			Item item = orderItem.getItem();
			switch (itemTypeOrderEntry.getKey()) {
				case SET:
				case VIP:
					ItemType itemType = item.getItemType();
					Server server = orderItem.getServer();
					LocalDateTime endDate;
					boolean repeat = false;
					UserService userService;
					do {
						userService = userServiceRepository.findByUserAndItemTypeAndServer(user, itemType, server);
						if (userService != null) {
							endDate = userService.getEndDate();
							if (RECHARGEABLE.contains(itemTypeOrderEntry.getKey())) {
								chargebackSet(userService);
								repeat = true;
							}
						} else {
							userService = new UserService();
							userService.setOrder(orderItem.getOrder());
							userService.setUser(orderItem.getUser());
							userService.setItemType(itemType);
							userService.setServer(server);
							userService.setUser(user);
							endDate = LocalDateTime.now();
							repeat = false;
						}
						if (!repeat) {
							userService.setOrder(orderItem.getOrder());
						}
					} while (repeat);
					if (itemTypeOrderEntry.getKey().equals(ItemType.VIP)) {
						endDate = endDate.plusDays((long) Integer.parseInt(item.getColor()) * orderItem.getCount());
					} else if (itemTypeOrderEntry.getKey().equals(ItemType.SET)) {
						endDate = LocalDateTime.now().plusDays((long) Integer.parseInt(item.getColor()) * orderItem.getCount());
					}
					userService.setEndDate(endDate);
					userServiceRepository.save(userService);
			}
		}
	}

	private void chargebackSet(UserService userService) {
		LocalDateTime end = userService.getEndDate();
		long days = ChronoUnit.DAYS.between(LocalDateTime.now(), end);
		OrderItem orderItem = OrderUtils.getItemsByType(userService.getOrder(), ItemType.SET).stream().findFirst().get();
		int totalDays = orderItem.getCount() * Integer.parseInt(orderItem.getItem().getColor());
		BigDecimal chargebackAmount = userService.getOrder().getOrderTotal().divide(BigDecimal.valueOf(totalDays), 0, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(days).abs());
		User user = userService.getUser();
		user.setBalance(user.getBalance().add(chargebackAmount));
		userRepository.save(user);
		userServiceRepository.delete(userService);
	}

	private Map<ItemType, List<OrderItem>> splitTypes(Order order) {
		Map<ItemType, List<OrderItem>> separatedItems = new HashMap<>();
		for (OrderItem orderItem : order.getOrderItems()) {
			ItemType itemType = orderItem.getItem().getItemType();
			List<OrderItem> orderItems = separatedItems.getOrDefault(itemType, new ArrayList<>());
			orderItems.add(orderItem);
			separatedItems.put(itemType, orderItems);
		}
		return separatedItems;
	}

	public Page<Order> getAllUserOrders(User user, Store store, Pageable pageable) {
		return orderRepository.findAllByUserAndStoreAndStatus(user, store, OrderStatus.COMPLETE, pageable);
	}
}
