package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.OrderItem;
import com.dayz.shop.jpa.entities.OrderStatus;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.OrderItemRepository;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orderitem")
public class OrderItemController {
	private final OrderService orderService;
	private final UserRepository userRepository;
	private final OrderItemRepository orderItemRepository;

	@Autowired
	public OrderItemController(OrderItemRepository orderItemRepository,OrderService orderService,
	                           UserRepository userRepository) {
		this.orderService = orderService;
		this.userRepository = userRepository;
		this.orderItemRepository = orderItemRepository;
	}

	@PutMapping("/{orderItem}/{count}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public OrderItem addOrderItem(@PathVariable OrderItem orderItem, @PathVariable Integer count, @RequestAttribute Store store) {
		orderItem.setCount(count);
		return orderItemRepository.save(orderItem);
	}

	@GetMapping("all/{steamId}/{page}")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public List<OrderItem> getAllUserOrderItemsBySteamId(@RequestAttribute Store store, @PathVariable String steamId, @PathVariable int page, @RequestParam(defaultValue = "20") int pageSize) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, pageSize < 20 ? 20 : pageSize, Sort.by("timePlaced"));
		return orderItemRepository.getAllByUserAndStoreAndStatus(userRepository.getBySteamIdAndStore(steamId, store), store,  OrderStatus.COMPLETE, pageable);
	}

	@GetMapping("all/self/{page}")
	@SuppressWarnings("deprecation")
	public List<OrderItem> getSelfOrderItems(OpenIDAuthenticationToken principal, @RequestAttribute Store store, @PathVariable int page, @RequestParam(defaultValue = "20") int pageSize) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, pageSize < 20 ? 20 : pageSize, Sort.by("timePlaced"));
		return orderItemRepository.getAllByUserAndStoreAndStatus((User) principal.getPrincipal(), store, OrderStatus.COMPLETE, pageable);
	}
}
