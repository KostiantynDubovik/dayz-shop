package ua.com.oak.shop.repository;

import ua.com.oak.shop.jpa.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);

	List<Role> findAllByName(String roleName);
	List<Role> findAllByNameIn(List<String> roleName);
}
