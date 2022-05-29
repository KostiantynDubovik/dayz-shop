package com.dayz.shop.handlers;

import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.OrderRepository;
import com.dayz.shop.srvice.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

	private final OrderRepository orderRepository;
	private final OrderService orderService;

	@Autowired
	public OrderController(OrderRepository orderRepository, OrderService orderService) {
		this.orderRepository = orderRepository;
		this.orderService = orderService;
	}

	@PostMapping("add/{item}")
	public Order addOrderItem(@PathVariable Item item, @RequestAttribute Store store) {
		return orderService.addOrderItem(item); //TODO
	}

	@PostMapping("{item}")
	public Order buyItemNow(@PathVariable Item item, @RequestAttribute Store store) {
		return null; //TODO
	}
}
