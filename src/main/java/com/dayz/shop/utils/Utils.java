package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.*;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Utils {
	public static final Long rootStoreId = -1L;
	public static final String MERCHANT_ORDER_ID_KEY = "MERCHANT_ORDER_ID";
	public static final String US_STORE_KEY = "us_store";
	private static Map<String, Store> storeNameStoreMap;

	private static PrivilegeRepository privilegeRepository;
	private static StoreConfigRepository storeConfigRepository;
	private static StoreRepository storeRepository;
	private static OrderRepository orderRepository;
	private static ServerConfigRepository serverConfigRepository;

	@Autowired
	public Utils(StoreRepository storeRepository, PrivilegeRepository privilegeRepository,
	             StoreConfigRepository storeConfigRepository, ServerConfigRepository serverConfigRepository,
	             OrderRepository orderRepository) {
		Utils.privilegeRepository = privilegeRepository;
		Utils.storeConfigRepository = storeConfigRepository;
		Utils.serverConfigRepository = serverConfigRepository;
		Utils.storeRepository = storeRepository;
		Utils.orderRepository = orderRepository;
		Utils.storeNameStoreMap = storeRepository.findAll().stream().collect(Collectors.toMap(Store::getStoreName, store -> store));
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

	public static Store extractStoreFromRequest(HttpServletRequest request) {
		Store store = storeNameStoreMap.get(request.getServerName().split("\\.")[0].toLowerCase());
		if (store == null && request.getParameterMap().containsKey(US_STORE_KEY)) {
			store = storeNameStoreMap.get(request.getParameter(US_STORE_KEY));
		} else if (store == null && request.getParameterMap().containsKey("MERCHANT_ORDER_ID")) {
			Order order = orderRepository.getById(Long.valueOf(request.getParameter("MERCHANT_ORDER_ID")));
			if (order != null) {
				store = order.getStore();
			}
		}
		return store;
	}

	public static boolean isFreeKassaIp(HttpServletRequest request) {
		String reqIp = Utils.getClientIpAddress(request);
		return Utils.getStoreConfig("freekassa.ips", -2L).contains(reqIp);
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
