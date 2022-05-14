package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
	Item findByName(String name);
	List<Item> findAllByCategoriesId(Long categoryId, Pageable pageable);
	Item findBySubItemsId(Long subItemId);
}
