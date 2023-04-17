package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Category;
import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

	Item findByIdAndStore(Long itemId, Store store);

	@Query(value = "SELECT I FROM Item I where I.servers is not empty AND (I.store = :store OR I.store.parentStore = :store) AND :category member I.categories",
			countQuery = "SELECT COUNT(I) FROM Item I where I.servers is not empty AND (I.store = :store OR I.store.parentStore = :store) AND :category member I.categories")
	Page<Item> findAllByStoreAndBuyableAndCategory(Store store, Category category, Pageable pageable);

	@Query(value = "select d.ENTITY_NAME, SUM(oi.TOTAL_PRICE) from order_items oi join ITEMS i on I.ITEM_ID = oi.ITEM_ID join DESCRIPTION d on i.DESCRIPTION_ID = d.DESCRIPTION_ID  where oi.BOUGHT_TIME > :from and oi.BOUGHT_TIME < :to and oi.STORE_ID = :storeId group by oi.ITEM_ID order by SUM(oi.TOTAL_PRICE) desc", nativeQuery = true)
	List<List<String>> getItemRevenue(Long storeId, LocalDate from, LocalDate to);

	@Query(value = "select s.SERVER_NAME, d.ENTITY_NAME, SUM(oi.TOTAL_PRICE) from order_items oi join ITEMS i on I.ITEM_ID = oi.ITEM_ID join DESCRIPTION d on i.DESCRIPTION_ID = d.DESCRIPTION_ID join servers s on oi.SERVER_ID = s.SERVER_ID where oi.BOUGHT_TIME > :from and oi.BOUGHT_TIME < :to and oi.STORE_ID = :storeId group by oi.SERVER_ID, oi.ITEM_ID order by oi.SERVER_ID, SUM(oi.TOTAL_PRICE) desc", nativeQuery = true)
	List<List<String>> getItemRevenuePerServer(Long storeId, LocalDate from, LocalDate to);
}
