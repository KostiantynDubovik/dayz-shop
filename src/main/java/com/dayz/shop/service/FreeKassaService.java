package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.repository.StoreConfigRepository;
import nonapi.io.github.classgraph.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.DatatypeConverter;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class FreeKassaService {
	public static final String MERCHAND_ID_KEY = "m";
	public static final String AMOUNT_KEY = "oa";
	public static final String SIGNATURE_KEY = "s";
	public static final String PAYMENT_ID_KEY = "o";
	public static final String CURRENCY_KEY = "currency";
	private StoreConfigRepository storeConfigRepository;
	private PaymentRepository paymentRepository;

	@Autowired
	public FreeKassaService(StoreConfigRepository storeConfigRepository, PaymentRepository paymentRepository) {
		this.storeConfigRepository = storeConfigRepository;
		this.paymentRepository = paymentRepository;
	}

	public String initPayment(Payment payment) {
		payment.setPaymentStatus(OrderStatus.PENDING);
		payment.setPaymentType(PaymentType.FREEKASSA);
		payment = paymentRepository.save(payment);
		return buildRedirectUrl(payment);
	}

	private String buildRedirectUrl(Payment payment) { //TODO
		Store store = payment.getStore();
		String merchantId = getStoreConfigValue("freekassa.merchantId", store);
		String secret = getStoreConfigValue("freekassa.secret", store);
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		String amount = payment.getAmount().setScale(2, RoundingMode.UNNECESSARY).toString();
		Long paymentId = payment.getId();
		Currency currency = payment.getCurrency();
		String sign = StringUtils.join(":", merchantId, amount, secret, currency, paymentId);
		md.update(sign.getBytes());
		byte[] digest = md.digest();
		String signHashed = DatatypeConverter
				.printHexBinary(digest).toUpperCase();

		return UriBuilder.fromUri(getStoreConfigValue("freekassa.baseUrl", store)).queryParam(MERCHAND_ID_KEY, merchantId)
				.queryParam(AMOUNT_KEY, amount)
				.queryParam(SIGNATURE_KEY, signHashed)
				.queryParam(CURRENCY_KEY, currency)
				.queryParam(PAYMENT_ID_KEY, paymentId)
				.build().toString();
	}

	private String getStoreConfigValue(String key, Store store) {
		return storeConfigRepository.findByKeyAndStore(key, store).getValue();
	}
}
