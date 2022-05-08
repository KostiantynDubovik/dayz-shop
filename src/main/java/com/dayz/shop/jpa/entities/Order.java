package com.dayz.shop.jpa.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "ORDERS")
public class Order {
	@Id
	@Column(name = "ORDER_ID", nullable = false)
	private Long id;

	@OneToMany(mappedBy = "order")
	private List<OrderItem> orderItems;

	@Column(name = "ORDER_TOTAL")
	private BigDecimal orderTotal;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private OrderStatus status;
}
