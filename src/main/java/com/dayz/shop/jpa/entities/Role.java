package com.dayz.shop.jpa.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ROLES")
public class Role {

	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "ROLE_NAME")
	private String name;

	@ManyToMany(mappedBy = "roles")
	private List<User> users;

	@ManyToMany
	@JoinTable(
			name = "ROLES_PRIVILEGES",
			joinColumns = @JoinColumn(
					name = "ROLE_ID", referencedColumnName = "ROLE_ID"),
			inverseJoinColumns = @JoinColumn(
					name = "PRIVILEGE_ID", referencedColumnName = "PRIVILEGE_ID"))
	private List<Privilege> privileges;
}
