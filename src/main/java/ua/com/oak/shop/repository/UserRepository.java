package ua.com.oak.shop.repository;

import ua.com.oak.shop.jpa.entities.Role;
import ua.com.oak.shop.jpa.entities.Store;
import ua.com.oak.shop.jpa.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User getBySteamId(String steamId);
	User getBySteamIdAndStore(String steamId, Store store);
	List<User> getAllByStore(Store store);

	User getBySteamIdAndRolesIn(String steamId, List<Role> roles);
}
