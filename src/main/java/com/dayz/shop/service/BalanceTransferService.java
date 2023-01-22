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

	public void doTransfer(Payment incomingTransfer) {
		incomingTransfer.setStatus(OrderStatus.PENDING);
		incomingTransfer.setDirection(PaymentDirection.INCOMING);
		User userFrom = incomingTransfer.getUserFrom();
		Payment outgoingTransfer = buildOutgoingTransfer(incomingTransfer, userFrom);
		boolean storeAdmin = Utils.isStoreAdmin(userFrom);
		if (storeAdmin || (userFrom.getBalance().compareTo(incomingTransfer.getAmount()) >= 0)) {
			if (doesHaveRealCharges(userFrom, incomingTransfer.getStore())) {
				User userTo = incomingTransfer.getUser();
				BigDecimal balanceBefore = userTo.getBalance();
				incomingTransfer.setBalanceBefore(balanceBefore);
				BigDecimal balanceAfter = balanceBefore.add(incomingTransfer.getAmount());
				userTo.setBalance(balanceAfter);
				if (!storeAdmin) {
					userFrom.setBalance(userFrom.getBalance().subtract(incomingTransfer.getAmount()));
				}
				outgoingTransfer.setBalanceAfter(userFrom.getBalance());
				incomingTransfer.setStatus(OrderStatus.COMPLETE);
				outgoingTransfer.setStatus(OrderStatus.COMPLETE);
				incomingTransfer.getProperties().put("message", Utils.getMessage("transfer.success", incomingTransfer.getStore()));
				outgoingTransfer.getProperties().put("message", Utils.getMessage("transfer.success", incomingTransfer.getStore()));
				userRepository.save(userFrom);
				userRepository.save(userTo);
				incomingTransfer.setBalanceAfter(userTo.getBalance());
				paymentRepository.save(incomingTransfer);
				paymentRepository.save(outgoingTransfer);
			} else {
				saveFail(incomingTransfer, userFrom, "transfer.failed.noRealCharges");
				saveFail(outgoingTransfer, userFrom, "transfer.failed.noRealCharges");
			}
		} else {
			saveFail(incomingTransfer, userFrom, "transfer.failed.insufficient");
			saveFail(outgoingTransfer, userFrom, "transfer.failed.insufficient");
		}
	}

	private static Payment buildOutgoingTransfer(Payment incomingTransfer, User userFrom) {
		Payment outgoingTransfer = new Payment();
		outgoingTransfer.setCurrency(incomingTransfer.getCurrency());
		outgoingTransfer.setUserFrom(incomingTransfer.getUserFrom());
		outgoingTransfer.setUser(incomingTransfer.getUser());
		outgoingTransfer.setStatus(incomingTransfer.getStatus());
		outgoingTransfer.setChargeTime(incomingTransfer.getChargeTime());
		outgoingTransfer.setProperties(incomingTransfer.getProperties());
		outgoingTransfer.setAmount(outgoingTransfer.getAmount().negate());
		outgoingTransfer.setDirection(PaymentDirection.OUTGOING);
		outgoingTransfer.setBalanceBefore(userFrom.getBalance());
		return outgoingTransfer;
	}

	private void saveFail(Payment payment, User currentUser, String messageKey) {
		payment.setStatus(OrderStatus.FAILED);
		payment.getProperties().put("message", Utils.getMessage(messageKey, payment.getStore(), currentUser.getBalance(), payment.getAmount()));
		payment.setChargeTime(LocalDateTime.now());
		payment.setBalanceBefore(currentUser.getBalance());
		payment.setBalanceAfter(currentUser.getBalance());
		paymentRepository.save(payment);
	}

	private boolean doesHaveRealCharges(User currentUser, Store store) {
		boolean result = true;
		if (Utils.isStoreAdmin(currentUser) || Boolean.parseBoolean(Utils.getStoreConfig("checkRealCharges", store))) {
			List<Payment> payments = paymentRepository.findAllByUserAndStoreAndStatusAndTypeIn(currentUser, store, OrderStatus.COMPLETE, Collections.singletonList(Type.FREEKASSA));
			int threshold = Integer.parseInt(Utils.getStoreConfig("realChargesThreshold", store));
			result = threshold <= payments.size();
		}
		return result;
	}
}
