package ua.com.oak.shop.repository;

import ua.com.oak.shop.jpa.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

	Privilege findByName(String name);
}
