package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.RoleRepository;
import com.dayz.shop.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Utils {
	public static final Long rootStoreId = -1L;
	private static Map<String, Long> storeNameStoreMap;

	private static RoleRepository roleRepository;
	private static StoreRepository storeRepository;

	@Autowired
	public Utils(RoleRepository roleRepository, StoreRepository storeRepository) {
		Utils.roleRepository = roleRepository;
		Utils.storeRepository = storeRepository;
		storeNameStoreMap = storeRepository.findAll().stream().collect(Collectors.toMap(Store::getStoreName, Store::getId));
	}

	public static boolean isAppAdmin(User user) {
		return Utils.isRootStore((user).getStore()) && user.getRoles().contains(roleRepository.findByName("APP_ADMIN"));
	}

	public static boolean isRootStore(Store store) {
		return rootStoreId.equals(store.getId());
	}

	public static boolean isSameStore(User user, OpenIDAuthenticationToken principal) {
		return user.getStore().getId().equals(((User) principal.getPrincipal()).getStore().getId());
	}

	public static Store extractStoreFromRequest(ServletRequest request) {
		return storeRepository.getById(storeNameStoreMap.get(request.getServerName().split("\\.")[0].toLowerCase()));
	}
}
