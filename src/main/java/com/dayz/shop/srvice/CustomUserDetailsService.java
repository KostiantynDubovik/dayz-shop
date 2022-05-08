package com.dayz.shop.srvice;

import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.UserRepository;
import org.apache.wink.json4j.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

	private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

	private UserRepository userRepository;

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
}
