package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Role;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User getBySteamId(String steamId);
	User getBySteamIdAndStore(String steamId, Store store);
	List<User> getAllByStore(Store store);

	User getBySteamIdAndRolesIn(String steamId, List<Role> roles);

	@Query(value = "SELECT u.STEAM_ID, u.STEAM_NICKNAME FROM payments p JOIN users u on u.USER_ID = p.USER_ID where p.USER_FROM = :userFromId AND p.USER_ID != :userFromId AND p.PAYMENT_STATUS = 'COMPLETE' and p.PAYMENT_TYPE='TRANSFER' AND p.STORE_ID = :storeId group by (p.USER_ID) order by count(p.USER_ID) desc limit :limit", nativeQuery = true)
	List<List<String>> findFriends(Long userFromId, Long storeId, int limit);
}
