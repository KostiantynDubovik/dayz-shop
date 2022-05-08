package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "USERS")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ID", nullable = false)
	private Long id;

	@Column(name = "STEAM_ID")
	private String steamId;

	@Column(name = "STEAM_NICKNAME")
	private String steamNickName;

	@Column(name = "STEAM_AVATAR_URL")
	private String steamAvatarUrl;

	@Column(name = "BALANCE")
	private BigDecimal balance;

	@Enumerated(EnumType.STRING)
	@Column(name = "ROLE")
	private Role role;

	@ManyToOne
	@JoinColumn(name = "STORE_ID")
	private Store currentStore;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Order> orders;

	@Override
	@Transient
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("ROLE_USER");
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
		return true;
	}
}

