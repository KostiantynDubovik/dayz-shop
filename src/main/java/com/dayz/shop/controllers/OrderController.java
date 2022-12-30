package com.dayz.shop.controllers;

import com.dayz.shop.ProcessMessage;
import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.service.OrderService;
import com.dayz.shop.utils.OrderUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import java.util.List;

@Aspect
@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order getOrder(@RequestAttribute Store store) {
		return OrderUtils.getCurrentOrder(store);
	}

	@GetMapping("all")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public List<Order> getOrders(@RequestAttribute Store store, OpenIDAuthenticationToken principal) {
		return orderService.getAllUserOrders((User) principal.getPrincipal(), store);
	}

	@PostMapping("add/{item}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order addOrderItem(@PathVariable Item item, @RequestAttribute Store store) {
		return orderService.addOrderItem(item, store);
	}

	@DeleteMapping("delete/{item}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order deleteOrderItem(@PathVariable Item item, @RequestAttribute Store store) {
		return orderService.deleteOrderItem(item, store);
	}

	@ProcessMessage
	@PostMapping("{item}/{server}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order buyItemNow(@PathVariable Item item, @PathVariable Server server, @RequestAttribute Store store) {
		try {
			return orderService.buyItemNow(item, store, server);
		} catch (Exception e) {
			throw new ServerErrorException(e.getMessage(), e);
		}
	}

	@ProcessMessage
	@PostMapping()
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order placeOrder(@RequestAttribute Store store) {
		try {
			return orderService.placeOrder(store);
		} catch (Exception e) {
			throw new ServerErrorException(e.getMessage(), e);
		}
	}
}
