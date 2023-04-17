package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Category;
import com.dayz.shop.jpa.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByCategoryNameAndStore(String categoryName, Store store);

	List<Category> findAllByStoreAndVisible(Store store, boolean visible);
}
