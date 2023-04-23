package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.FundTransfer;
import com.dayz.shop.jpa.entities.OrderStatus;
import com.dayz.shop.jpa.entities.Payment;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.FundTransferRepository;
import com.dayz.shop.utils.Utils;
import okhttp3.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.wink.client.ClientWebException;
import org.apache.wink.client.RestClient;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FundTransferService {
	public static final String CLASSNAME = FundTransferService.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

	private final FundTransferRepository fundTransferRepository;

	@Autowired
	public FundTransferService(FundTransferRepository fundTransferRepository) {
		this.fundTransferRepository = fundTransferRepository;
	}

	public void transfer(Payment payment) throws NoSuchAlgorithmException, InvalidKeyException, JSONException {
		buildFundWithdraw(payment);
		buildFundTransfer(payment);
	}

	@Scheduled(cron = "0 */5 * * * *")
	public void transferFunds() throws NoSuchAlgorithmException, InvalidKeyException {
		List<FundTransfer> pendingWithdraws = fundTransferRepository.getAllByStatusAndPercentage(OrderStatus.PENDING, BigDecimal.ZERO);
		for (FundTransfer pendingWithdraw : pendingWithdraws) {
			doWithdraw(pendingWithdraw);
		}
		List<FundTransfer> pendingTransfers = fundTransferRepository.getAllByStatusAndPercentageGreaterThan(OrderStatus.PENDING, BigDecimal.ZERO);
		for (FundTransfer pendingTransfer : pendingTransfers) {
			doTransfer(pendingTransfer);
		}
	}

	private void doWithdraw(FundTransfer fundWithdraw) throws NoSuchAlgorithmException, InvalidKeyException {
		String methodName = "doWithdraw";
		try {
			Map<String, String> requestObject = buildWithdrawRequest(fundWithdraw);
			String response = new RestClient().resource(fundWithdraw.getStoreFrom().getString("freekassa.withdraw.api.url")).contentType(MediaType.APPLICATION_JSON_TYPE).post(String.class, new JSONObject(requestObject).toString());
			processResponse(fundWithdraw, response);
		} catch (ClientWebException e) {
			processWebException(fundWithdraw, e);
			LOGGER.logp(Level.SEVERE, CLASSNAME, methodName, "Fund withdraw failed with following response: ", fundWithdraw.getProperties());
		}
	}

	private Map<String, String> buildWithdrawRequest(FundTransfer fundWithdraw) throws NoSuchAlgorithmException, InvalidKeyException {
		Map<String, String> requestObject = new HashMap<>();
		putAsString("nonce", System.currentTimeMillis(), requestObject);
		putAsString("shopId", fundWithdraw.getStoreFrom().getString("freekassa.merchantId"), requestObject);
		putAsString("paymentId", fundWithdraw.getId(), requestObject);
		putAsString("i", fundWithdraw.getInitialAmount().setScale(0, RoundingMode.UNNECESSARY), requestObject);
		putAsString("account", fundWithdraw.getWalletTo(), requestObject);
		putAsString("amount", fundWithdraw.getAmount().setScale(2, RoundingMode.UNNECESSARY), requestObject);
		putAsString("currency", fundWithdraw.getCurrency(), requestObject);
		putAsString("signature", Utils.getFreekassaSignatureForWithdraw(requestObject, fundWithdraw.getStoreFrom()), requestObject);
		return requestObject;
	}

	private void buildFundWithdraw(Payment payment) {
		FundTransfer fundWithdraw = new FundTransfer();
		fundWithdraw.setCurrency(payment.getCurrency());
		fundWithdraw.setTransferTime(LocalDateTime.now());
		fundWithdraw.setInitialAmount(BigDecimal.ONE);
		fundWithdraw.setPayment(payment);
		Store store = payment.getStore();
		fundWithdraw.setPercentage(BigDecimal.ZERO);
		String walletTo = store.getString("freekassa.wallet.id");
		fundWithdraw.setWalletTo(walletTo);
		BigDecimal amount = payment.getAmount();
		String commission = payment.getProperties().get("commission");
		if (NumberUtils.isParsable(commission)) {
			amount = amount.subtract(new BigDecimal(commission));
		}
		fundWithdraw.setAmount(amount);
		fundWithdraw.setStatus(OrderStatus.PENDING);
		fundWithdraw.setStoreFrom(store);
		fundTransferRepository.save(fundWithdraw);
	}

	private void doTransfer(FundTransfer fundTransfer) {
		Map<String, String> requestObject = buildTransferRequest(fundTransfer);
		String fkWalletApi = fundTransfer.getStoreFrom().getString("freekassa.wallet.api.url");
		String response = sendMultipart(fkWalletApi, requestObject);
		processResponse(fundTransfer, response);
	}

	private String sendMultipart(String url, Map<String, String> parameters) {
		OkHttpClient httpClient = new OkHttpClient();
		FormBody.Builder builder = new FormBody.Builder();
		for (String s : parameters.keySet()) {
			builder.add(s, parameters.get(s));
		}
		RequestBody formBody = builder.build();

		Request request = new Request.Builder()
				.url(url)
				.addHeader("User-Agent", "OkHttp Bot")
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
				.post(formBody)
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			return response.body().string();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, String> buildTransferRequest(FundTransfer fundTransfer) {
		Map<String, String> requestObject = new LinkedHashMap<>();
		putAsString("wallet_id", fundTransfer.getStoreFrom().getString("freekassa.wallet.id"), requestObject);
		putAsString("purse", fundTransfer.getWalletTo(), requestObject);
		putAsString("amount", fundTransfer.getAmount().setScale(2, RoundingMode.UNNECESSARY), requestObject);
		putAsString("sign", Utils.getFreekassaSignatureForTransfer(fundTransfer), requestObject);
		putAsString("action", "transfer", requestObject);
		return requestObject;
	}

	private void buildFundTransfer(Payment payment) {
		FundTransfer fundTransfer = new FundTransfer();
		fundTransfer.setCurrency(payment.getCurrency());
		fundTransfer.setPayment(payment);
		fundTransfer.setTransferTime(LocalDateTime.now());
		BigDecimal paymentAmount = payment.getAmount();
		String commission = payment.getProperties().get("commission");
		if (NumberUtils.isParsable(commission)) {
			paymentAmount = paymentAmount.subtract(new BigDecimal(commission));
		}
		fundTransfer.setInitialAmount(paymentAmount);
		Store store = payment.getStore();
		BigDecimal percentage = new BigDecimal(store.getString("freekassa.comission"));
		fundTransfer.setPercentage(percentage);
		String purse = store.getString("freekassa.purse");
		fundTransfer.setWalletTo(purse);
		BigDecimal fee = fundTransfer.getInitialAmount().multiply(percentage).setScale(0, RoundingMode.DOWN);
		BigDecimal amount = fundTransfer.getInitialAmount().subtract(fee);
		fundTransfer.setAmount(amount);
		fundTransfer.setStatus(OrderStatus.PENDING);
		fundTransfer.setStoreFrom(store);
		fundTransferRepository.save(fundTransfer);
	}

	@SuppressWarnings("unchecked")
	private void processWebException(FundTransfer fundWithdraw, ClientWebException e) {
		fundWithdraw.setStatus(OrderStatus.FAILED);
		String entity = e.getResponse().getEntity(String.class);
		Map<String, String> properties;
		try {
			properties = new JSONObject(entity);
		} catch (JSONException je) {
			properties = new HashMap<>();
			properties.put("response", entity);
		}
		fundWithdraw.setProperties(properties);
		fundWithdraw.setStatus(OrderStatus.FAILED);
		fundTransferRepository.save(fundWithdraw);
	}

	@SuppressWarnings("unchecked")
	private void processResponse(FundTransfer fundTransfer, String response) {
		Map<String, String> properties;
		try {
			properties = new JSONObject(response);
			properties.remove("data");
		} catch (JSONException je) {
			properties = new HashMap<>();
			properties.put("response", response);
		}
		fundTransfer.setProperties(properties);
		fundTransfer.setStatus(!"error".equals(properties.get("status")) ? OrderStatus.COMPLETE : OrderStatus.FAILED);
		fundTransferRepository.save(fundTransfer);
	}

	private void putAsString(String key, Object value, Map<String, String> map) {
		if (value != null) {
			map.put(key, String.valueOf(value));
		}
	}
}
