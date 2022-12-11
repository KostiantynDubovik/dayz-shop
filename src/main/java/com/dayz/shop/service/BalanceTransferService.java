package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.repository.StoreConfigRepository;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
		payment.setStatus(OrderStatus.PENDING);
		User currentUser = Utils.getCurrentUser();
		if (Utils.isStoreAdmin(currentUser) || (currentUser.getBalance().compareTo(payment.getAmount()) > 0 && doesHaveRealCharges(currentUser, payment.getStore()))) {
			User paymentUser = payment.getUser();
			paymentUser.setBalance(paymentUser.getBalance().add(payment.getAmount()));
			if (!Utils.isStoreAdmin(currentUser)) {
				currentUser.setBalance(currentUser.getBalance().subtract(payment.getAmount()));
				userRepository.save(currentUser);
			}
			userRepository.save(paymentUser);
			payment.setStatus(OrderStatus.COMPLETE);
			payment.setChargeTime(LocalDateTime.now());
			paymentRepository.save(payment);
		} else {
			payment.setStatus(OrderStatus.FAILED);
			payment.getProperties().put("reason", "Не хватает средств на счету для совершения операции");
		}
	}

	private boolean doesHaveRealCharges(User currentUser, Store store) {
		boolean result = true;
		if (Utils.isStoreAdmin(currentUser) || Boolean.parseBoolean(storeConfigRepository.findByKeyAndStore("checkRealCharges", store).getValue())) {
			List<Payment> payments = paymentRepository.findAllByUserAndTypeNotIn(currentUser, Collections.singletonList(Type.TRANSFER));
			int threshold = Integer.parseInt(storeConfigRepository.findByKeyAndStore("realChargesThreshold", store).getValue());
			result = threshold <= payments.size();
		}
		return result;
	}
}
