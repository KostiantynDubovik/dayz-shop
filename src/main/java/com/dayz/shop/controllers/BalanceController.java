package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Payment;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.service.FreeKassaService;
import com.dayz.shop.service.AdminChargeService;
import com.dayz.shop.service.UserService;
import com.dayz.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {
	private UserService userService;
	private FreeKassaService freeKassaService;
	private AdminChargeService adminChargeService;

	@Autowired
	public BalanceController(UserService userService, FreeKassaService freeKassaService, AdminChargeService adminChargeService) {
		this.userService = userService;
		this.freeKassaService = freeKassaService;
		this.adminChargeService = adminChargeService;
	}

	@PostMapping("confirm")
	public User confirmPayment(@RequestBody Map<String, Object> request) {
		BigDecimal amount = BigDecimal.TEN;
		return userService.updateUserBalance(Utils.getCurrentUser(), amount);
	}

	@PostMapping("cancel")
	public User cancelPayment(@RequestBody Map<String, Object> request) {
		BigDecimal amount = BigDecimal.TEN;
		return userService.updateUserBalance(Utils.getCurrentUser(), amount);
	}

	@PostMapping("notify")
	public User notify(@RequestBody Map<String, Object> request) {
		BigDecimal amount = BigDecimal.TEN;
		return userService.updateUserBalance(Utils.getCurrentUser(), amount);
	}

	@PostMapping("init")
	public void initPayment(HttpServletResponse response, @RequestBody Payment payment, @RequestAttribute("store") Store store) throws IOException {
		payment.setUser(Utils.getCurrentUser());
		payment.setStore(store);
		String redirectUrl;
		switch (payment.getPaymentType()) {
			case ADMIN:
				redirectUrl = adminChargeService.initPayment(payment);
				break;
			case FREEKASSA:
				redirectUrl = freeKassaService.initPayment(payment);
				break;
			default:
				redirectUrl = "/profile";
		}
		response.sendRedirect(redirectUrl);
	}
}
