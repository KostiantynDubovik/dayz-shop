package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.OfferPriceRepository;
import com.dayz.shop.repository.OrderRepository;
import com.dayz.shop.repository.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class OrderUtils {

	private static OrderRepository orderRepository;
	private static OfferPriceRepository offerPriceRepository;
	private static UserRepository userRepository;

	@Autowired
	public OrderUtils(OrderRepository orderRepository, OfferPriceRepository offerPriceRepository, UserRepository userRepository) {
		OrderUtils.orderRepository = orderRepository;
		OrderUtils.offerPriceRepository = offerPriceRepository;
		OrderUtils.userRepository = userRepository;
	}

	public static Order getCurrentOrder(Store store) {
		return getCurrentOrder(Utils.getCurrentUser(), store, null);
	}

	public static Order getCurrentOrder(User user, Store store, User userTo) {
		List<Order> orders = orderRepository.findAllByUserAndStoreAndStatus(user, store, OrderStatus.PENDING);
		if (CollectionUtils.isEmpty(orders)) {
			orders = new ArrayList<>();
			Order order = createOrder(user, store, userTo);
			orders.add(orderRepository.save(order));
		}
		return orders.stream().findFirst().orElse(new Order());
	}

	public static List<OrderItem> getItemsByType(Order order, ItemType itemType) {
		return order.getOrderItems().stream().filter(orderItem -> orderItem.getItem().getItemType().equals(itemType)).collect(Collectors.toList());
	}

	public static Order createOrder(User user, Store store, User userTo) {
		Order order = new Order();
		order.setOrderItems(new ArrayList<>());
		order.setStore(store);
		order.setUser(user);
		order.setUserTo(userTo);
		order.setStatus(OrderStatus.PENDING);
		order.setOrderTotal(BigDecimal.ZERO);
		return order;
	}

	public static List<OrderItem> toOrderItems(Map<Item, Integer> items, User user, Order order){
		return toOrderItems(new ArrayList<>(), items, user, order);
	}

	public static List<OrderItem> toOrderItems(List<OrderItem> orderItems, Map<Item, Integer> items, User user, Order order) {
		if (orderItems == null) {
			orderItems = new ArrayList<>();
		}
		for (Map.Entry<Item, Integer> item : items.entrySet()) {
			orderItems.add(createOrderItem(item.getKey(), user, user, order, item.getValue()));
		}
		return orderItems;
	}

	public static OrderItem createOrderItem(Item item, User user, User userTo, Order order, int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setUser(user);
		orderItem.setUserTo(userTo);
		orderItem.setOrder(order);
		orderItem.setServer(order.getServer());
		orderItem.setStore(order.getStore());
		orderItem.setStatus(OrderStatus.PENDING);
		orderItem.setReceived(false);
		orderItem.setCoordinates(StringUtils.EMPTY);
		orderItem.setBoughtTime(LocalDateTime.now());
		orderItem.setItem(item);
		orderItem.setPrice(getCurrentPrice(order.getStore(), item));
		orderItem.setCount(Math.max(count, 1));
		return orderItem;
	}

	private static BigDecimal getCurrentPrice(Store store, Item item) {
		BigDecimal result;
		List<OfferPrice> offerPrices = offerPriceRepository.findAllActiveOfferPrices(store, item);
		if(CollectionUtils.isNotEmpty(offerPrices)) {
			OfferPrice offerPrice = offerPrices.stream().findFirst().get();
			result = getOfferPrice(offerPrice);
		} else {
			result = item.getListPrice().getPrice();
		}
		return result.setScale(0, RoundingMode.UP);
	}

	public static BigDecimal getOfferPrice(OfferPrice offerPrice) {
		BigDecimal result;
		switch (offerPrice.getOfferPriceType()) {
			case PERCENTAGE:
				BigDecimal hundred = BigDecimal.valueOf(100);
				result = offerPrice.getItem().getListPrice().getPrice().multiply(hundred.subtract(offerPrice.getPrice())).divide(hundred, 0, RoundingMode.UP);
				break;
			case ABSOLUTE:
			default:
				result = offerPrice.getPrice();
		}
		return result.setScale(0, RoundingMode.UP);
	}

	public static void recalculateOrder(Order order) {
		List<OrderItem> orderItems = order.getOrderItems();
		BigDecimal total = BigDecimal.ZERO;
		for (OrderItem orderItem : orderItems) {
			recalculateOrderItem(orderItem);
			total = total.add(orderItem.getTotalPrice());
		}
		order.setOrderTotal(total);
	}

	public static void recalculateOrderItem(OrderItem orderItem) {
		orderItem.setTotalPrice(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getCount())));
	}
}
