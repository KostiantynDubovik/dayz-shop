package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.repository.StoreConfigRepository;
import com.dayz.shop.utils.Utils;
import com.google.common.base.Splitter;
import nonapi.io.github.classgraph.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;
import java.math.RoundingMode;

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
				.build().toString();
	}

	private String getStoreConfigValue(String key, Store store) {
		return storeConfigRepository.findByKeyAndStore(key, store).getValue();
	}

	public String notify(HttpServletRequest request, HttpServletResponse response, Store store) {
		String result = "YES";
		try {
			if (isFreeKassaIp(request, store)) {

			}
		} catch (Exception e) {
			result = "NO";
		}
		return result;
	}

	public boolean isFreeKassaIp(HttpServletRequest request, Store store) {
		String reqIp = Utils.getClientIpAddress(request);
		return Splitter.on(',').splitToList(storeConfigRepository.findByKeyAndStore("freekassa.ips", store).getValue()).contains(reqIp);
	}
}
