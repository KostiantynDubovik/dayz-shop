package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "item_description")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class ItemDescription {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "DESCRIPTION_ID", nullable = false)
	private Long id;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "LANGUAGE_ID")
	private Language language;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID", nullable = false)
	private Item item;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "PUBLISHED")
	private boolean published;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ItemDescription that = (ItemDescription) o;
		return id != null && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
