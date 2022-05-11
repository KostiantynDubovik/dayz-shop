package com.dayz.shop.jpa.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "ORDER_ITEMS")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ORDER_ITEM_ID", nullable = false)
	private Long id;

	@OneToOne
	@JoinColumn(name = "ITEM_ID")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private User user;

	@Column(name = "BOUGHT_TIME")
	private LocalDateTime boughtTime;

	@Column(name = "RECEIVED")
	private boolean received;

	@Column(name = "RECEIVE_TIME")
	private LocalDateTime receiveDateTime;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "QUANTITY")
	private Integer quantity;
}
