package com.dayz.shop.handlers;

import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("add/{item}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order addOrderItem(@PathVariable Item item, @RequestAttribute Store store) {
		return orderService.addOrderItem(item, store);
	}

	@DeleteMapping("add/{item}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order deleteOrderItem(@PathVariable Item item,@RequestAttribute Store store) {
		return orderService.deleteOrderItem(item, store);
	}

	@PostMapping("{item}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order buyItemNow(@PathVariable Item item, @RequestAttribute Store store) {
		try {
			return orderService.buyItemNow(item, store);
		} catch (Exception e) {
			throw new ServerErrorException(e.getMessage(), e);
		}
	}

	@PostMapping
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order placeOrder(@RequestAttribute Store store) {
		try {
			return orderService.placeOrder(store);
		} catch (Exception e) {
			throw new ServerErrorException(e.getMessage(), e);
		}
	}
}
