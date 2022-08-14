package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ORDER_ITEMS")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
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

	@Column(name = "M_CODE")
	@GeneratedValue(generator = "m-code-generator")
	@GenericGenerator(name = "m-code-generator",
			strategy = "com.dayz.shop.utils.MCodeGenerator")
	private String code;

	@ManyToOne
	@JoinColumn(name = "SERVER_ID")
	private Server server;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private OrderStatus status;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		OrderItem orderItem = (OrderItem) o;
		return id != null && Objects.equals(id, orderItem.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, item, order, user, boughtTime, received, receiveDateTime, price, code, server);
	}
}
