package com.dayz.shop.srvice;

import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	private final OrderRepository orderRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public Order addOrderItem(Item item, Store store) {

		return null; //TODO
	}

	public Order buyItemNow(Item item, Store store) {
		return null; //TODO
	}
}
