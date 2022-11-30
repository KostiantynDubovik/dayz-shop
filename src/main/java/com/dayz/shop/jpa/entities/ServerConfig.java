package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "SERVER_CONFIG")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
@IdClass(ServerConfigKey.class)
public class ServerConfig {

	@EmbeddedId
	private ServerConfigKey primaryKey;

	@Id
	@ManyToOne
	@JsonBackReference
	private Server server;

	@Id
	private String key;

	@Column(name = "VALUE", nullable = false)
	private String value;

	@ManyToOne
	@JsonBackReference
	private Store store;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ServerConfig that = (ServerConfig) o;

		if (!Objects.equals(primaryKey, that.primaryKey)) return false;
		if (!Objects.equals(server, that.server)) return false;
		if (!Objects.equals(store, that.store)) return false;
		if (!Objects.equals(key, that.key)) return false;
		if (!Objects.equals(value, that.value)) return false;
		return Objects.equals(server, that.server);
	}

	@Override
	public int hashCode() {
		int result = primaryKey != null ? primaryKey.hashCode() : 0;
		result = 31 * result + (server != null ? server.hashCode() : 0);
		result = 31 * result + (store != null ? store.hashCode() : 0);
		result = 31 * result + (key != null ? key.hashCode() : 0);
		result = 31 * result + (value != null ? value.hashCode() : 0);
		result = 31 * result + (server != null ? server.hashCode() : 0);
		return result;
	}
}

@Embeddable
@Data
class ServerConfigKey implements Serializable {
	@ManyToOne
	@JoinColumn(name = "SERVER_ID", nullable = false, insertable = false, updatable = false)
	@JsonBackReference
	private Server server;

	@Column(name = "KEY", nullable = false, insertable = false, updatable = false)
	private String key;
}
