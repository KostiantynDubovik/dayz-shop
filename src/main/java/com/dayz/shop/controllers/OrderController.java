package com.dayz.shop.controllers;

import com.dayz.shop.ProcessMessage;
import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.repository.UserServiceRepository;
import com.dayz.shop.service.OrderService;
import com.dayz.shop.utils.OrderUtils;
import com.dayz.shop.utils.Utils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Aspect
@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final OrderService orderService;
	private final UserRepository userRepository;
	private final UserServiceRepository userServiceRepository;

	@Autowired
	public OrderController(OrderService orderService, UserRepository userRepository,
	                       UserServiceRepository userServiceRepository) {
		this.orderService = orderService;
		this.userRepository = userRepository;
		this.userServiceRepository = userServiceRepository;
	}

	@GetMapping
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order getOrder(@RequestAttribute Store store) {
		return OrderUtils.getCurrentOrder(store);
	}

	@GetMapping("all/{page}")
	@SuppressWarnings("deprecation")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Page<Order> getAllUserOrders(@RequestAttribute Store store, OpenIDAuthenticationToken principal, @PathVariable int page, @RequestParam(defaultValue = "10") int pageSize) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, pageSize < 5 ? 5 : pageSize, Sort.by("timePlaced").descending());
		return orderService.getAllUserOrders((User) principal.getPrincipal(), store, pageable);
	}

	@GetMapping("all/{steamId}/{page}")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public Page<Order> getAllUserOrdersBySteamId(@RequestAttribute Store store, @PathVariable String steamId, @PathVariable int page, @RequestParam(defaultValue = "10") int pageSize) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, pageSize < 5 ? 5 : pageSize, Sort.by("timePlaced").descending());
		return orderService.getAllUserOrders(userRepository.getBySteamIdAndStore(steamId, store), store, pageable);
	}

	@PostMapping("add/{item}/{count}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order addOrderItem(@PathVariable Item item, @RequestAttribute Store store, @PathVariable(required = false) Integer count) {
		return orderService.addOrderItem(item, store, count == null || count < 1 ? 1 : count);
	}

	@DeleteMapping("delete/{item}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order deleteOrderItem(@PathVariable Item item, @RequestAttribute Store store) {
		return orderService.deleteOrderItem(item, store);
	}

	@ProcessMessage
	@PostMapping(value = {"{item}/{server}/{count}/{steamIdTo}","{item}/{server}/{count}"})
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order buyItemNow(@PathVariable Item item, @PathVariable Server server, @RequestAttribute Store store, @PathVariable(required = false) Integer count, @PathVariable(required = false) String steamIdTo, HttpServletResponse response) {
		try {
			User user = Utils.getCurrentUser();
			User userTo = Utils.getUserTo(user, steamIdTo);
			Order order;
			if (userTo == null) {
				order = OrderUtils.createOrder(user, store, user);
				order.setStatus(OrderStatus.FAILED);
				order.getProperties().put("message", Utils.getMessage("transfer.failed.no_user", store));
			} else {
				order = orderService.buyItemNow(item, store, server, count == null || count < 1 ? 1 : count, user, userTo);
				if (ItemType.CUSTOM_SET.equals(item.getItemType()) && user == userTo && OrderStatus.COMPLETE.equals(order.getStatus())) {
					response.sendRedirect("/custom");
				}
			}
			return order;
		} catch (Exception e) {
			throw new ServerErrorException(e.getMessage(), e);
		}
	}

	@ProcessMessage
	@PostMapping("customise")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order changeCustomSet(@RequestParam("itemId") Map<Item, Integer> items, @RequestAttribute Store store) {
		try {
			User user = Utils.getCurrentUser();
			UserService customSet = userServiceRepository.findAnyByUserAndItemType(user, ItemType.CUSTOM_SET);
			Order order;
			if (customSet != null) {
				order = orderService.changeCustomSet(items, store, user, user);
			} else {
				order = OrderUtils.createOrder(user, store, user);
				order.setStatus(OrderStatus.FAILED);
				order.getProperties().put("message", Utils.getMessage("custom.set.not.active", store));
			}
			return order;
		} catch (Exception e) {
			throw new ServerErrorException(e.getMessage(), e);
		}
	}

	@ProcessMessage
	@PostMapping("/{steamIdTo}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Order placeOrder(@RequestAttribute Store store, @PathVariable(required = false) String steamIdTo) {
		try {
			User user = Utils.getCurrentUser();
			User userTo = Utils.getUserTo(user, steamIdTo);
			if (userTo == null) {
				throw new Exception();
			}
			return orderService.placeOrder(store, user, userTo);
		} catch (Exception e) {
			throw new ServerErrorException(e.getMessage(), e);
		}
	}
}
