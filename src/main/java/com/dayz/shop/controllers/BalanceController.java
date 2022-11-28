package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Currency;
import com.dayz.shop.jpa.entities.Payment;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.service.AdminChargeService;
import com.dayz.shop.service.FreeKassaService;
import com.dayz.shop.service.UserService;
import com.dayz.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {
	private final UserService userService;
	private final FreeKassaService freeKassaService;
	private final AdminChargeService adminChargeService;

	@Autowired
	public BalanceController(UserService userService, FreeKassaService freeKassaService, AdminChargeService adminChargeService) {
		this.userService = userService;
		this.freeKassaService = freeKassaService;
		this.adminChargeService = adminChargeService;
	}

	@PostMapping("confirm")
	public void confirmPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		userService.updateUserBalance(Utils.getCurrentUser(), BigDecimal.valueOf(100));
		response.sendRedirect("/profile");
	}

	@PostMapping("cancel")
	public void cancelPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect("/profile");
	}

	@PostMapping("notify")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String notify(HttpServletRequest request, HttpServletResponse response, @RequestAttribute("store") Store store) {
		return freeKassaService.notify(request, response, store);
	}

	@PostMapping("init")
	public void initPayment(HttpServletResponse response, @ModelAttribute Payment payment, @RequestAttribute("store") Store store) throws IOException {
		payment.setUser(Utils.getCurrentUser());
		payment.setStore(store);
		payment.setCurrency(Currency.RUB);
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
