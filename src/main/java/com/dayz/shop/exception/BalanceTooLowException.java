package com.dayz.shop.exception;

import java.math.BigDecimal;

public class BalanceTooLowException extends Exception{
	private final BigDecimal orderTotal;
	private final BigDecimal userBalance;

	public BalanceTooLowException(BigDecimal userBalance, BigDecimal orderTotal) {
		this.orderTotal = orderTotal;
		this.userBalance = userBalance;
	}

	public BalanceTooLowException(BigDecimal userBalance, BigDecimal orderTotal, String message) {
		super(message);
		this.orderTotal = orderTotal;
		this.userBalance = userBalance;
	}

	public BalanceTooLowException(BigDecimal userBalance, BigDecimal orderTotal, String message, Throwable cause) {
		super(message, cause);
		this.orderTotal = orderTotal;
		this.userBalance = userBalance;
	}

	public BalanceTooLowException(BigDecimal userBalance, BigDecimal orderTotal, Throwable cause) {
		super(cause);
		this.orderTotal = orderTotal;
		this.userBalance = userBalance;
	}

	public BalanceTooLowException(BigDecimal userBalance, BigDecimal orderTotal, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.orderTotal = orderTotal;
		this.userBalance = userBalance;
	}
}
