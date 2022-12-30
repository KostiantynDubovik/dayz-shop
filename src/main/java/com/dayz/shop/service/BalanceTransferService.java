package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.PaymentRepository;
import com.dayz.shop.repository.UserRepository;
import com.dayz.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class BalanceTransferService {
	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;

	@Autowired
	public BalanceTransferService(PaymentRepository paymentRepository, UserRepository userRepository) {
		this.paymentRepository = paymentRepository;
		this.userRepository = userRepository;
	}

	public void doTransfer(Payment payment) {
		payment.setStatus(OrderStatus.PENDING);
		User currentUser = Utils.getCurrentUser();
		boolean storeAdmin = Utils.isStoreAdmin(currentUser);
		if (storeAdmin || (currentUser.getBalance().compareTo(payment.getAmount()) > 0 && doesHaveRealCharges(currentUser, payment.getStore()))) {
			User paymentUser = payment.getUser();
			BigDecimal newBalance = paymentUser.getBalance().add(payment.getAmount());
			paymentUser.setBalance(newBalance.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newBalance);
			if (!storeAdmin) {
				currentUser.setBalance(currentUser.getBalance().subtract(payment.getAmount()));
				userRepository.save(currentUser);
			}
			userRepository.save(paymentUser);
			payment.setStatus(OrderStatus.COMPLETE);
			payment.setChargeTime(LocalDateTime.now());
			payment.getProperties().put("message", Utils.getMessage("transfer.success", payment.getStore()));
			paymentRepository.save(payment);
		} else {
			saveFail(payment, currentUser);
		}
	}

	private void saveFail(Payment payment, User currentUser) {
		payment.setStatus(OrderStatus.FAILED);
		payment.getProperties().put("message", Utils.getMessage("transfer.failed.insufficient", payment.getStore(), currentUser.getBalance(), payment.getAmount()));
		paymentRepository.save(payment);
	}

	private boolean doesHaveRealCharges(User currentUser, Store store) {
		boolean result = true;
		if (Utils.isStoreAdmin(currentUser) || Boolean.parseBoolean(Utils.getStoreConfig("checkRealCharges", store))) {
			List<Payment> payments = paymentRepository.findAllByUserAndStoreAndTypeNotIn(currentUser, store, Collections.singletonList(Type.TRANSFER));
			int threshold = Integer.parseInt(Utils.getStoreConfig("realChargesThreshold", store));
			result = threshold <= payments.size();
		}
		return result;
	}
}
