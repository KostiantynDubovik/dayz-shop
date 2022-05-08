package com.dayz.shop.jpa.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "STORE_CONFIG")
public class StoreConfig {
	@Id
	@Column(name = "STORE_CONFIG_ID", nullable = false)
	private Long id;

	@ManyToOne
	private Store store;

	@Column(name = "KEY", nullable = false)
	private String key;

	@Column(name = "VALUE", nullable = false)
	private String value;
}
