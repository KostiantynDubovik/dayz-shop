package ua.com.oak.shop.repository;

import com.dayz.shop.jpa.entities.*;
import com.dubochok.shop.jpa.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.oak.shop.jpa.entities.*;
import ua.oak.shop.jpa.entities.*;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	List<OrderItem> findAllByOrderId(Long orderId);

	OrderItem findFirstByItemAndOrder(Item item, Order order);

	List<OrderItem> findAllByUserAndReceivedAndStatus(User user, boolean received, OrderStatus status);

	List<OrderItem> findAllByIdIn(Iterable<Long> codes);
}
