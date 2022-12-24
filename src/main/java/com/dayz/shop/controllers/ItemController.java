package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.ListPrice;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.CategoryRepository;
import com.dayz.shop.repository.ItemRepository;
import com.dayz.shop.repository.ListPriceRepository;
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
	private final ListPriceRepository listPriceRepository;

	@Autowired
	public ItemController(ItemRepository itemRepository, CategoryRepository categoryRepository, ListPriceRepository listPriceRepository) {
		this.itemRepository = itemRepository;
		this.categoryRepository = categoryRepository;
		this.listPriceRepository = listPriceRepository;
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

	@PostMapping()
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public Item createItem(@RequestBody Item item, @RequestParam BigDecimal listPrice, @RequestAttribute Store store) {
		item.setStore(store);
		ListPrice price = new ListPrice();
		price.setPrice(listPrice);
		price.setStore(store);
		price.setItem(item);
		price.setCurrency("RUB");
		item.setListPrice(listPriceRepository.save(price));
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
