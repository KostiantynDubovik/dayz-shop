package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.ListPrice;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.CategoryRepository;
import com.dayz.shop.repository.ItemRepository;
import com.dayz.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final ItemService itemService;

	@Autowired
	public ItemController(ItemRepository itemRepository, ItemService itemService, CategoryRepository categoryRepository) {
		this.itemRepository = itemRepository;
		this.itemService = itemService;
		this.categoryRepository = categoryRepository;
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
		return itemRepository.findAllByStoreAndBuyableAndCategory(store, categoryRepository.findByCategoryName(categoryName), pageable);
	}
//
//	@GetMapping("app/{categoryName}/{page}")
//	public Page<Item> getAllItemsByCategoryAcrossStores(
//			@PathVariable int page,
//			@PathVariable String categoryName,
//			@RequestParam(defaultValue = "name") String sortBy,
//			@RequestParam(defaultValue = "9") int pageSize) {
//		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortBy));
//		return itemService.findAllByCategory(categoryName, pageable);
//	}

	@PostMapping()
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public Item createItem(@RequestBody Item item, @RequestParam BigDecimal listPriceValue, @RequestAttribute Store store) {
		item.setStore(store);
		ListPrice listPrice = new ListPrice();
		listPrice.setPrice(listPriceValue);
		listPrice.setStore(store);
		listPrice.setItem(item);
		listPrice.setCurrency("RUB");
		item.setListPrice(listPrice);
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
}
