package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.StoreConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreConfigRepository  extends JpaRepository<StoreConfig, Long> {
	StoreConfig findByKeyAndStore(String key, Store store);
	default String getValueByKeyAndStore(String key, Store store) {
		return findByKeyAndStore(key, store).getValue();
	}
	List<StoreConfig> findAllByStore(Store store);
}
