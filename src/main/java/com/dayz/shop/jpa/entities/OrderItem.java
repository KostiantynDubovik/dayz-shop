package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "ORDER_ITEMS")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ORDER_ITEM_ID", nullable = false)
	private Long id;

	@OneToOne
	@JoinColumn(name = "ITEM_ID")
	private Item item;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ORDER_ID")
	@JsonBackReference
	private Order order;

	@ManyToOne(cascade = CascadeType.MERGE)
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

	@Column(name = "M_CODE")
	private String code;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "SERVER_ID")
	private Server server;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private OrderStatus status;

	@Column(name = "COUNT")
	private int count;

}
