package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.jpa.entities.OrderStatus;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findAllByUserId(Long userId);
	List<Order> findAllByUserAndStoreAndStatus(User user, Store store, OrderStatus status);
	Page<Order> findAllByUserAndStoreAndStatus(User user, Store store, OrderStatus status, Pageable pageable);
}
