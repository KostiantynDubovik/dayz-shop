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
@Table(name = "SERVERS")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "store"})
public class Server {
	@Id
	@Column(name = "SERVER_ID", nullable = false)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@Column(name = "SERVER_NAME")
	private String serverName;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Server server = (Server) o;
		return id != null && Objects.equals(id, server.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
