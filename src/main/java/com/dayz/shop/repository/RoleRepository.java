package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);

	List<Role> findAllByName(String roleName);
}
