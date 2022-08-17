package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "STORES")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Store implements Serializable {
	@Id
	@Column(name = "STORE_ID", nullable = false)
	private Long id;

	@Column(name = "STORE_NAME", nullable = false, unique = true)
	private String storeName;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "STORE_LANGUAGES", joinColumns = @JoinColumn(name = "STORE_ID"), inverseJoinColumns = @JoinColumn(name = "LANGUAGE_ID"))
	@ToString.Exclude
	private List<Language> languages;


	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	@ToString.Exclude
	private List<StoreConfig> configs;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	@ToString.Exclude
	private List<Server> servers;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Store store = (Store) o;
		return id != null && Objects.equals(id, store.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
