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
@Table(name = "USER_SERVICES")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@IdClass(UserServiceKey.class)
public class UserService {

	@EmbeddedId
	private UserServiceKey userServiceKey;

	@Id
	@ManyToOne
	@JsonBackReference
	private User user;

	@Id
	private ItemType itemType;

	@Column(name = "END_DATE")
	private LocalDateTime endDate;


	@JoinColumn(name = "SERVER_ID")
	@ManyToOne
	private Server server;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserService that = (UserService) o;

		if (!Objects.equals(userServiceKey, that.userServiceKey))
			return false;
		if (!Objects.equals(user, that.user)) return false;
		if (itemType != that.itemType) return false;
		return Objects.equals(endDate, that.endDate);
	}

	@Override
	public int hashCode() {
		int result = userServiceKey != null ? userServiceKey.hashCode() : 0;
		result = 31 * result + (user != null ? user.hashCode() : 0);
		result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
		result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		return result;
	}
}

@Embeddable
@Data
class UserServiceKey implements Serializable {
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "USER_ID", nullable = false, insertable = false, updatable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "ITEM_TYPE", nullable = false, insertable = false, updatable = false)
	private ItemType itemType;
}
