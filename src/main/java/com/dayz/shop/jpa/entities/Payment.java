package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
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
	@MapKeyColumn(name="name")
	@Column(name="VALUE")
	private Map<String, String> properties = new HashMap<>();
}
