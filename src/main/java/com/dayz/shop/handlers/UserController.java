package com.dayz.shop.handlers;

import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	@PreAuthorize("hasAuthority('STORE_READ')")
	public User getSelf(OpenIDAuthenticationToken principal) {
		return (User) principal.getPrincipal();
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
