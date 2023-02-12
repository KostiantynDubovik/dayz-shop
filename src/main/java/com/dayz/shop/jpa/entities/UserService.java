package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user_services")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@IdClass(UserServiceKey.class)
public class UserService {

	@Id
	@Column(name = "USER_ID", nullable = false, insertable = false, updatable = false)
	private Long userId;

	@Id
	@Column(name = "ITEM_TYPE", nullable = false, insertable = false, updatable = false)
	private String itemTypeStr;

	@Id
	@Column(name = "SERVER_ID", nullable = false, insertable = false, updatable = false)
	private Long serverId;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "USER_ID", nullable = false, insertable = false, updatable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "ITEM_TYPE", nullable = false, insertable = false, updatable = false)
	private ItemType itemType;

	@Column(name = "END_DATE")
	private LocalDateTime endDate;

	@JoinColumn(name = "ORDER_ID")
	@OneToOne
	private Order order;

	@JoinColumn(name = "SERVER_ID", nullable = false, insertable = false, updatable = false)
	@ManyToOne
	private Server server;

	public void setUser(User user) {
		this.user = user;
		this.userId = user.getId();
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
		this.itemTypeStr = itemType.toString();
	}

	public void setServer(Server server) {
		this.server = server;
		this.serverId = server.getId();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserService that = (UserService) o;
		return Objects.equals(user, that.user) && itemType == that.itemType && Objects.equals(endDate, that.endDate) && Objects.equals(server, that.server);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, itemType, endDate, server);
	}
}

@Data
class UserServiceKey implements Serializable {
	private Long userId;
	private String itemTypeStr;
	private Long serverId;
}
