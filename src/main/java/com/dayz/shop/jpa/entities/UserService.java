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

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "USER_ID", nullable = false, insertable = false, updatable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "ITEM_TYPE", nullable = false, insertable = false, updatable = false)
	private ItemType itemType;

	@Column(name = "END_DATE")
	private LocalDateTime endDate;


	@JoinColumn(name = "SERVER_ID")
	@ManyToOne
	private Server server;

	@JoinColumn(name = "ORDER_ID")
	@OneToOne
	private Order order;

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
}
