package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
	@JsonBackReference
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
@Data
class StoreConfigKey implements Serializable {
	@ManyToOne
	@JoinColumn(name = "STORE_ID", nullable = false, insertable = false, updatable = false)

	@JsonBackReference
	private Store store;

	@Column(name = "KEY", nullable = false, insertable = false, updatable = false)
	private String key;
}
