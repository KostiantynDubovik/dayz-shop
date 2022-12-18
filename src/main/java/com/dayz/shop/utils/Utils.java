package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.*;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Utils {
	public static final Long rootStoreId = -1L;
	private static Map<String, Store> storeNameStoreMap;

	private static PrivilegeRepository privilegeRepository;
	private static StoreConfigRepository storeConfigRepository;
	private static ServerConfigRepository serverConfigRepository;

	@Autowired
	public Utils(StoreRepository storeRepository, PrivilegeRepository privilegeRepository, StoreConfigRepository storeConfigRepository, ServerConfigRepository serverConfigRepository) {
		Utils.privilegeRepository = privilegeRepository;
		Utils.storeConfigRepository = storeConfigRepository;
		Utils.serverConfigRepository = serverConfigRepository;
		storeNameStoreMap = storeRepository.findAll().stream().collect(Collectors.toMap(Store::getStoreName, store -> store));
	}

	public static boolean isAppAdmin(User user) {
		return Utils.isRootStore(user.getStore()) && user.getRoles().stream().flatMap((Function<Role, Stream<Privilege>>) input -> input.getPrivileges().stream()).collect(Collectors.toList()).contains(privilegeRepository.findByName("APP_WRITE"));
	}

	public static boolean isStoreAdmin(User user) {
		return user.getRoles().stream().flatMap((Function<Role, Stream<Privilege>>) input -> input.getPrivileges().stream()).collect(Collectors.toList()).contains(privilegeRepository.findByName("STORE_WRITE"));
	}

	public static boolean isAppAdmin() {
		return isAppAdmin(getCurrentUser());
	}

	public static boolean isStoreAdmin() {
		return isStoreAdmin(getCurrentUser());
	}

	public static boolean isRootStore(Store store) {
		return rootStoreId.equals(store.getId());
	}

	@SuppressWarnings("deprecation")
	public static boolean isSameStore(User user, OpenIDAuthenticationToken principal) {
		return user.getStore().getId().equals(((User) principal.getPrincipal()).getStore().getId());
	}

	public static Store extractStoreFromRequest(ServletRequest request) {
		return storeNameStoreMap.get(request.getServerName().split("\\.")[0].toLowerCase());
	}

	public static User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static String getClientIpAddress(HttpServletRequest request) {
		String xForwardedForHeader = request.getHeader("X-Forwarded-For");
		if (xForwardedForHeader == null) {
			return request.getRemoteAddr();
		} else {
			return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
		}
	}

	public static String getStoreConfig(String key, Long storeId) {
		String result = null;
		StoreConfig storeConfig = storeConfigRepository.findByKeyAndStoreId(key, storeId);
		if (storeConfig != null) {
			result = storeConfig.getValue();
		}
		return result;
	}

	public static String getStoreConfig(String key, Store store) {
		String result = null;
		StoreConfig storeConfig = storeConfigRepository.findByKeyAndStore(key, store);
		if (storeConfig != null) {
			result = storeConfig.getValue();
		}
		return result;
	}

	public static String getServerConfig(String key, Long serverId) {

		String result = null;
		ServerConfig serverConfig = serverConfigRepository.findByKeyAndServerId(key, serverId);
		if (serverConfig != null) {
			result = serverConfig.getValue();
		}
		return result;
	}

	public static String getServerConfig(String key, Server server) {
		String result = null;
		ServerConfig serverConfig = serverConfigRepository.findByKeyAndServer(key, server);
		if (serverConfig != null) {
			result = serverConfig.getValue();
		}
		return result;
	}
}
