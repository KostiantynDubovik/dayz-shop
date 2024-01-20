package ua.oak.shop.repository;

import ua.oak.shop.jpa.entities.Order;
import ua.oak.shop.jpa.entities.OrderStatus;
import ua.oak.shop.jpa.entities.Store;
import ua.oak.shop.jpa.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findAllByUserId(Long userId);
	List<Order> findAllByUserAndStoreAndStatus(User user, Store store, OrderStatus status);
	List<Order> findAllByUserAndStoreAndStatus(User user, Store store, OrderStatus status, Pageable pageable);
}
