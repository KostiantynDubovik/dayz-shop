package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@Table(name = "LIST_PRICE")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class ListPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "LISTPRICE", nullable = false)
	private Long id;

	@Column(name = "PRICE", nullable = false)
	private BigDecimal price;

	@OneToOne
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID", nullable = false)
	private Item item;

	@Column(name = "CURRENCY", nullable = false)
	private String currency;
}
