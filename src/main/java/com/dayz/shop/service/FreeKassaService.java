package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dayz.shop.utils.Utils.US_STORE_KEY;

@Service
public class FreeKassaService {
	public static final String MERCHAND_ID_KEY = "m";
	public static final String AMOUNT_KEY = "oa";
	public static final String SIGNATURE_KEY = "s";
	public static final String PAYMENT_ID_KEY = "o";
	public static final String CURRENCY_KEY = "currency";
	private final PaymentRepository paymentRepository;
	private final UserService userService;
	private final FundTransferService fundTransferService;

	@Autowired
	public FreeKassaService(PaymentRepository paymentRepository, UserService userService, FundTransferService fundTransferService) {
		this.paymentRepository = paymentRepository;
		this.userService = userService;
		this.fundTransferService = fundTransferService;
	}

	public String initPayment(Payment payment) {
		payment.setStatus(OrderStatus.PENDING);
		payment.setType(Type.FREEKASSA);
		payment.setChargeTime(LocalDateTime.now());
		payment.setBalanceBefore(payment.getUser().getBalance());
		payment.setBalanceAfter(payment.getUser().getBalance().add(payment.getAmount()));
		payment.setDirection(PaymentDirection.INCOMING);
		payment = paymentRepository.save(payment);
		return buildRedirectUrl(payment);
	}

	private String buildRedirectUrl(Payment payment) {
		payment.setDirection(PaymentDirection.INCOMING);
		Store store = payment.getStore();

		String merchantId = Utils.getStoreConfig("freekassa.merchantId", store);
		String amount = payment.getAmount().setScale(2, RoundingMode.UNNECESSARY).toString();
		Long paymentId = payment.getId();
		Currency currency = payment.getCurrency();
		String signHashed = Utils.getFreekassaSignatureForPayment(payment);

		paymentRepository.save(payment);
		return UriBuilder.fromUri(Utils.getStoreConfig("freekassa.baseUrl", store))
				.queryParam(MERCHAND_ID_KEY, merchantId)
				.queryParam(AMOUNT_KEY, amount)
				.queryParam(CURRENCY_KEY, currency)
				.queryParam(PAYMENT_ID_KEY, paymentId)
				.queryParam(SIGNATURE_KEY, signHashed)
				.queryParam(US_STORE_KEY, payment.getStore().getStoreName())
				.build().toString();
	}

	public String notify(HttpServletRequest request) {
		String result = "NO";
		Map<String, String> parameterMap = request.getParameterMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, (e)-> e.getValue()[0]));
		Optional<Payment> paymentOptional = paymentRepository.findById(Long.valueOf(parameterMap.get("MERCHANT_ORDER_ID")));
		try {
			if (Utils.isFreeKassaIp(request)) {
				if (paymentOptional.isPresent()) {
					Payment payment = paymentOptional.get();
					payment.getProperties().putAll(parameterMap);
					payment.setStatus(OrderStatus.COMPLETE);
					BigDecimal amount = new BigDecimal(parameterMap.get("AMOUNT"));
					if (amount.compareTo(BigDecimal.valueOf(3000)) > -1) {
						payment.setAmount(amount.multiply(BigDecimal.valueOf(1.33)));
					}
					payment.getProperties().put("message", Utils.getMessage("payment.success", payment.getStore(), payment.getAmount(), payment.getCurrency()));
					payment.setBalanceBefore(payment.getUser().getBalance());
					userService.updateUserBalance(payment.getUser(), payment.getAmount());
					payment.setBalanceAfter(payment.getUser().getBalance());
					paymentRepository.save(payment);
					boolean commissionEnabled = Boolean.parseBoolean(Utils.getStoreConfig("comisson.enabled", payment.getStore()));
					if (commissionEnabled) {
						fee(payment);
					}
					result = "YES";
				}
			}
		} catch (Exception e) {
			if(paymentOptional.isPresent()) {
				Payment payment = paymentOptional.get();
				payment.getProperties().put("message", Utils.getMessage("payment.failed", payment.getStore()));
				payment.setBalanceBefore(payment.getUser().getBalance());
				payment.setBalanceAfter(payment.getUser().getBalance());
				paymentRepository.save(payment);
			}
			return result;
		}
		return result;
	}

	private void fee(Payment payment) throws NoSuchAlgorithmException, InvalidKeyException {
		fundTransferService.transfer(payment);
	}
}
