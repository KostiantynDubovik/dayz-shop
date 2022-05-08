package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
