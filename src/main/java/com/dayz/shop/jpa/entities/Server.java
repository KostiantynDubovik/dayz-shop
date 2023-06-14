package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "servers")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "store"})
public class Server {
	@Id
	@Column(name = "SERVER_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@JsonProperty("name")
	@Column(name = "SERVER_NAME")
	private String serverName;

	@JsonIgnore
	@Column(name = "INSTANCE_NAME")
	private String instanceName;

	@ElementCollection
	@CollectionTable(name = "server_config",
			joinColumns = {@JoinColumn(name = "SERVER_ID", referencedColumnName = "SERVER_ID")})
	@MapKeyColumn(name = "KEY")
	@Column(name = "VALUE")
	@JsonIgnore
	private Map<String, String> configs = new HashMap<>();

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

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}

	public String getString(String key) {
		return configs.get(key);
	}

	public Integer getInteger(String key) {
		return Integer.valueOf(getString(key));
	}

	public Long getLong(String key) {
		return Long.valueOf(getString(key));
	}

	public BigDecimal getBigDecimal(String key) {
		return new BigDecimal(getString(key));
	}
}
