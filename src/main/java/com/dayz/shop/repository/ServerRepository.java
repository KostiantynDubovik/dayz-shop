package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Server;
import com.dayz.shop.jpa.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {
	List<Server> findAllByStoreId(Long storeId);
	List<Server> findAllByStore(Store store);
}
