package com.dayz.shop.jpa.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Data
@Entity
@Table(name = "LANGUAGES")
public class Language {
	@Id
	@Column(name = "LANGUAGE_ID", nullable = false)
	private Long id;

	@Column(name = "LANGUAGE")
	private String language;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "LOCALE")
	private String locale;
}
