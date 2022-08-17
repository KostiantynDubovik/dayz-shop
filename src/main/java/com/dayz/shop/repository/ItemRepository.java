package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Category;
import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
	Item findByName(String name);
	List<Item> findAllByCategoriesId(Long categoryId, Pageable pageable);
	Item findBySubItemsId(Long subItemId);

	Item findByIdAndStore(Long itemId, Store store);

	Page<Item> findAllByCategoriesInAndStoreAndBuyable(Collection<Category> categories, Store store, boolean buyable, Pageable pageable);
	Page<Item> findAllByCategoriesInAndBuyable(Collection<Category> categories, boolean buyable, Pageable pageable);

	Page<Item> findAllByStoreAndBuyable(Store store, Pageable pageable, boolean buyable);
}
