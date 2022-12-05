package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "LIST_PRICE")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class ListPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "LISTPRICE_ID", nullable = false)
	private Long id;

	@Column(name = "PRICE", nullable = false)
	private BigDecimal price;

	@OneToOne(cascade = CascadeType.MERGE)
	@JsonIgnore
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID", nullable = false)
	private Item item;

	@Column(name = "CURRENCY", nullable = false)
	private String currency;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID")
	@ToString.Exclude
	private Store store;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ListPrice listPrice = (ListPrice) o;
		return id != null && Objects.equals(id, listPrice.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
