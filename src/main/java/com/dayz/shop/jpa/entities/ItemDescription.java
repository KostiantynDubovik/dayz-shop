package com.dayz.shop.jpa.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ITEM_DESCRIPTION")
public class ItemDescription {
	@Id
	@Column(name = "DESCRIPTION_ID", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "LANGUAGE_ID")
	private Language language;

	@ManyToOne
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@ManyToOne
	@JoinColumn(name = "ITEM_ID")
	private Item item;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "PUBLISHED")
	private boolean published;
}
