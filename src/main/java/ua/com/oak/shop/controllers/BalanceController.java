package ua.com.oak.shop.controllers;

import com.dayz.shop.jpa.entities.*;
import com.dubochok.shop.jpa.entities.*;
import ua.com.oak.shop.jpa.entities.*;
import ua.oak.shop.jpa.entities.*;
import ua.com.oak.shop.repository.PaymentRepository;
import ua.com.oak.shop.repository.UserRepository;
import ua.com.oak.shop.service.BalanceTransferService;
import ua.com.oak.shop.service.FreeKassaService;
import ua.com.oak.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {
	private final FreeKassaService freeKassaService;
	private final BalanceTransferService balanceTransferService;

	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;

	@Autowired
	public BalanceController(FreeKassaService freeKassaService,
	                         PaymentRepository paymentRepository, BalanceTransferService balanceTransferService,
	                         UserRepository userRepository) {
		this.freeKassaService = freeKassaService;
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
			payment.setUserFrom(currentUser);
		}
		payment.setStore(store);
		payment.setCurrency(Currency.RUB);
		String redirectUrl;
		switch (payment.getType()) {
			case FREEKASSA:
				redirectUrl = freeKassaService.initPayment(payment);
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
		if (payment.getAmount().compareTo(BigDecimal.ZERO) > 0 || Utils.isStoreAdmin(currentUser)) {
			payment.setUserFrom(currentUser);
			boolean isSelfCharge = Utils.isStoreAdmin() && currentUser.getSteamId().equals(steamId);
			User userTo = isSelfCharge ? currentUser : userRepository.getBySteamIdAndStore(steamId, store);
			if (userTo == null && Utils.isStoreAdmin(currentUser)) {
				userTo = Utils.createUser(store, steamId);
			}
			if (userTo != null) {
				payment.setUser(userTo);
				balanceTransferService.doTransfer(payment);
			} else {
				payment.setStatus(OrderStatus.FAILED);
				payment.getProperties().put("message", Utils.getMessage("transfer.failed.no_user", store));
				paymentRepository.save(payment);
			}
		} else {
			payment.setStatus(OrderStatus.FAILED);
			payment.getProperties().put("message",  Utils.getMessage("transfer.failed.negative_amount", store));
			paymentRepository.save(payment);
		}
		return payment;
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

	@GetMapping("all/{page}")
	@SuppressWarnings("deprecation")
	@PreAuthorize("hasAuthority('STORE_READ')")
	public List<Payment> getPaymentById(@RequestAttribute Store store, OpenIDAuthenticationToken principal, @PathVariable int page, @RequestParam(defaultValue = "20") int pageSize) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, pageSize < 20 ? 20 : pageSize, Sort.by("chargeTime"));
		return paymentRepository.findAllByUserAndStoreAndStatusAndTypeIn((User) principal.getPrincipal(), store, OrderStatus.COMPLETE, Arrays.asList(Type.TRANSFER, Type.FREEKASSA), pageable);
	}
}
