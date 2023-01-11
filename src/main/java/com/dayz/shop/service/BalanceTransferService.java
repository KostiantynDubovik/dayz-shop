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
		User userFrom = payment.getUserFrom();
		boolean storeAdmin = Utils.isStoreAdmin(userFrom);
		if (storeAdmin || (userFrom.getBalance().compareTo(payment.getAmount()) >= 0)) {
			if (doesHaveRealCharges(userFrom, payment.getStore())) {
				User paymentUser = payment.getUser();
				BigDecimal newBalance = paymentUser.getBalance().add(payment.getAmount());
				paymentUser.setBalance(newBalance.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newBalance);
				if (!storeAdmin) {
					userFrom.setBalance(userFrom.getBalance().subtract(payment.getAmount()));
				}
				payment.setStatus(OrderStatus.COMPLETE);
				payment.getProperties().put("message", Utils.getMessage("transfer.success", payment.getStore()));
				userRepository.save(userFrom);
				userRepository.save(paymentUser);
				paymentRepository.save(payment);
			} else {
				saveFail(payment, userFrom, "transfer.failed.noRealCharges");
			}
		} else {
			saveFail(payment, userFrom, "transfer.failed.insufficient");
		}
	}

	private void saveFail(Payment payment, User currentUser, String messageKey) {
		payment.setStatus(OrderStatus.FAILED);
		payment.getProperties().put("message", Utils.getMessage(messageKey, payment.getStore(), currentUser.getBalance(), payment.getAmount()));
		payment.setChargeTime(LocalDateTime.now());
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
