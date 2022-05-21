package com.dayz.shop.handlers;

import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.ItemRepository;
import com.dayz.shop.srvice.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class ItemController {

	private final ItemRepository itemRepository;
	private final ItemService itemService;

	@Autowired
	public ItemController(ItemRepository itemRepository, ItemService itemService) {
		this.itemRepository = itemRepository;
		this.itemService = itemService;
	}

	@GetMapping("{itemId}")
	public Item getItem(@PathVariable Long itemId, @RequestAttribute Store store) {
		return itemRepository.findByIdAndStore(itemId, store);
	}

	@GetMapping("list/{page}")
	public Page<Item> getAllItems(
			@PathVariable int page,
			@RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "9") int pageSize,
			@RequestAttribute Store store) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, pageSize < 3 ? 3 : pageSize, Sort.by(sortBy));
		return itemRepository.findAllByStore(store, pageable);
	}

	@GetMapping("{categoryName}/{page}")
	public Page<Item> getAllItemsByCategory(
			@PathVariable int page,
			@PathVariable String categoryName,
			@RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "9") int pageSize,
			@RequestAttribute Store store) {
		Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, pageSize < 3 ? 3 : pageSize, Sort.by(sortBy));
		return itemService.findAllByCategoryNameAndStore(categoryName, store, pageable);
	}

	@GetMapping("app/{categoryName}/{page}")
	@PreAuthorize("hasAuthority('APP_WRITE')")
	public Page<Item> getAllItemsByCategoryAcrossStores(
			@PathVariable int page,
			@PathVariable String categoryName,
			@RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "9") int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortBy));
		return itemService.findAllByCategory(categoryName, pageable);
	}

	@PostMapping()
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public Item createItem(@RequestBody Item item, @RequestAttribute Store store) {
		item.setStore(store);
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
