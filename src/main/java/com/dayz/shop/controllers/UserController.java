package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.OrderItem;
import com.dayz.shop.jpa.entities.OrderStatus;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.utils.Utils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	@GetMapping("/self")
	@SuppressWarnings({"deprecation", "unchecked"})
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Map<String, Object> getSelf(OpenIDAuthenticationToken principal) throws JsonProcessingException {
		User user = userRepository.getBySteamId(((User) principal.getPrincipal()).getSteamId());
		user.setPayments(user.getPayments().stream().filter(payment -> payment.getStatus() == OrderStatus.COMPLETE).collect(Collectors.toList()));

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		Map<String, Object> jsonUser = objectMapper.convertValue(user, Map.class);
		List<OrderItem> orderItems = user.getOrders().stream().filter(order -> order.getStatus() == OrderStatus.COMPLETE).flatMap(order -> order.getOrderItems().stream()).collect(Collectors.toList());
		jsonUser.put("orders", objectMapper.writeValueAsString(orderItems));
		return jsonUser;
	}

	@Transactional
	@GetMapping("app/{steamId}")
	@PreAuthorize("hasAuthority('APP_WRITE')")
	public User getUser(@PathVariable String steamId) {
		return userRepository.getBySteamId(steamId);
	}

	@Transactional
	@GetMapping("app")
	@PreAuthorize("hasAuthority('APP_WRITE')")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Transactional
	@GetMapping("store/list")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public List<User> getAllUsersByStore(@RequestAttribute Store store) {
		return userRepository.getAllByStore(store);
	}

	@SuppressWarnings("deprecation")
	@DeleteMapping("store/{userId}")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public void deactivateUser(@PathVariable("userId") User user, OpenIDAuthenticationToken principal) {
		if (Utils.isSameStore(user, principal) || Utils.isAppAdmin((User) principal.getPrincipal())) {
			user.setActive(false);
			userRepository.save(user);
		}
	}

	@PutMapping("app/update")
	@PreAuthorize("hasAuthority('APP_WRITE')")
	public User updateUser(@RequestBody User user) {
		return userRepository.save(user);
	}
}
