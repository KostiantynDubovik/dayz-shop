package com.dayz.shop.handlers;

import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.repository.ItemRepository;
import com.dayz.shop.srvice.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
public class ItemController {

	private final ItemRepository itemRepository;
	private final ItemService itemService;


	@Autowired
	public ItemController(ItemRepository itemRepository, ItemService itemService) {
		this.itemRepository = itemRepository;
		this.itemService = itemService;
	}

	@GetMapping("/items/id/{itemId}")
	public Optional<Item> getItem(@PathVariable Long itemId, Principal principal) {
		return itemRepository.findById(itemId);
	}

	@GetMapping("/items/{page}")
	public Page<Item> getAllItems(@PathVariable int page, @RequestParam(defaultValue = "name") String sortBy, @RequestParam(defaultValue = "9") int pageSize, @Autowired Principal principal) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortBy));
		return itemRepository.findAll(pageable);
	}

	@DeleteMapping("/items/delete/{itemId}")
	public void deleteItem(@PathVariable Long itemId) {
		itemRepository.deleteById(itemId);
	}

	@PostMapping("/items/create")
	public Optional<Item> createItem(@RequestBody Item item, Principal principal) {
		return itemService.createItem(item, principal);
	}
}
