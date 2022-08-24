package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.OrderRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Aspect
@Service
public class OrderUtils {

	final private OrderRepository orderRepository;

	@Autowired
	public OrderUtils(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public OfferPrice getCurrentOfferPrice(List<OfferPrice> offerPrices) {
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

	public Order getCurrentOrder(Store store) {
		return getCurrentOrder(Utils.getCurrentUser(), store);
	}

	public Order getCurrentOrder(User user, Store store) {
		List<Order> orders = orderRepository.findAllByUserAndStoreAndStatus(user, store, OrderStatus.PENDING);
		if (CollectionUtils.isEmpty(orders)) {
			orders = new ArrayList<>();
			user.setOrders(orders);
			Order order = createOrder(user, store);
			orders.add(order);
			orderRepository.save(order);
		}
		return orders.stream().findFirst().get();
	}

	public Order createOrder(User user, Store store) {
		Order order = new Order();
		order.setOrderItems(new ArrayList<>());
		order.setStore(store);
		order.setUser(user);
		order.setStatus(OrderStatus.PENDING);
		order.setOrderTotal(BigDecimal.ZERO);
		return order;
	}

	public OrderItem createOrderItem(Item item, User user, Order order) {
		OrderItem orderItem = new OrderItem();
		orderItem.setUser(user);
		orderItem.setOrder(order);
		orderItem.setCode(MCodeGenerator.generateMCode());
		orderItem.setServer(order.getServer());
		orderItem.setStatus(OrderStatus.PENDING);
		orderItem.setReceived(false);
		orderItem.setBoughtTime(LocalDateTime.now());
		orderItem.setItem(item);
		List<OfferPrice> offerPrices = item.getOfferPrices();
		orderItem.setPrice(CollectionUtils.isEmpty(offerPrices) ? item.getListPrice() : getCurrentOfferPrice(offerPrices).getPrice());
		order.getOrderItems().add(orderItem);
		return orderItem;
	}

	public void recalculateOrder(Order order) {
		List<OrderItem> orderItems = order.getOrderItems();
		BigDecimal total = BigDecimal.ZERO;
		for (OrderItem orderItem : orderItems) {
			recalculateOrderItem(orderItem);
			total = total.add(orderItem.getTotalPrice());
		}
		order.setOrderTotal(total);
	}

	public void recalculateOrderItem(OrderItem orderItem) {
		orderItem.setTotalPrice(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getCount())));
	}
}
