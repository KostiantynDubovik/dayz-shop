package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findBySteamId(String steamId);
}
