package com.dayz.shop.jpa.entities;

import com.dayz.shop.listeners.OrderListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Aspect
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ORDERS")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@EntityListeners(OrderListener.class)
public class Order {
	@Id
	@Column(name = "ORDER_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@OneToMany(mappedBy = "order")
	@ToString.Exclude
	private List<OrderItem> orderItems = new ArrayList<>();

	@Column(name = "ORDER_TOTAL")
	private BigDecimal orderTotal;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "orders_users_STORE_ID_fk"))
	private User user;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "SERVER_ID")
	private Server server;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private OrderStatus status;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Order order = (Order) o;
		return id != null && Objects.equals(id, order.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderItems, orderTotal, user, store, server, status);
	}
}
