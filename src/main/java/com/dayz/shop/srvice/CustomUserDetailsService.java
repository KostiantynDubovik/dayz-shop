package com.dayz.shop.srvice;

import com.dayz.shop.jpa.entities.Privilege;
import com.dayz.shop.jpa.entities.Role;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.RoleRepository;
import com.dayz.shop.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.wink.json4j.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Service
public class CustomUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken>, UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

	private UserRepository userRepository;
	private RoleRepository roleRepository;

	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {

		log.info("Loading user details ...");

		String name = token.getName().substring(token.getName().lastIndexOf('/') + 1);

		log.info("Finding user by token " + name);

		User user = userRepository.findBySteamId(name);

		if (user == null) {

			try {
				user = getUserService().createUser(name);
			} catch (JSONException e) {
				throw new UsernameNotFoundException("ErrorParsing json", e);
			}
			user = userRepository.save(user);
		}
		return user;

	}

	@Override
	public UserDetails loadUserByUsername(String steamId)
			throws UsernameNotFoundException {

		User user = userRepository.findBySteamId(steamId);
		if (user == null) {
			return new org.springframework.security.core.userdetails.User(
					" ", " ", true, true, true, true,
					getAuthorities(Collections.singletonList(
							roleRepository.findByName("USER"))));
		}

		return new org.springframework.security.core.userdetails.User(
				user.getSteamId(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
				user.isCredentialsNonExpired(), user.isAccountNonLocked(), getAuthorities(user.getRoles()));
	}

	private List<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
		return getGrantedAuthorities(getPrivileges(roles));
	}

	private List<String> getPrivileges(List<Role> roles) {
		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		for (Role role : roles) {
			privileges.add(role.getName());
			collection.addAll(role.getPrivileges());
		}
		for (Privilege item : collection) {
			privileges.add(item.getName());
		}
		return privileges;
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}
}
