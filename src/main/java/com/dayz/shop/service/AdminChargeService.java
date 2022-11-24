package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.OrderStatus;
import com.dayz.shop.jpa.entities.Payment;
import com.dayz.shop.jpa.entities.PaymentType;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.repository.StoreConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.UriBuilder;
import java.math.RoundingMode;

@Service
public class AdminChargeService {
	private StoreConfigRepository storeConfigRepository;
	private PaymentRepository paymentRepository;

	@Autowired
	public AdminChargeService(StoreConfigRepository storeConfigRepository, PaymentRepository paymentRepository) {
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
		String url = "";
		Store store = payment.getUser().getStore();
		UriBuilder uriBuilder = UriBuilder.fromUri(getStoreConfigValue("freekassa.baseUrl", store));
		uriBuilder.queryParam("m", getStoreConfigValue("freekassa.merchantId", store))
				.queryParam("oa", payment.getAmount().setScale(2, RoundingMode.UNNECESSARY).toString())
				.queryParam("o", payment.getId());
		return uriBuilder.build().toString();
	}

	private String getStoreConfigValue(String key, Store store) {
		return storeConfigRepository.findByKeyAndStore(key, store).getValue();
	}
}
