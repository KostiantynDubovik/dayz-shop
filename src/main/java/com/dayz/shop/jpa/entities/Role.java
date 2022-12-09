package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "roles")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Role {

	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "ROLE_NAME")
	private String name;

	@JsonIgnore
	@ManyToMany(mappedBy = "roles", cascade = CascadeType.MERGE)
	@ToString.Exclude
	private List<User> users;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "roles_privileges",
			joinColumns = @JoinColumn(
					name = "ROLE_ID", referencedColumnName = "ROLE_ID"),
			inverseJoinColumns = @JoinColumn(
					name = "PRIVILEGE_ID", referencedColumnName = "PRIVILEGE_ID"))
	private List<Privilege> privileges;

	public Role() {
	}

	public Role(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Role role = (Role) o;
		return id != null && Objects.equals(id, role.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
