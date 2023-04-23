package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.repository.CategoryRepository;
import com.dayz.shop.repository.DescriptionRepository;
import com.dayz.shop.repository.ItemRepository;
import com.dayz.shop.repository.LanguageRepository;
import com.dayz.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private final ItemService itemService;
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final LanguageRepository languageRepository;
	private final DescriptionRepository descriptionRepository;

	@Autowired
	public ItemController(ItemService itemService, ItemRepository itemRepository, CategoryRepository categoryRepository,
	                      LanguageRepository languageRepository, DescriptionRepository descriptionRepository) {
		this.itemService = itemService;
		this.itemRepository = itemRepository;
		this.categoryRepository = categoryRepository;
		this.languageRepository = languageRepository;
		this.descriptionRepository = descriptionRepository;
	}

	@GetMapping("{itemId}")
	public Item getItem(@PathVariable Long itemId, @RequestAttribute Store store) {
		return itemRepository.findByIdAndStore(itemId, store);
	}

	@GetMapping("{categoryName}/{page}")
	public Page<Item> getByCategory(@PathVariable("categoryName") String categoryName,
	                                @PathVariable int page,
	                                @RequestParam(defaultValue = "sequence") String sortBy,
	                                @RequestParam(defaultValue = "100") int pageSize,
	                                @RequestAttribute Store store) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, pageSize < 3 ? 3 : pageSize, Sort.by(sortBy));
		Page<Item> allByStoreAndBuyableAndCategory = itemRepository.findAllByStoreAndBuyableAndCategory(store, categoryRepository.findByCategoryNameAndStore(categoryName, store), pageable);
		LocalDateTime now = LocalDateTime.now();
		allByStoreAndBuyableAndCategory.getContent().forEach(item -> item.setOfferPrices(item.getOfferPrices().stream().filter(offerPrice -> offerPrice.getStartTime().isBefore(now) && offerPrice.getEndTime().isAfter(now)).collect(Collectors.toList())));
		return allByStoreAndBuyableAndCategory;
	}

	@PostMapping()
	@Consumes("*/*")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public Item createItem(@RequestBody Item item, @RequestParam BigDecimal listPrice, @RequestAttribute Store store) {
		item.setStore(store);
		Description description = item.getDescription();
		if (description != null) {
			description.setStore(store);
			if (description.getLanguage() == null) {
				description.setLanguage(languageRepository.getById(store.getLong("language.default")));
			}
			descriptionRepository.save(description);
		}
		item.setDescription(description);
		ListPrice price = new ListPrice();
		price.setPrice(listPrice);
		price.setStore(store);
		price.setItem(item);
		price.setCurrency("RUB");
		item.setListPrice(price);
		if (item.getImageUrl() == null) {
			item.setImageUrl(store.getString("default.image"));
		}
		return itemRepository.save(item);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public Item updateItem(@RequestBody Item item) {
		return itemRepository.save(item);
	}

	@DeleteMapping("{item}")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public void deleteItem(@PathVariable Item item) {
		itemRepository.delete(item);
	}

	@GetMapping("revenue")
	@PreAuthorize("hasAnyAuthority('STORE_WRITE')")
	public Map<String, String> getServersRevenue(@RequestAttribute Store store, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return itemService.getItemRevenue(store.getId(), from, to);
	}

	@GetMapping("revenue/per/server")
	@PreAuthorize("hasAnyAuthority('STORE_WRITE')")
	public Map<String, String> getServersRevenuePerServer(@RequestAttribute Store store, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return itemService.getItemRevenuePerServer(store.getId(), from, to);
	}
}
