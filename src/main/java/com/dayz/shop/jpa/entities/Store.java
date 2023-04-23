package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "stores")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Store implements Serializable {
	@Id
	@Column(name = "STORE_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "STORE_NAME", nullable = false, unique = true)
	private String storeName;

	@OneToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "store_languages", joinColumns = @JoinColumn(name = "STORE_ID"), inverseJoinColumns = @JoinColumn(name = "LANGUAGE_ID"))
	@ToString.Exclude
	@JsonIgnore
	private List<Language> languages;

	@ElementCollection
	@CollectionTable(name = "store_config",
			joinColumns = {@JoinColumn(name = "STORE_ID", referencedColumnName = "STORE_ID")})
	@MapKeyColumn(name = "KEY")
	@Column(name = "VALUE")
	@JsonIgnore
	private Map<String, String> configs = new HashMap<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.MERGE)
	@ToString.Exclude
	private List<Server> servers;

	@OneToOne
	@JoinColumn(name = "PARENT_STORE_ID")
	private Store parentStore;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Store store = (Store) o;
		return id != null && Objects.equals(id, store.id) && Objects.equals(storeName, store.storeName);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}

	public String getString(String key) {
		return configs.get(key);
	}

	public Integer getInteger(String key) {
		return Integer.valueOf(getString(key));
	}

	public Long getLong(String key) {
		return Long.valueOf(getString(key));
	}

	public BigDecimal getBigDecimal(String key) {
		return new BigDecimal(getString(key));
	}
}
