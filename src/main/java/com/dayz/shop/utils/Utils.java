package com.dayz.shop.utils;

import com.dayz.shop.config.LocalizationConfiguration;
import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.*;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSourceExt;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Utils {
	public static final Long rootStoreId = -1L;
	public static final String MERCHANT_ORDER_ID_KEY = "MERCHANT_ORDER_ID";
	public static final String US_STORE_KEY = "us_store";
	public static User DEFAULT_USER;
	private static Map<String, Store> storeNameStoreMap;

	private static PrivilegeRepository privilegeRepository;
	private static StoreConfigRepository storeConfigRepository;
	private static StoreRepository storeRepository;
	private static PaymentRepository paymentRepository;
	private static ServerConfigRepository serverConfigRepository;
	private static ResourceBundleMessageSourceExt messageSource;
	private static AcceptHeaderLocaleResolver localeResolver;
	private static RoleRepository roleRepository;
	private static UserRepository userRepository;

	@Autowired
	public Utils(StoreRepository storeRepository, PrivilegeRepository privilegeRepository,
	             StoreConfigRepository storeConfigRepository, ServerConfigRepository serverConfigRepository,
	             PaymentRepository paymentRepository, ResourceBundleMessageSource messageSource,
	             AcceptHeaderLocaleResolver localeResolver, RoleRepository roleRepository, UserRepository userRepository) {
		Utils.privilegeRepository = privilegeRepository;
		Utils.storeConfigRepository = storeConfigRepository;
		Utils.serverConfigRepository = serverConfigRepository;
		Utils.storeRepository = storeRepository;
		Utils.paymentRepository = paymentRepository;
		Utils.storeNameStoreMap = storeRepository.findAll().stream().collect(Collectors.toMap(Store::getStoreName, store -> store));
		Utils.messageSource = new ResourceBundleMessageSourceExt(messageSource);
		Utils.localeResolver = localeResolver;
		Utils.roleRepository = roleRepository;
		Utils.userRepository = userRepository;
		DEFAULT_USER = userRepository.getById(-100L);
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
		} else if (store == null && request.getParameterMap().containsKey(MERCHANT_ORDER_ID_KEY)) {
			Optional<Payment> paymentOptional = paymentRepository.findById(Long.valueOf(request.getParameter(MERCHANT_ORDER_ID_KEY)));
			if (paymentOptional.isPresent()) {
				store = paymentOptional.get().getStore();
			}
		}
		return store;
	}

	public static boolean isFreeKassaIp(HttpServletRequest request) {
		String reqIp = Utils.getClientIpAddress(request);
		return Utils.getStoreConfig("freekassa.ips", -2L).contains(reqIp);
	}

	public static boolean isStoreServersRequest(HttpServletRequest request, Store store) {
		List<String> ips = serverConfigRepository.findAllByStoreAndKey(store, "SSH_IP").stream().map(ServerConfig::getValue).collect(Collectors.toList());
		String reqIp = Utils.getClientIpAddress(request);
		return ips.contains(reqIp);
	}

	public static User getCurrentUser() {
		return userRepository.getBySteamId(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSteamId());
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

	public static String getMessage(String key, Store store, Object... args) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		Locale locale = LocalizationConfiguration.DEFAULT_LOCALE;
		return messageSource.getMessage(key, args, locale);
	}

	public static Map<String, String> transformResultSetToMap(List<List<String>> resultSet) {
		return resultSet.stream()
				.map(input -> new AbstractMap.SimpleEntry<>(input.get(0), input.get(1)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
	}

	public static User createUser(Store store, String steamId) {
		User paymentUser = new User();
		paymentUser.setSteamId(steamId);
		paymentUser.setStore(store);
		paymentUser.setBalance(BigDecimal.ZERO);
		paymentUser.setActive(true);
		paymentUser.setSteamNickName("user");
		paymentUser.setSteamAvatarUrl("https://avatars.cloudflare.steamstatic.com/fef49e7fa7e1997310d705b2a6158ff8dc1cdfeb_full.jpg");
		paymentUser.getRoles().add(roleRepository.getById(-6L));
		return userRepository.save(paymentUser);
	}
}
