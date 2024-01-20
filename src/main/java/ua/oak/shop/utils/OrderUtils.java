package ua.oak.shop.utils;

import com.dayz.shop.jpa.entities.*;
import ua.oak.shop.jpa.entities.*;
import ua.oak.shop.repository.OrderRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Aspect
@Component
public class OrderUtils {

	private static OrderRepository orderRepository;

	@Autowired
	public OrderUtils(OrderRepository orderRepository) {
		OrderUtils.orderRepository = orderRepository;
	}

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
		List<Order> orders = orderRepository.findAllByUserAndStoreAndStatus(user, store, OrderStatus.PENDING);
		if (CollectionUtils.isEmpty(orders)) {
			orders = new ArrayList<>();
			user.setOrders(orders);
			Order order = createOrder(user, store);
			orders.add(order);
			orderRepository.save(order);
		}
		return orders.stream().findFirst().orElse(new Order());
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
		orderItem.setServer(order.getServer());
		orderItem.setStatus(OrderStatus.PENDING);
		orderItem.setReceived(false);
		orderItem.setBoughtTime(LocalDateTime.now());
		orderItem.setItem(item);
		List<OfferPrice> offerPrices = item.getOfferPrices();
		orderItem.setPrice(CollectionUtils.isEmpty(offerPrices) ? item.getListPrice().getPrice() : getCurrentOfferPrice(offerPrices).getPrice());
		return orderItem;
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

	public static Order getOrder(String orderId) {
		return getOrder(Long.valueOf(orderId));
	}

	public static Order getOrder(Long orderId) {
		return orderRepository.getById(orderId);
	}
}
