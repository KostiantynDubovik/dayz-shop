package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.ItemType;
import com.dayz.shop.jpa.entities.Server;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.jpa.entities.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserServiceRepository extends JpaRepository<UserService, Long> {

	List<UserService> findAllByEndDateIsBefore(LocalDateTime endDate);
	UserService findByUserAndItemTypeAndServer(User user, ItemType itemType, Server server);
}
