package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.jpa.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	@Query("SELECT S from Store S join Order O on O.store = S where O.id = :orderId")
	Store findByOrder(Long orderId);
}
