package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server, Long> {
	List<Server> findAllByStoreId(Long storeId);
}
