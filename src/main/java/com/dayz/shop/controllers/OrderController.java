package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.OrderRepository;
import com.dayz.shop.service.OrderService;
import com.dayz.shop.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import javax.ws.rs.PathParam;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final OrderService orderService;
	private final OrderUtils orderUtils;
	private final OrderRepository orderRepository;

	@Autowired
	public OrderController(OrderService orderService, OrderUtils orderUtils, OrderRepository orderRepository) {
		this.orderService = orderService;
		this.orderUtils = orderUtils;
		this.orderRepository = orderRepository;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order getOrder(@RequestAttribute Store store) {
		return orderUtils.getCurrentOrder(store);
	}

	@GetMapping("{orderId}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order getOrderById(@PathVariable("orderId") Long orderId, OpenIDAuthenticationToken principal) {
		Order result = null;
		Order fromRepo = orderRepository.getById(orderId);
		if(fromRepo != null && fromRepo.getUser().equals(principal.getPrincipal())) {
			result = fromRepo;
		}
		return result;
	}

	@PostMapping("add/{item}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order addOrderItem(@PathVariable Item item, @RequestAttribute Store store) {
		return orderService.addOrderItem(item, store);
	}

	@DeleteMapping("delete/{item}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order deleteOrderItem(@PathVariable Item item,@RequestAttribute Store store) {
		return orderService.deleteOrderItem(item, store);
	}

	@PostMapping("{item}/{server}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order buyItemNow(@PathVariable Item item, @PathVariable Server server, @RequestAttribute Store store) {
		try {
			return orderService.buyItemNow(item, store, server);
		} catch (Exception e) {
			throw new ServerErrorException(e.getMessage(), e);
		}
	}

	@PostMapping("")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order placeOrder(@RequestAttribute Store store) {
		try {
			return orderService.placeOrder(store);
		} catch (Exception e) {
			throw new ServerErrorException(e.getMessage(), e);
		}
	}
}
