package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "sub_items")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@IdClass(SubItemKey.class)
public class SubItem {

	@EmbeddedId
	private SubItemKey primaryKey;

	@Id
	@ManyToOne
	@JsonBackReference
	private Item item;

	@Id
	@OneToOne
	@JsonBackReference
	private Item subItem;

	@Column(name = "QUANTITY")
	private Integer quantity;
}


@Embeddable
@Data
class SubItemKey implements Serializable {


	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false, insertable = false, updatable = false)
	@JsonBackReference
	private Item item;

	@OneToOne
	@JoinColumn(name = "SUB_ITEM_ID", nullable = false, insertable = false, updatable = false)
	@JsonBackReference
	private Item subItem;
}
