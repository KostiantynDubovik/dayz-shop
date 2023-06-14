package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.jpa.entities.UserService;
import com.dayz.shop.repository.UserServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/services")
public class UserServicesController {

	private final UserServiceRepository userServiceRepository;

	@Autowired
	public UserServicesController(UserServiceRepository userServiceRepository) {
		this.userServiceRepository = userServiceRepository;
	}

	@Transactional
	@GetMapping("/self")
	@SuppressWarnings({"deprecation"})
	@PreAuthorize("hasAuthority('STORE_READ')")
	public List<UserService> getSelf(OpenIDAuthenticationToken principal) {
		return userServiceRepository.findByUser((User) principal.getPrincipal());
	}
}
