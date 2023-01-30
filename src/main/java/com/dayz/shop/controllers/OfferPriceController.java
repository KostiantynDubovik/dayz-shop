package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.OfferPrice;
import com.dayz.shop.jpa.entities.OfferPriceType;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.OfferPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/offer")
public class OfferPriceController {

	private final OfferPriceRepository offerPriceRepository;

	@Autowired
	public OfferPriceController(OfferPriceRepository offerPriceRepository) {
		this.offerPriceRepository = offerPriceRepository;
	}

	@GetMapping("all/{itemId}")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public List<OfferPrice> getAllItemOffers(@RequestAttribute Store store, @PathVariable long itemId) {
		return offerPriceRepository.findAllByStoreAndItemId(store, itemId);
	}

	@PostMapping("all/{itemId}")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public OfferPrice addOfferPrice(@RequestBody OfferPrice offerPrice, @RequestAttribute Store store, @PathVariable(value = "itemId") Item item) {
		offerPrice.setStore(store);
		offerPrice.setItem(item);
		if (priceCondition(offerPrice)) {
			return offerPriceRepository.save(offerPrice);
		} else {
			return null;
		}
	}

	private static boolean priceCondition(OfferPrice offerPrice) {
		return absoluteCondition(offerPrice) || percentageCondition(offerPrice);
	}

	private static boolean percentageCondition(OfferPrice offerPrice) {
		BigDecimal price = offerPrice.getPrice();
		return offerPrice.getOfferPriceType().equals(OfferPriceType.PERCENTAGE) && price.compareTo(BigDecimal.valueOf(100)) < 0
				&& price.compareTo(BigDecimal.ONE) > 0;
	}

	private static boolean absoluteCondition(OfferPrice offerPrice) {
		BigDecimal price = offerPrice.getPrice();
		Item item = offerPrice.getItem();
		return offerPrice.getOfferPriceType().equals(OfferPriceType.ABSOLUTE) && item.getListPrice().getPrice().compareTo(price) < 0
				&& price.compareTo(BigDecimal.ZERO) > 0;
	}

	@PutMapping("all/{offerPriceId}")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public OfferPrice updateOfferPrice(@RequestBody OfferPrice offerPrice, @RequestAttribute Store store, @PathVariable long offerPriceId) {
		offerPrice.setId(offerPriceId);
		offerPrice.setStore(store);
		if (priceCondition(offerPrice)) {
			return offerPriceRepository.save(offerPrice);
		} else {
			return null;
		}
	}

	@DeleteMapping("all/{offerPriceId}")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public void deleteByStoreAndId(@RequestAttribute Store store, @PathVariable long offerPriceId) {
		offerPriceRepository.deleteByStoreAndId(store, offerPriceId);
	}
}
