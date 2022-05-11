package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
	Item findByName(String name);
	List<Item> findAllByCategoriesId(Long categoryId);
	Item findBySubItemsId(Long subItemId);
}
