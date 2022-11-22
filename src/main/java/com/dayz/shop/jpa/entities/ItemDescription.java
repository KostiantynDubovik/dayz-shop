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
@Table(name = "ITEM_DESCRIPTION")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class ItemDescription {
	@Id
	@Column(name = "DESCRIPTION_ID", nullable = false)
	private Long id;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "LANGUAGE_ID")
	private Language language;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ITEM_ID")
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
