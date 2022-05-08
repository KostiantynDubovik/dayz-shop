package com.dayz.shop.jpa.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "OFFER_PRICE")
public class OfferPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "OFFER_ID", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ITEM_ID")
	private Item item;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "START_TIME")
	private LocalDateTime startTime;

	@Column(name = "END_TIME")
	private LocalDateTime endTime;

	@Column(name = "PRIORITY")
	private int priority;
}
