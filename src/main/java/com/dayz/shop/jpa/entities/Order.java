package com.dayz.shop.jpa.entities;

import com.dayz.shop.listeners.OrderListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.aspectj.lang.annotation.Aspect;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Aspect
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "orders")
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

	@Transient
	private Type type = Type.ORDER;

	@ElementCollection
	@CollectionTable(name = "order_properties",
			joinColumns = {@JoinColumn(name = "ORDER_ID", referencedColumnName = "ORDER_ID")})
	@MapKeyColumn(name = "NAME")
	@Column(name = "VALUE")
	private Map<String, String> properties = new HashMap<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return Objects.equals(id, order.id) && Objects.equals(orderItems, order.orderItems) && Objects.equals(orderTotal, order.orderTotal) && Objects.equals(user, order.user) && Objects.equals(store, order.store) && Objects.equals(server, order.server) && status == order.status && type == order.type && Objects.equals(properties, order.properties);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderItems, orderTotal, user, store, server, status, type, properties);
	}
}
