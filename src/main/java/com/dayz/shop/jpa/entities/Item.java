package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "items")
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
	@JoinTable(name = "item_category",
			joinColumns = @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID"))
	@ToString.Exclude
	private List<Category> categories;

	@Formula("(select lp.PRICE from list_price lp where lp.ITEM_ID = ITEM_ID AND lp.STORE_ID = STORE_ID)")
	private BigDecimal listPrice;

	@OneToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "offer_price",
			joinColumns = @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "OFFER_ID", referencedColumnName = "ITEM_ID"))
	@ToString.Exclude
	private List<OfferPrice> offerPrices;

	@Column(name = "IMAGE_URL")
	private String imageUrl;

	@OneToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "sub_items",
			joinColumns = @JoinColumn(name = "MAIN_ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "SUB_ITEM_ID", referencedColumnName = "ITEM_ID"))
	@ToString.Exclude
	@JsonBackReference
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

	@Column(name = "COLOR")
	private String color;

	@Column(name = "SEQUENCE")
	private Long sequence;

	@OneToOne
	private ItemDescription itemDescription;

	@ManyToMany()
	@JoinTable(name = "item_server_buyable",
			joinColumns = {@JoinColumn(name = "item_id")},
			inverseJoinColumns = {@JoinColumn(name = "server_id")})
	private List<Server> servers;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Item item = (Item) o;
		return Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(inGameId, item.inGameId) && Objects.equals(store, item.store) && Objects.equals(categories, item.categories) && Objects.equals(listPrice, item.listPrice) && Objects.equals(offerPrices, item.offerPrices) && Objects.equals(imageUrl, item.imageUrl) && Objects.equals(subItems, item.subItems) && Objects.equals(count, item.count) && itemType == item.itemType && Objects.equals(attributes, item.attributes) && Objects.equals(color, item.color) && Objects.equals(servers, item.servers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, inGameId, store, categories, listPrice, offerPrices, imageUrl, subItems, count, itemType, attributes, color, servers);
	}
}
