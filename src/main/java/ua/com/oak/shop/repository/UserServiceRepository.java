package ua.com.oak.shop.repository;

import ua.com.oak.shop.jpa.entities.ItemType;
import ua.com.oak.shop.jpa.entities.User;
import ua.com.oak.shop.jpa.entities.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserServiceRepository extends JpaRepository<UserService, Long> {

	List<UserService> findAllByEndDateIsBefore(LocalDateTime endDate);
	UserService findByUserAndItemTypeAndServer(User user, ItemType itemType, Server server);
}
