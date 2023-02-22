package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user_services")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class UserService {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "USER_SERVICE_ID")
	private Long userServiceId;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "USER_ID")
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "ITEM_TYPE")
	private ItemType itemType;

	@Column(name = "END_DATE")
	private LocalDateTime endDate;

	@JoinColumn(name = "ORDER_ID")
	@OneToOne
	private Order order;

	@JoinColumn(name = "SERVER_ID")
	@ManyToOne
	private Server server;

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
