package com.dayz.shop.jpa.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "STORES")
public class Store {
	@Id
	@Column(name = "STORE_ID", nullable = false)
	private Long id;

	@Column(name = "STORE_NAME", nullable = false, unique = true)
	private String storeName;

	@OneToMany
	@JoinTable(name = "STORE_LANGUAGES", joinColumns = @JoinColumn(name = "STORE_ID"), inverseJoinColumns = @JoinColumn(name = "LANGUAGE_ID"))
	private List<Language> languages;


	@OneToMany(mappedBy = "store")
	private List<StoreConfig> configs;

	@OneToMany(mappedBy = "store")
	private List<Server> servers;

}
