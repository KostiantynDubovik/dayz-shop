package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.OrderItem;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orderitem")
public class OrderItemController {

	private final OrderItemRepository orderItemRepository;

	@Autowired
	public OrderItemController(OrderItemRepository orderItemRepository) {
		this.orderItemRepository = orderItemRepository;
	}

	@PutMapping("/{orderItem}/{count}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public OrderItem addOrderItem(@PathVariable OrderItem orderItem, @PathVariable Integer count, @RequestAttribute Store store) {
		orderItem.setCount(count);
		return orderItemRepository.save(orderItem);
	}
}
