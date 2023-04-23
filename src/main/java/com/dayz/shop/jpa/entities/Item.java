package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
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
	@JsonIgnore
	private List<Category> categories = new ArrayList<>();

	@OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
	private ListPrice listPrice;

	@OneToMany(mappedBy = "item", cascade = CascadeType.MERGE)
	@ToString.Exclude
	private List<OfferPrice> offerPrices = new ArrayList<>();

	@Column(name = "IMAGE_URL")
	private String imageUrl;

	@OneToMany(mappedBy = "item")
	@ToString.Exclude
	private List<SubItem> subItems = new ArrayList<>();

	@Column(name = "COUNT")
	private Integer count = 1;

	@Enumerated(EnumType.STRING)
	@Column(name = "ITEM_TYPE")
	private ItemType itemType;

	@OneToMany(mappedBy = "item")
	@ToString.Exclude
	@JsonIgnore
	private List<ItemAttribute> attributes = new ArrayList<>();

	@Column(name = "COLOR")
	private String color;

	@Column(name = "SEQUENCE")
	private Long sequence;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "DESCRIPTION_ID")
	private Description description;

	@ManyToMany()
	@JoinTable(name = "item_server_buyable",
			joinColumns = {@JoinColumn(name = "item_id")},
			inverseJoinColumns = {@JoinColumn(name = "server_id")})
	@ToString.Exclude
	@JsonIgnore
	private List<Server> servers;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Item item = (Item) o;
		return Objects.equals(id, item.id) && Objects.equals(description, item.description) && Objects.equals(inGameId, item.inGameId) && Objects.equals(store, item.store) && Objects.equals(categories, item.categories) && Objects.equals(listPrice, item.listPrice) && Objects.equals(offerPrices, item.offerPrices) && Objects.equals(imageUrl, item.imageUrl) /*&& Objects.equals(subItems, item.subItems)*/ && Objects.equals(count, item.count) && itemType == item.itemType && Objects.equals(attributes, item.attributes) && Objects.equals(color, item.color) && Objects.equals(servers, item.servers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, description, inGameId, store, categories, listPrice, offerPrices, imageUrl, /*subItems,*/ count, itemType, attributes, color, servers);
	}
}
