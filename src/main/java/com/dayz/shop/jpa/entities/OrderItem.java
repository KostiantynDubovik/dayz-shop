package com.dayz.shop.jpa.entities;

import com.dayz.shop.listeners.OrderItemListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "order_items")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@EntityListeners(OrderItemListener.class)
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ORDER_ITEM_ID", nullable = false)
	@JsonIgnore
	private Long id;

	@OneToOne
	@JoinColumn(name = "ITEM_ID")
	private Item item;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ORDER_ID")
	@JsonIgnore
	private Order order;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	@JsonIgnore
	private User user;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "STORE_ID", referencedColumnName = "STORE_ID")
	@JsonIgnore
	private Store store;

	@Column(name = "BOUGHT_TIME")
	private LocalDateTime boughtTime;

	@Column(name = "RECEIVED")
	@JsonIgnore
	private boolean received;

	@Column(name = "RECEIVE_TIME")
	private LocalDateTime receiveDateTime;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "TOTAL_PRICE")
	private BigDecimal totalPrice;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "SERVER_ID")
	private Server server;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	@JsonIgnore
	private OrderStatus status;

	@Column(name = "COUNT")
	private Integer count = 1;

	@Column(name = "COORDINATES")
	private String coordinates;
}
