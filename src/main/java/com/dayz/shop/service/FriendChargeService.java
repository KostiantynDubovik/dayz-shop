package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.OrderStatus;
import com.dayz.shop.jpa.entities.Payment;
import com.dayz.shop.jpa.entities.PaymentType;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.repository.StoreConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendChargeService {
	private final StoreConfigRepository storeConfigRepository;
	private final PaymentRepository paymentRepository;
	private final UserService userService;

	@Autowired
	public FriendChargeService(StoreConfigRepository storeConfigRepository, PaymentRepository paymentRepository, UserService userService) {
		this.storeConfigRepository = storeConfigRepository;
		this.paymentRepository = paymentRepository;
		this.userService = userService;
	}

	public String initPayment(Payment payment) {
		payment.setPaymentStatus(OrderStatus.PENDING);
		payment.setPaymentType(PaymentType.FRIEND);
		payment = paymentRepository.save(payment);
		return buildRedirectUrl(payment);
	}

	private String buildRedirectUrl(Payment payment) {
		return ""; //TODO
	}
}
