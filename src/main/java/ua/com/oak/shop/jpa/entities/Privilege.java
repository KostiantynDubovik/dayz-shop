package ua.com.oak.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
@Table(name = "privileges")
public class Privilege {

	@Id
	@Column(name = "PRIVILEGE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "PRIVILEGE_NAME")
	private String name;

	@JsonIgnore
	@ManyToMany(mappedBy = "privileges", cascade = CascadeType.MERGE)
	@ToString.Exclude
	private Collection<Role> roles;

	public Privilege() {
	}

	public Privilege(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Privilege privilege = (Privilege) o;
		return id != null && Objects.equals(id, privilege.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}