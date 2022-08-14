package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	List<OrderItem> findAllByOrderId(Long orderId);

	OrderItem findFirstByItemAndOrder(Item item, Order order);

	@Query("select OrderItem from OrderItem where user in (select User from User where steamId = ?1) and received = ?2 and status = ?3")
	List<OrderItem> findAllByUserSteamIdAndReceivedAndStatus(String userSteamId, boolean received, OrderStatus status);

	List<OrderItem> findAllByMCode(List<String> mCode);
}
