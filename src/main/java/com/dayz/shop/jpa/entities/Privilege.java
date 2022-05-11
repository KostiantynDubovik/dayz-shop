package com.dayz.shop.jpa.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
public class Privilege {

	@Id
	@Column(name = "PRIVILEGE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "PRIVILEGE_NAME")
	private String name;

	@ManyToMany(mappedBy = "privileges")
	private Collection<Role> roles;
}