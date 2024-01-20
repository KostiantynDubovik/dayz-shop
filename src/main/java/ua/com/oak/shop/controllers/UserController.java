package ua.com.oak.shop.controllers;

import ua.com.oak.shop.jpa.entities.OrderStatus;
import ua.com.oak.shop.jpa.entities.Store;
import ua.com.oak.shop.jpa.entities.User;
import ua.com.oak.shop.repository.UserRepository;
import ua.com.oak.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
	@SuppressWarnings("deprecation")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public User getSelf(OpenIDAuthenticationToken principal) {
		User user = userRepository.getBySteamId(((User) principal.getPrincipal()).getSteamId());
		user.setPayments(user.getPayments().stream().filter(payment -> payment.getStatus() == OrderStatus.COMPLETE).collect(Collectors.toList()));
		user.setOrders(user.getOrders().stream().filter(order -> order.getStatus() == OrderStatus.COMPLETE).collect(Collectors.toList()));
		return user;
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
