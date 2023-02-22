package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserServiceRepository extends JpaRepository<UserService, Long> {

	List<UserService> findAllByEndDateIsBefore(LocalDateTime endDate);

	@Query(value = "select * from user_services US join servers S on US.SERVER_ID = S.SERVER_ID where S.STORE_ID = :storeId and US.END_DATE > NOW()", nativeQuery = true)
	List<UserService> findAllByStoreIdAndEndDateInFuture(Long storeId);
	UserService findByUserAndItemTypeAndServer(User user, ItemType itemType, Server server);

	@Modifying
	@Query(value = "delete FROM user_services US where US.USER_ID = :userId AND US.ITEM_TYPE = :itemType AND US.SERVER_ID = :serverId", nativeQuery = true)
	void deleteUserServiceByUserAndItemTypeAndServer(Long userId, String itemType, Long serverId);

	void deleteAllByEndDateIsBefore(LocalDateTime now);
}
