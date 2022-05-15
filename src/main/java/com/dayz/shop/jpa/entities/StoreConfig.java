package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "STORE_CONFIG")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		StoreConfig that = (StoreConfig) o;
		return id != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
