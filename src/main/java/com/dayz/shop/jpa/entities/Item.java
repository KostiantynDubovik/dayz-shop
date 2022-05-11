package com.dayz.shop.jpa.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ITEMS")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ITEM_ID", nullable = false)
	private Long id;

	@Column(name = "ITEM_NAME", nullable = false)
	private String itemName;

	@Column(name = "ITEM_DESCRIPTION")
	private String itemDescription;

	@ManyToMany
	@JoinTable(name = "ITEM_CATEGORY",
			joinColumns = @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID"))
	private List<Category> itemCategories;

	@OneToMany
	@JoinTable(name = "OFFER_PRICE",
			joinColumns = @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID"),
			inverseJoinColumns = @JoinColumn(name = "OFFER_ID", referencedColumnName = "ITEM_ID"))
	private List<OfferPrice> offerPriceList;

	@Column(name = "BUYABLE", nullable = false)
	private boolean buyable;

	@Column(name = "IMAGE_URL")
	private String imageUrl;

	@OneToMany
	@JoinTable(name = "SUB_ITEMS")
	private List<Item> subItems;

}
