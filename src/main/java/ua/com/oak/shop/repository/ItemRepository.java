package ua.com.oak.shop.repository;

import ua.com.oak.shop.jpa.entities.Category;
import ua.com.oak.shop.jpa.entities.Item;
import ua.com.oak.shop.jpa.entities.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

	Item findByIdAndStore(Long itemId, Store store);

	@Query(value = "SELECT I FROM Item I where I.servers is not empty AND (I.store = :store OR I.store.parentStore = :store) AND :category member I.categories",
			countQuery = "SELECT COUNT(I) FROM Item I where I.servers is not empty AND (I.store = :store OR I.store.parentStore = :store) AND :category member I.categories")
	Page<Item> findAllByStoreAndBuyableAndCategory(Store store, Category category, Pageable pageable);
}
