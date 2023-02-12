package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.OrderItem;
import com.dayz.shop.jpa.entities.OrderStatus;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.json.GameServerSideOrderItem;
import com.dayz.shop.repository.OrderItemRepository;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.service.OrderItemService;
import com.dayz.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/orderitem")
public class OrderItemController {
	private final OrderItemService orderItemService;
	private final UserRepository userRepository;
	private final OrderItemRepository orderItemRepository;

	@Autowired
	public OrderItemController(OrderItemRepository orderItemRepository, OrderItemService orderItemService,
	                           UserRepository userRepository) {
		this.orderItemService = orderItemService;
		this.userRepository = userRepository;
		this.orderItemRepository = orderItemRepository;
	}

	@PutMapping("/{orderItem}/{count}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public OrderItem addOrderItem(@PathVariable OrderItem orderItem, @PathVariable Integer count, @RequestAttribute Store store) {
		orderItem.setCount(count);
		orderItem.setStore(store);
		return orderItemRepository.save(orderItem);
	}

	@GetMapping("all/{steamId}/{page}")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public Page<OrderItem> getAllUserOrderItemsBySteamId(@RequestAttribute Store store, @PathVariable String steamId, @PathVariable int page, @RequestParam(defaultValue = "10") int pageSize) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, pageSize < 5 ? 5 : pageSize, Sort.by("boughtTime").descending());
		return orderItemRepository.getAllByUserAndStoreAndStatus(userRepository.getBySteamIdAndStore(steamId, store), store, OrderStatus.COMPLETE, pageable);
	}

	@GetMapping("all/self/{page}")
	@SuppressWarnings("deprecation")
	public Page<OrderItem> getSelfOrderItems(OpenIDAuthenticationToken principal, @RequestAttribute Store store, @PathVariable int page, @RequestParam(defaultValue = "10") int pageSize) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, pageSize < 5 ? 5 : pageSize, Sort.by("boughtTime").descending());
		return orderItemRepository.getAllByUserAndStoreAndStatus((User) principal.getPrincipal(), store, OrderStatus.COMPLETE, pageable);
	}

	@PostMapping("/received")
	public void markItemReceived(@RequestAttribute Store store, HttpServletRequest request, @RequestBody GameServerSideOrderItem requestOrderItem) {
		if (Utils.isStoreServersRequest(request, store)) {
			OrderItem orderItem = orderItemRepository.getById(requestOrderItem.getProductCode());
			orderItem.setReceived(true);
			orderItem.setReceiveDateTime(requestOrderItem.getTime());
			orderItemRepository.save(orderItem);
		}
	}
}
