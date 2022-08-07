package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "STORE_CONFIG")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
@IdClass(StoreConfigKey.class)
public class StoreConfig {

	@Id
	@ManyToOne
	private Store store;

	@Id
	private String key;

	@EmbeddedId
	private StoreConfigKey primaryKey;

	@Column(name = "VALUE", nullable = false)
	private String value;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		StoreConfig that = (StoreConfig) o;
		return store.equals(that.store) && key.equals(that.key) && value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(store, key, value);
	}
}

@Embeddable
class StoreConfigKey implements Serializable {
	@ManyToOne
	@JoinColumn(name = "STORE_ID", nullable = false, insertable = false, updatable = false)
	private Store store;

	@Column(name = "KEY", nullable = false, insertable = false, updatable = false)
	private String key;

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StoreConfigKey that = (StoreConfigKey) o;
		return store.equals(that.store) && key.equals(that.key);
	}

	@Override
	public int hashCode() {
		return Objects.hash(store, key);
	}
}
