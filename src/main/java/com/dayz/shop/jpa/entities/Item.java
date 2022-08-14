package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ITEMS")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ITEM_ID", nullable = false)
	private Long id;

	@Column(name = "ITEM_NAME", nullable = false)
	private String name;

	@Column(name = "IN_GAME_ID", nullable = false)
	private String inGameId;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@ManyToMany
	@JoinTable(name = "ITEM_CATEGORY",
			joinColumns = @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID"))
	@ToString.Exclude
	private List<Category> categories;

	@Formula("(select lp.PRICE from LIST_PRICE lp where lp.ITEM_ID = ITEM_ID AND lp.STORE_ID = STORE_ID)")
	private BigDecimal listPrice;

	@OneToMany
	@JoinTable(name = "OFFER_PRICE",
			joinColumns = @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "OFFER_ID", referencedColumnName = "ITEM_ID"))
	@ToString.Exclude
	private List<OfferPrice> offerPrices;

	@Column(name = "BUYABLE", nullable = false)
	private boolean buyable;

	@Column(name = "IMAGE_URL")
	private String imageUrl;

	@OneToMany
	@JoinTable(name = "SUB_ITEMS",
			joinColumns = @JoinColumn(name = "MAIN_ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "SUB_ITEM_ID", referencedColumnName = "ITEM_ID"))
	@ToString.Exclude
	private List<Item> subItems;

	@Column(name = "DELETABLE")
	private boolean deletable;

	@Column(name = "COUNT")
	private int count;

	@Enumerated(EnumType.STRING)
	@Column(name = "ITEM_TYPE")
	private ItemType itemType;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Item item = (Item) o;
		return buyable == item.buyable && deletable == item.deletable && count == item.count && id.equals(item.id)
				&& name.equals(item.name) && inGameId.equals(item.inGameId) && store.equals(item.store)
				&& Objects.equals(categories, item.categories) && listPrice.equals(item.listPrice)
				&& Objects.equals(offerPrices, item.offerPrices) && Objects.equals(imageUrl, item.imageUrl)
				&& subItems.equals(item.subItems) && itemType == item.itemType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, inGameId, store, categories, listPrice, offerPrices, buyable, imageUrl, subItems, deletable, count, itemType);
	}
}
