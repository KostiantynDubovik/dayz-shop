package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.*;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderUtils {

	public static OfferPrice getCurrentOfferPrice(List<OfferPrice> offerPrices) {
		OfferPrice result = new OfferPrice();
		offerPrices.sort(Comparator.comparingInt(OfferPrice::getPriority));
		for (OfferPrice offerPrice : offerPrices) {
			LocalDateTime now = LocalDateTime.now();
			if (now.isBefore(offerPrice.getEndTime()) && now.isAfter(offerPrice.getStartTime())) {
				result = offerPrice;
				break;
			}
		}
		return result;
	}

	public static Order getCurrentOrder(Store store) {
		return getCurrentOrder(Utils.getCurrentUser(), store);
	}

	public static Order getCurrentOrder(User user, Store store) {
		List<Order> orders = user.getOrders();
		if (CollectionUtils.isEmpty(orders)) {
			orders = new ArrayList<>();
			user.setOrders(orders);
			Order order = createOrder(user, store);
			orders.add(order);
		}
		return orders.stream().filter(order -> order.getStatus().equals(OrderStatus.PENDING)).findFirst().get();
	}

	public static Order createOrder(User user, Store store) {
		Order order = new Order();
		order.setOrderItems(new ArrayList<>());
		order.setStore(store);
		order.setUser(user);
		order.setStatus(OrderStatus.PENDING);
		order.setOrderTotal(BigDecimal.ZERO);
		return order;
	}

	public static OrderItem createOrderItem(Item item, User user, Order order) {
		OrderItem orderItem = new OrderItem();
		orderItem.setUser(user);
		orderItem.setOrder(order);
		orderItem.setReceived(false);
		orderItem.setBoughtTime(LocalDateTime.now());
		orderItem.setItem(item);
		List<OfferPrice> offerPrices = item.getOfferPrices();
		orderItem.setPrice(CollectionUtils.isEmpty(offerPrices) ? item.getListPrice() : getCurrentOfferPrice(offerPrices).getPrice());
		return orderItem;
	}
}
