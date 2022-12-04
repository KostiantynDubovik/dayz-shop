package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Currency;
import com.dayz.shop.jpa.entities.Payment;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.service.AdminChargeService;
import com.dayz.shop.service.FreeKassaService;
import com.dayz.shop.service.FriendChargeService;
import com.dayz.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {
	private final FreeKassaService freeKassaService;
	private final AdminChargeService adminChargeService;
	private final FriendChargeService friendChargeService;

	private final PaymentRepository paymentRepository;

	@Autowired
	public BalanceController(FreeKassaService freeKassaService, AdminChargeService adminChargeService,
							 PaymentRepository paymentRepository, FriendChargeService friendChargeService) {
		this.freeKassaService = freeKassaService;
		this.adminChargeService = adminChargeService;
		this.paymentRepository = paymentRepository;
		this.friendChargeService = friendChargeService;
	}

	@PostMapping("notify")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String notify(HttpServletRequest request, HttpServletResponse response, @RequestAttribute("store") Store store) {
		return freeKassaService.notify(request, response, store);
	}

	@GetMapping("{paymentId}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Payment getOrderById(@PathVariable("paymentId") Long paymentId, OpenIDAuthenticationToken principal) {
		Payment result = null;
		Optional<Payment> fromRepo = paymentRepository.findById(paymentId);
		if (fromRepo.isPresent() && fromRepo.get().getUser().equals(principal.getPrincipal())) {
			result = fromRepo.get();
		}
		return result;
	}

	@PostMapping("init")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public void initPayment(HttpServletResponse response, @ModelAttribute Payment payment, @RequestAttribute("store") Store store) throws IOException {
		if (payment.getUser() == null) {
			payment.setUser(Utils.getCurrentUser());
		}
		payment.setStore(store);
		payment.setCurrency(Currency.RUB);
		String redirectUrl;
		switch (payment.getPaymentType()) {
			case ADMIN:
				redirectUrl = adminChargeService.initPayment(payment);
				break;
			case FRIEND:
				redirectUrl = friendChargeService.initPayment(payment);
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
