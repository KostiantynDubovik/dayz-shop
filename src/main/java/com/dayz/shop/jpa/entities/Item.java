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

	@ManyToOne(cascade = CascadeType.MERGE)
	@JsonIgnore
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "ITEM_CATEGORY",
			joinColumns = @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID"))
	@ToString.Exclude
	private List<Category> categories;

	@Formula("(select lp.PRICE from LIST_PRICE lp where lp.ITEM_ID = ITEM_ID AND lp.STORE_ID = STORE_ID)")
	private BigDecimal listPrice;

	@OneToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "OFFER_PRICE",
			joinColumns = @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "OFFER_ID", referencedColumnName = "ITEM_ID"))
	@ToString.Exclude
	private List<OfferPrice> offerPrices;

	@Column(name = "IMAGE_URL")
	private String imageUrl;

	@OneToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "SUB_ITEMS",
			joinColumns = @JoinColumn(name = "MAIN_ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "SUB_ITEM_ID", referencedColumnName = "ITEM_ID"))
	@ToString.Exclude
	private List<Item> subItems;

	@Column(name = "COUNT")
	private Integer count = 1;

	@Enumerated(EnumType.STRING)
	@Column(name = "ITEM_TYPE")
	private ItemType itemType;

	@OneToMany(mappedBy = "item")
	@ToString.Exclude
	@JsonIgnore
	private List<ItemAttribute> attributes;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Item item = (Item) o;
		return Objects.equals(count, item.count) && id.equals(item.id)
				&& name.equals(item.name) && inGameId.equals(item.inGameId) && store.equals(item.store)
				&& Objects.equals(categories, item.categories) && listPrice.equals(item.listPrice)
				&& Objects.equals(offerPrices, item.offerPrices) && Objects.equals(imageUrl, item.imageUrl)
				&& subItems.equals(item.subItems) && itemType == item.itemType && attributes.equals(item.getAttributes());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, inGameId, store, categories, listPrice, offerPrices, imageUrl, subItems, count, itemType, attributes);
	}
}
