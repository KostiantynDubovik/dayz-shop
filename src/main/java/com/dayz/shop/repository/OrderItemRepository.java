package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	List<OrderItem> findAllByOrderId(Long orderId);

	OrderItem findFirstByItemAndOrder(Item item, Order order);

	List<OrderItem> findAllByUserAndReceivedAndStatus(User user, boolean received, OrderStatus status);

	List<OrderItem> findAllByCodeIn(Iterable<String> codes);
}
