package com.dayz.shop.service;

import com.dayz.shop.repository.CategoryRepository;
import com.dayz.shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;

	@Autowired
	public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository) {
		this.itemRepository = itemRepository;
		this.categoryRepository = categoryRepository;
	}

	public void deleteById(Long itemId) {
		itemRepository.deleteById(itemId);
	}

}
