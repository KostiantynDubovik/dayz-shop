package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "store", "user"})
@Table(name = "PAYMENTS")
public class Payment {

	@Id
	@Column(name = "PAYMENT_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "AMOUNT")
	private BigDecimal amount;

	@Column(name = "CHARGE_TIME")
	private LocalDateTime chargeTime;

	@JoinColumn(name = "USER_ID")
	@ManyToOne
	private User user;

	@JoinColumn(name = "STORE_ID")
	@ManyToOne
	private Store store;

	@Enumerated(EnumType.STRING)
	@Column(name = "PAYMENT_TYPE")
	private PaymentType paymentType;

	@Enumerated(EnumType.STRING)
	@Column(name = "PAYMENT_STATUS")
	private OrderStatus paymentStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "CURRENCY")
	private Currency currency;

	@ElementCollection
	@CollectionTable(name = "PAYMENT_PROPERTIES",
			joinColumns = {@JoinColumn(name = "PAYMENT_ID", referencedColumnName = "PAYMENT_ID")})
	@MapKeyColumn(name = "name")
	@Column(name = "VALUE")
	private Map<String, String> properties = new HashMap<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Payment payment = (Payment) o;
		return id != null && Objects.equals(id, payment.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
