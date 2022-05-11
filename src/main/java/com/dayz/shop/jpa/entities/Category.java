package com.dayz.shop.jpa.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "CATEGORIES")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CATEGORY_ID", nullable = false)
	private Long id;

	@Column(name = "CATEGORY_NAME", nullable = false)
	private String categoryName;

	@OneToMany
	@JoinTable(name = "CATEGORY_RELATIONS",
			joinColumns = @JoinColumn(name = "PARENT_CATEGORY_ID", referencedColumnName = "CATEGORY_ID", unique = false),
			inverseJoinColumns = @JoinColumn(name = "CHILD_CATEGORY_ID", referencedColumnName = "CATEGORY_ID", unique = false))
	private List<Category> childCategories;
}
