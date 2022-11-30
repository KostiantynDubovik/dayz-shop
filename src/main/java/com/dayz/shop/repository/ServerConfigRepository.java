package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Server;
import com.dayz.shop.jpa.entities.ServerConfig;
import com.dayz.shop.jpa.entities.StoreConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerConfigRepository extends JpaRepository<ServerConfig, Long> {
	ServerConfig findByKeyAndServer(String key, Server store);
	List<ServerConfig> findAllByServer(Server store);
}
