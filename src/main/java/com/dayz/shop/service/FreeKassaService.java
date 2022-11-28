package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.repository.StoreConfigRepository;
import nonapi.io.github.classgraph.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.ws.rs.core.UriBuilder;
import java.math.RoundingMode;
import java.net.URI;

@Service
public class FreeKassaService {
	public static final String MERCHAND_ID_KEY = "m";
	public static final String AMOUNT_KEY = "oa";
	public static final String SIGNATURE_KEY = "s";
	public static final String PAYMENT_ID_KEY = "o";
	public static final String CURRENCY_KEY = "currency";
	private final StoreConfigRepository storeConfigRepository;
	private final PaymentRepository paymentRepository;

	@Autowired
	public FreeKassaService(StoreConfigRepository storeConfigRepository, PaymentRepository paymentRepository) {
		this.storeConfigRepository = storeConfigRepository;
		this.paymentRepository = paymentRepository;
	}

	public URI initPayment(Payment payment) {
		payment.setPaymentStatus(OrderStatus.PENDING);
		payment.setPaymentType(PaymentType.FREEKASSA);
		payment = paymentRepository.save(payment);
		return buildRedirectUrl(payment);
	}

	private URI buildRedirectUrl(Payment payment) { //TODO
		Store store = payment.getStore();
		String merchantId = getStoreConfigValue("freekassa.merchantId", store);
		String secret = getStoreConfigValue("freekassa.secret", store);

		String amount = payment.getAmount().setScale(2, RoundingMode.UNNECESSARY).toString();
		Long paymentId = payment.getId();
		Currency currency = payment.getCurrency();
		String sign = StringUtils.join(":", merchantId, amount, secret, currency, paymentId);

		String signHashed = DigestUtils.md5DigestAsHex(sign.getBytes());

		return UriBuilder.fromUri(getStoreConfigValue("freekassa.baseUrl", store))
				.queryParam(MERCHAND_ID_KEY, merchantId)
				.queryParam(AMOUNT_KEY, amount)
				.queryParam(CURRENCY_KEY, currency)
				.queryParam(PAYMENT_ID_KEY, paymentId)
				.queryParam(SIGNATURE_KEY, signHashed)
				.build();
	}

	private String getStoreConfigValue(String key, Store store) {
		return storeConfigRepository.findByKeyAndStore(key, store).getValue();
	}
}
