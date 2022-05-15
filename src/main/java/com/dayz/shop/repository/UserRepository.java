package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	User getBySteamId(String steamId);
	User getBySteamIdAndStore(String steamId, Store store);
	List<User> getAllByStore(Store store);
}
