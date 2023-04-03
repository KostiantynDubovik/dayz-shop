package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Payment;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.service.ClearServices;
import com.dayz.shop.service.FundTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final ClearServices clearServices;
	private final FundTransferService fundTransferService;

	@Autowired
	public AdminController(ClearServices clearServices,
	                       FundTransferService fundTransferService) {
		this.clearServices = clearServices;
		this.fundTransferService = fundTransferService;
	}

	@Transactional
	@PostMapping("sync/service")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public void synchronizeServices(@RequestAttribute("store") Store store) throws Exception {
		clearServices.clearAll();
	}

	@Transactional
	@PostMapping("withdraw/{paymentId}")
	@PreAuthorize("hasAuthority('APP_WRITE')")
	public void createWithdraw(@PathVariable("paymentId") Payment payment) throws Exception {
		fundTransferService.transfer(payment);
	}
}
