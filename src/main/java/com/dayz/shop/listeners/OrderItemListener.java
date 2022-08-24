package com.dayz.shop.listeners;

import com.dayz.shop.jpa.entities.OrderItem;
import com.dayz.shop.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Service
public class OrderItemListener {
	private static OrderUtils orderUtils;

	@Autowired
	private void init(OrderUtils orderUtilsParam) {
		orderUtils = orderUtilsParam;
	}

	@PrePersist
	@PreUpdate
	protected void recalculate(OrderItem orderItem) {
		orderUtils.recalculateOrderItem(orderItem);
	}
}
