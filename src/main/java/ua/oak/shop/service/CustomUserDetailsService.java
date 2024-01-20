package ua.oak.shop.service;

import ua.oak.shop.jpa.entities.Privilege;
import ua.oak.shop.jpa.entities.Role;
import ua.oak.shop.jpa.entities.Store;
import ua.oak.shop.jpa.entities.User;
import ua.oak.shop.repository.RoleRepository;
import ua.oak.shop.repository.UserRepository;
import ua.oak.shop.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.openid4java.consumer.ConsumerManager;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Service
@SuppressWarnings("deprecation")
public class CustomUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken>, UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private ConsumerManager manager;
	private UserService userService;
	private HttpServletRequest request;

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

	@Autowired
	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Autowired
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	@SuppressWarnings("deprecation")
	public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
		String steamId = token.getName().substring(token.getName().lastIndexOf('/') + 1);
		Store store = Utils.extractStoreFromRequest(request);
		User user = userRepository.getBySteamIdAndRolesIn(steamId, Collections.singletonList(roleRepository.findByName("APP_ADMIN")));
		if (user == null) {
			user = userRepository.getBySteamIdAndStore(steamId, store);
		}
		if (user == null) {
			user = getUserService().createUser(steamId, store);
		} else {
			user = userService.updateUser(user, store);
		}
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String steamId)
			throws UsernameNotFoundException {
		User user = userRepository.getBySteamId(steamId);
		if (user != null) {
			return user;
		} else {
			return new org.springframework.security.core.userdetails.User(
					StringUtils.EMPTY, StringUtils.EMPTY, true, true, true, true,
					getAuthorities(Collections.singletonList(
							roleRepository.findByName("USER"))));
		}
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
