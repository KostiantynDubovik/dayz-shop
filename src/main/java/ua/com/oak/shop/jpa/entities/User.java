package ua.com.oak.shop.jpa.entities;

import ua.com.oak.shop.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID"})})
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "USER_ID")
	private Long id;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JsonIgnore
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@Column(name = "STEAM_ID")
	private String steamId;

	@Column(name = "STEAM_NICKNAME")
	private String steamNickName;

	@Column(name = "STEAM_AVATAR_URL")
	private String steamAvatarUrl;

	@Column(name = "BALANCE")
	private BigDecimal balance = BigDecimal.ZERO;

	@Column(name = "IS_ACTIVE")
	private Boolean active;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(
					name = "USER_ID", referencedColumnName = "USER_ID"),
			inverseJoinColumns = @JoinColumn(
					name = "ROLE_ID", referencedColumnName = "ROLE_ID"))
	@JsonIgnore
	private List<Role> roles = new ArrayList<>();

	@ToString.Exclude
	@OneToMany(mappedBy = "user")
	private List<Order> orders;

	@ToString.Exclude
	@OneToMany(mappedBy = "user")
	private List<Payment> payments;

	@JsonProperty(value = "isAdmin", defaultValue = "false")
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	public boolean isAdmin() {
		return Utils.isStoreAdmin(this);
	}

	@Override
	@Transient
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles().stream()
				.flatMap(role -> role.getPrivileges().stream())
				.map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
				.collect(Collectors.toList());
	}

	@Override
	@Transient
	@JsonIgnore
	public String getPassword() {
		return StringUtils.EMPTY;
	}

	@Override
	@Transient
	@JsonIgnore
	public String getUsername() {
		return getSteamId();
	}

	@JsonIgnore
	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Transient
	@Override
	public boolean isEnabled() {
		return active;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		User user = (User) o;
		return id != null && Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}

