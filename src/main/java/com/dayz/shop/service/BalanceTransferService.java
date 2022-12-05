package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.OrderStatus;
import com.dayz.shop.jpa.entities.Payment;
import com.dayz.shop.jpa.entities.PaymentType;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.repository.StoreConfigRepository;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BalanceTransferService {
	private final StoreConfigRepository storeConfigRepository;
	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;

	@Autowired
	public BalanceTransferService(StoreConfigRepository storeConfigRepository,
								  PaymentRepository paymentRepository, UserRepository userRepository) {
		this.storeConfigRepository = storeConfigRepository;
		this.paymentRepository = paymentRepository;
		this.userRepository = userRepository;
	}

	public void doTransfer(Payment payment) {
		payment.setPaymentStatus(OrderStatus.PENDING);
		payment = paymentRepository.save(payment);
		User currentUser = Utils.getCurrentUser();
		if (Utils.isStoreAdmin(currentUser) || (currentUser.getBalance().compareTo(payment.getAmount()) > 0 && doesHaveRealCharges(currentUser))) {
			User paymentUser = payment.getUser();
			paymentUser.setBalance(paymentUser.getBalance().add(payment.getAmount()));
			currentUser.setBalance(currentUser.getBalance().subtract(payment.getAmount()));
			userRepository.save(currentUser);
			userRepository.save(paymentUser);
			payment.setPaymentStatus(OrderStatus.COMPLETE);
			paymentRepository.save(payment);
		} else {
			payment.setPaymentStatus(OrderStatus.FAILED);
			payment.getProperties().put("reason", "Не хватает средств на счету для совершения операции");
		}
	}

	private boolean doesHaveRealCharges(User currentUser) {
		boolean result = false;
		if (Boolean.parseBoolean(storeConfigRepository.findByKeyAndStore("checkRealCharges", currentUser.getStore()).getValue())) {
			List<Payment> payments = paymentRepository.findAllByUserAndPaymentTypeNotIn(currentUser, Collections.singletonList(PaymentType.TRANSFER));
			int threshold = Integer.parseInt(storeConfigRepository.findByKeyAndStore("realChargesThreshold", currentUser.getStore()).getValue());
			result = threshold <= payments.size();
		}
		return result;
	}
}
