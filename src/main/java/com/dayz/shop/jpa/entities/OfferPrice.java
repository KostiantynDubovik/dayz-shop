package com.dayz.shop.jpa.entities;

import com.dayz.shop.utils.OrderUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "offer_price")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class OfferPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "OFFER_ID", nullable = false)
	private Long id;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ITEM_ID")
	@JsonBackReference
	private Item item;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "START_TIME")
	private LocalDateTime startTime;

	@Column(name = "END_TIME")
	private LocalDateTime endTime;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JsonIgnore
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@Column(name = "PRIORITY")
	private int priority;

	@Enumerated(EnumType.STRING)
	@Column(name = "OFFER_PRICE_TYPE")
	private OfferPriceType offerPriceType;

	@JsonProperty("displayPrice")
	public BigDecimal getDisplayPrice() {
		return OrderUtils.getOfferPrice(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		OfferPrice that = (OfferPrice) o;
		return id != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
