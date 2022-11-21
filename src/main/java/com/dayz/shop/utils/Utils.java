package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.RoleRepository;
import com.dayz.shop.repository.StoreRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Utils {
	public static final Long rootStoreId = -1L;
	private static Map<String, Store> storeNameStoreMap;

	private static RoleRepository roleRepository;
	private static StoreRepository storeRepository;

	@Autowired
	public Utils(RoleRepository roleRepository, StoreRepository storeRepository) {
		Utils.roleRepository = roleRepository;
		Utils.storeRepository = storeRepository;
		storeNameStoreMap = storeRepository.findAll().stream().collect(Collectors.toMap(Store::getStoreName, store -> store));
	}

	public static boolean isAppAdmin(User user) {
		return Utils.isRootStore((user).getStore()) && user.getRoles().contains(roleRepository.findByName("APP_ADMIN"));
	}

	public static boolean isRootStore(Store store) {
		return rootStoreId.equals(store.getId());
	}

	@SuppressWarnings("deprecation")
	public static boolean isSameStore(User user, OpenIDAuthenticationToken principal) {
		return user.getStore().getId().equals(((User) principal.getPrincipal()).getStore().getId());
	}

	public static Store extractStoreFromRequest(ServletRequest request) {
//		return storeNameStoreMap.get(request.getServerName().split("\\.")[0].toLowerCase());
		return storeNameStoreMap.get("alcatraz");
	}

	public static User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
