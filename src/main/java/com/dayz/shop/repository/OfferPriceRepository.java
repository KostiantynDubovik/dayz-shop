package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.OfferPrice;
import com.dayz.shop.jpa.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferPriceRepository extends JpaRepository<OfferPrice, Long> {



	@Query("SELECT OP from OfferPrice OP where OP.store = :store and OP.item = :item and CURRENT_TIME between OP.startTime AND OP.endTime ORDER BY OP.priority DESC ")
	List<OfferPrice> findAllActiveOfferPrices(Store store, Item item);

	List<OfferPrice> findAllByStoreAndItemId(Store store, Long itemId);

	void deleteByStoreAndId(Store store, long offerPriceId);
}
