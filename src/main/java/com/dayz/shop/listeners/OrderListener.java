package com.dayz.shop.listeners;

import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.jpa.entities.OrderItem;
import com.dayz.shop.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class OrderListener {
	private static OrderUtils orderUtils;

	@Autowired
	public void init(OrderUtils orderUtilsParam) {
		orderUtils = orderUtilsParam;
	}

	@PrePersist
	@PreUpdate
	protected void recalculate(Order orderItem) {
		orderUtils.recalculateOrder(orderItem);
	}
}
