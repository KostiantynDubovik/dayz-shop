package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Server;
import com.dayz.shop.jpa.entities.ServerConfig;
import com.dayz.shop.jpa.entities.StoreConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerConfigRepository extends JpaRepository<ServerConfig, Long> {
	ServerConfig findByKeyAndServer(String key, Server server);
	ServerConfig findByKeyAndServerId(String key, Long serverId);
	List<ServerConfig> findAllByServer(Server server);
}
