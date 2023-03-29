package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.FundTransfer;
import com.dayz.shop.jpa.entities.OrderStatus;
import com.dayz.shop.jpa.entities.Payment;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.FundTransferRepository;
import com.dayz.shop.utils.Utils;
import org.apache.wink.client.RestClient;
import org.apache.wink.json4j.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class FundTransferService {
	private final FundTransferRepository fundTransferRepository;

	@Autowired
	public FundTransferService(FundTransferRepository fundTransferRepository) {
		this.fundTransferRepository = fundTransferRepository;
	}

	public void transfer(Payment payment) throws NoSuchAlgorithmException {
//		buildFundTransfer(payment);
		buildFundWithdraw(payment);
	}

	private void buildFundTransfer(Payment payment) {
		FundTransfer fundTransfer = new FundTransfer();
		fundTransfer.setCurrency(payment.getCurrency());
		fundTransfer.setTransferTime(LocalDateTime.now());
		fundTransfer.setInitialAmount(payment.getAmount());
		Store store = payment.getStore();
		BigDecimal percentage = new BigDecimal(Utils.getStoreConfig("freekassa.comission", store));
		fundTransfer.setPercentage(percentage);
		String walletTo = Utils.getStoreConfig("freekassa.wallet.id", store);
		fundTransfer.setWalletTo(walletTo);
		BigDecimal fee = fundTransfer.getInitialAmount().multiply(percentage).setScale( 0, RoundingMode.DOWN);
		BigDecimal amount = fundTransfer.getInitialAmount().subtract(fee);
		fundTransfer.setAmount(amount);
		fundTransfer.setStatus(OrderStatus.PENDING);
		fundTransfer.setStoreFrom(store);
		fundTransferRepository.save(fundTransfer);
	}

	@SuppressWarnings("unchecked")
	private void buildFundWithdraw(Payment payment) throws NoSuchAlgorithmException {
		FundTransfer fundWithdraw = new FundTransfer();
		fundWithdraw.setCurrency(payment.getCurrency());
		fundWithdraw.setTransferTime(LocalDateTime.now());
		fundWithdraw.setInitialAmount(BigDecimal.ONE);
		Store store = payment.getStore();
		fundWithdraw.setPercentage(new BigDecimal(System.currentTimeMillis()));
		String walletTo = Utils.getStoreConfig("freekassa.own.wallet.id", store);
		fundWithdraw.setWalletTo(walletTo);
		fundWithdraw.setAmount(payment.getAmount());
		fundWithdraw.setStatus(OrderStatus.PENDING);
		fundWithdraw.setStoreFrom(store);
		fundTransferRepository.save(fundWithdraw);
		Map<String, String> requestObject = new HashMap<>();
		putAsString("nonce", fundWithdraw.getPercentage().setScale(0, RoundingMode.UNNECESSARY), requestObject);
		putAsString("shopId", Utils.getStoreConfig("freekassa.merchantId", fundWithdraw.getStoreFrom()), requestObject);
		putAsString("paymentId", fundWithdraw.getId(), requestObject);
		putAsString("i", fundWithdraw.getInitialAmount().setScale(0, RoundingMode.UNNECESSARY), requestObject);
		putAsString("account", fundWithdraw.getWalletTo(), requestObject);
		putAsString("amount", fundWithdraw.getAmount(), requestObject);
		putAsString("currency", fundWithdraw.getCurrency(), requestObject);
		putAsString("signature", Utils.getFreekassaSignaturesSHA256(requestObject, store), requestObject);
		Map<String, String> response = new RestClient().resource("https://api.freekassa.ru/v1/withdrawals/create").contentType(MediaType.APPLICATION_JSON_TYPE).post(Map.class, new JSONObject(requestObject).toString());
		System.out.println(response);
		fundWithdraw.setStatus(OrderStatus.COMPLETE);
		fundTransferRepository.save(fundWithdraw);
	}

	private void putAsString(String key, Object value, Map<String, String> map) {
		if (value != null) {
			map.put(key, String.valueOf(value));
		}
	}
}
