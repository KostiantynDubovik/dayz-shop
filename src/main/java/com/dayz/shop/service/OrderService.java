package com.dayz.shop.service;

import com.dayz.shop.exception.BalanceTooLowException;
import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.OrderItemRepository;
import com.dayz.shop.repository.OrderRepository;
import com.dayz.shop.utils.OrderUtils;
import com.dayz.shop.utils.Utils;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final SendToServerService sendToServerService;
	private final OrderUtils orderUtils;

	@Autowired
	public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, SendToServerService sendToServerService, OrderUtils orderUtils) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.sendToServerService = sendToServerService;
		this.orderUtils = orderUtils;
	}

	public Order addOrderItem(Item item, Store store) {
		User user = Utils.getCurrentUser();
		Order order = orderUtils.getCurrentOrder(user, store);
		OrderItem orderItem = orderUtils.createOrderItem(item, user, order);
		orderItemRepository.save(orderItem);
		orderUtils.recalculateOrder(order);
		return orderRepository.save(order);
	}

	public Order deleteOrderItem(Item item, Store store) {
		Order order = orderUtils.getCurrentOrder(store);
		OrderItem orderItem = orderItemRepository.findFirstByItemAndOrder(item, order);
		order.getOrderItems().remove(orderItem);
		orderUtils.recalculateOrder(order);
		orderItemRepository.delete(orderItem);
		return orderRepository.save(order);
	}

	public Order buyItemNow(Item item, Store store, Server server) throws BalanceTooLowException {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Order order = orderUtils.createOrder(user, store);
		order.setServer(server);
		orderRepository.save(order);
		OrderItem orderItem = orderUtils.createOrderItem(item, user, order);
		order.getOrderItems().add(orderItemRepository.save(orderItem));
		return placeOrder(order);
	}

	public Order placeOrder(Store store) throws BalanceTooLowException {
		return placeOrder(orderUtils.getCurrentOrder(Utils.getCurrentUser(), store));
	}

	public Order placeOrder(Order order) throws BalanceTooLowException {
		User user = order.getUser();
		if (user.getBalance().compareTo(order.getOrderTotal()) < 0) { //TODO
			throw new BalanceTooLowException(user.getBalance(), order.getOrderTotal());
		}
		try {
			sendToServerService.sendOrder(order);
			user.setBalance(user.getBalance().subtract(order.getOrderTotal()));
			order.setStatus(OrderStatus.COMPLETE);
			order.getOrderItems().forEach(orderItem -> orderItem.setStatus(OrderStatus.COMPLETE));
		} catch (JSchException | InterruptedException | SftpException | IOException e) {
			e.printStackTrace();
		}
		return orderRepository.save(order);
	}
}
