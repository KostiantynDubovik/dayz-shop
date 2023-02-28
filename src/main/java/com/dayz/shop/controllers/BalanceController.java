package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.service.BalanceTransferService;
import com.dayz.shop.service.FreeKassaService;
import com.dayz.shop.service.YooMoneyService;
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
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {
	private final FreeKassaService freeKassaService;
	private final YooMoneyService yooMoneyService;
	private final BalanceTransferService balanceTransferService;
	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;

	@Autowired
	public BalanceController(FreeKassaService freeKassaService, YooMoneyService yooMoneyService, PaymentRepository paymentRepository,
								BalanceTransferService balanceTransferService, UserRepository userRepository) {
		this.freeKassaService = freeKassaService;
		this.yooMoneyService = yooMoneyService;
		this.paymentRepository = paymentRepository;
		this.balanceTransferService = balanceTransferService;
		this.userRepository = userRepository;
	}

	@PostMapping("notify")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String notify(HttpServletRequest request, HttpServletResponse response, @RequestAttribute("store") Store store) {
		return freeKassaService.notify(request);
	}

	@PostMapping("init")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public void initPayment(HttpServletResponse response, @ModelAttribute Payment payment, @RequestAttribute("store") Store store, String steamId) throws IOException {
		if (payment.getUser() == null) {
			User currentUser = Utils.getCurrentUser();
			payment.setUser(currentUser);
			payment.setUserFrom(Utils.DEFAULT_USER);
		}
		payment.setStore(store);
		payment.setCurrency(Currency.RUB);
		String redirectUrl;
		switch (payment.getType()) {
			case FREEKASSA:
				redirectUrl = freeKassaService.initPayment(payment);
				break;
			case YOOMONEY:
				redirectUrl = yooMoneyService.initPayment(payment);
				break;
			default:
				redirectUrl = "/profile";
		}
		response.sendRedirect(redirectUrl);
	}

	@PostMapping(value = "transfer/{steamId}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Payment transferBalance(@RequestBody Payment payment, @RequestAttribute("store") Store store, @PathVariable(name = "steamId") String steamId) {
		payment.setStore(store);
		payment.setCurrency(Currency.RUB);
		payment.setType(Type.TRANSFER);
		payment.setStatus(OrderStatus.PENDING);
		payment.setChargeTime(LocalDateTime.now());
		User currentUser = Utils.getCurrentUser();
		boolean isSelfCharge = currentUser.getSteamId().equals(steamId);
		if(isSelfCharge && !Utils.isStoreAdmin()) {
			failPayment(payment, "transfer.failed.selfcharge", store);
		} else if (payment.getAmount().compareTo(BigDecimal.ZERO) > 0 || Utils.isStoreAdmin(currentUser)) {
			payment.setUserFrom(currentUser);
			User userTo = isSelfCharge ? currentUser : userRepository.getBySteamIdAndStore(steamId, store);
			if (userTo == null && Utils.isStoreAdmin(currentUser)) {
				userTo = Utils.createUser(store, steamId);
			}
			if (userTo != null) {
				payment.setUser(userTo);
				payment = balanceTransferService.doTransfer(payment);
			} else {
				failPayment(payment, "transfer.failed.no_user", store);
			}
		} else {
			failPayment(payment, "transfer.failed.negative_amount", store);
		}
		return payment;
	}

	private static void failPayment(Payment payment, String key, Store store) {
		payment.setStatus(OrderStatus.FAILED);
		payment.getProperties().put("message",  Utils.getMessage(key, store));
	}

	@GetMapping("{paymentId}")
	@SuppressWarnings("deprecation")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Payment getPaymentById(@PathVariable("paymentId") Long paymentId, OpenIDAuthenticationToken principal) {
		Payment result = null;
		User user = (User) principal.getPrincipal();
		if (Utils.isAppAdmin(user)) {
			result = paymentRepository.findById(paymentId).orElse(null);
		} else if (Utils.isStoreAdmin(user)) {
			Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
			if (paymentOptional.isPresent() && user.getStore().equals(paymentOptional.get().getStore())) {
				result = paymentOptional.get();
			}
		} else {
			result = paymentRepository.findByIdAndUser(paymentId, user);
		}
		return result;
	}

	@GetMapping("all/balance/{steamId}/{page}")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public Page<Payment> getAllUserBalance(@RequestAttribute Store store, @PathVariable String steamId, @PathVariable int page, @RequestParam(defaultValue = "10") int pageSize) {
		Pageable pageable = getPageable(page, pageSize);
		return paymentRepository.findAllByUserAndStoreAndStatusAndTypeIn(userRepository.getBySteamIdAndStore(steamId, store), store, OrderStatus.COMPLETE, Arrays.asList(Type.TRANSFER, Type.FREEKASSA), pageable);
	}

	@GetMapping("all/payments/{steamId}/{page}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Page<Payment> getAllUserPayments(@RequestAttribute Store store, @PathVariable String steamId, @PathVariable int page, @RequestParam(defaultValue = "10") int pageSize) {
		Pageable pageable = getPageable(page, pageSize);
		return paymentRepository.findAllByUserAndStoreAndStatusAndTypeIn(userRepository.getBySteamIdAndStore(steamId, store), store, OrderStatus.COMPLETE, Collections.singletonList(Type.FREEKASSA), pageable);
	}

	@GetMapping("all/transfers/{steamId}/{page}")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Page<Payment> getAllUserTransfers(@RequestAttribute Store store, @PathVariable String steamId, @PathVariable int page, @RequestParam(defaultValue = "10") int pageSize) {
		Pageable pageable = getPageable(page, pageSize);
		return paymentRepository.findAllByUserAndStoreAndStatusAndTypeIn(userRepository.getBySteamIdAndStore(steamId, store), store, OrderStatus.COMPLETE, Collections.singletonList(Type.TRANSFER), pageable);
	}

	@GetMapping("all/balance/self/{page}")
	@SuppressWarnings("deprecation")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Page<Payment> getAllSelfBalance(@RequestAttribute Store store, OpenIDAuthenticationToken principal, @PathVariable int page, @RequestParam(defaultValue = "10") int pageSize) {
		Pageable pageable = getPageable(page, pageSize);
		return paymentRepository.findAllByUserAndStoreAndStatusAndTypeIn((User) principal.getPrincipal(), store, OrderStatus.COMPLETE, Arrays.asList(Type.TRANSFER, Type.FREEKASSA), pageable);
	}

	@GetMapping("all/payments/self/{page}")
	@SuppressWarnings("deprecation")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Page<Payment> getSelfPayments(@RequestAttribute Store store, OpenIDAuthenticationToken principal, @PathVariable int page, @RequestParam(defaultValue = "10") int pageSize) {
		Pageable pageable = getPageable(page, pageSize);
		return paymentRepository.findAllByUserAndStoreAndStatusAndTypeIn((User) principal.getPrincipal(), store, OrderStatus.COMPLETE, Collections.singletonList(Type.FREEKASSA), pageable);
	}

	@GetMapping("all/transfers/self/{page}")
	@SuppressWarnings("deprecation")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public Page<Payment> getSelfTransfers(@RequestAttribute Store store, OpenIDAuthenticationToken principal, @PathVariable int page, @RequestParam(defaultValue = "10") int pageSize) {
		Pageable pageable = getPageable(page, pageSize);
		return paymentRepository.findAllByUserAndStoreAndStatusAndTypeIn((User) principal.getPrincipal(), store, OrderStatus.COMPLETE, Collections.singletonList(Type.TRANSFER), pageable);
	}

	private static Pageable getPageable(int page, int pageSize) {
		return PageRequest.of(page > 0 ? page - 1 : 0, Math.max(pageSize, 5), Sort.by("chargeTime").descending());
	}
}
