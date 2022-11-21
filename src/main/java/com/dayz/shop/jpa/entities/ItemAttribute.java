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
@Table(name = "ITEM_ATTRIBUTES")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@IdClass(ItemAttributeKey.class)
public class ItemAttribute {

	@EmbeddedId
	private ItemAttributeKey primaryKey;

	@Id
	@ManyToOne
	@JsonBackReference
	private Store store;

	@Id
	@ManyToOne
	@JsonBackReference
	private Item item;

	@Id
	private String attributeName;

	@Column(name = "ATTRIBUTE_VALUE", nullable = false)
	private String attributeValue;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ItemAttribute that = (ItemAttribute) o;
		return item != null && Objects.equals(item, that.item)
				&& store != null && Objects.equals(store, that.store)
				&& attributeName != null && Objects.equals(attributeName, that.attributeName)
				&& primaryKey != null && Objects.equals(primaryKey, that.primaryKey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(item,
				store,
				attributeName,
				primaryKey);
	}
}

@Embeddable
@Data
class ItemAttributeKey implements Serializable {

	@ManyToOne
	@JoinColumn(name = "STORE_ID", nullable = false, insertable = false, updatable = false)
	@JsonBackReference
	private Store store;

	@Column(name = "ATTRIBUTE_NAME", nullable = false, insertable = false, updatable = false)
	private String attributeName;

	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false, insertable = false, updatable = false)
	@JsonBackReference
	private Item item;
}
