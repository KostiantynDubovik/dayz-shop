package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Category;
import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
	Item findByName(String name);
	List<Item> findAllByCategoriesId(Long categoryId, Pageable pageable);
	Item findBySubItemsId(Long subItemId);

	Item findByIdAndStore(Long itemId, Store store);

//	Page<Item> findAllByCategoriesInAndStoreAndBuyable(Collection<Category> categories, Store store, boolean buyable, Pageable pageable);

//	Page<Item> findAllByCategoriesInAndBuyable(Collection<Category> categories, boolean buyable, Pageable pageable);

	@Query(value = "SELECT I FROM Item I where I.servers is not empty AND (I.store = :store OR I.store.parentStore = :store) AND :category member I.categories",
			countQuery = "SELECT COUNT(I) FROM Item I where I.servers is not empty AND (I.store = :store OR I.store.parentStore = :store) AND :category member I.categories")
	Page<Item> findAllByStoreAndBuyableAndCategory(Store store, Category category, Pageable pageable);
}
