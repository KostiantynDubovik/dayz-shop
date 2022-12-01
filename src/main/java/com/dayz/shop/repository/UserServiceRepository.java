package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.ItemType;
import com.dayz.shop.jpa.entities.Server;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.jpa.entities.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserServiceRepository extends JpaRepository<UserService, Long> {

	UserService findByUserAndItemTypeAndServer(User user, ItemType itemType, Server server);
}
