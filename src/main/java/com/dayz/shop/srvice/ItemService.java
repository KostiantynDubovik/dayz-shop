package com.dayz.shop.srvice;

import com.dayz.shop.jpa.entities.Category;
import com.dayz.shop.jpa.entities.Item;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.CategoryRepository;
import com.dayz.shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;

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

	public Page<Item> findAllByCategory(String categoryName, Pageable pageable) {
		Category category = categoryRepository.findByCategoryName(categoryName);
		if (category != null) {
			return itemRepository.findAllByCategoriesIn(Collections.singletonList(Collections.singletonList(category)), pageable);
		} else {
			return Page.empty();
		}
	}

	public Page<Item> findAllByCategoryNameAndStore(String categoryName, Store store, Pageable pageable) {
		Category category = categoryRepository.findByCategoryName(categoryName);
		if (category != null) {
			return itemRepository.findAllByCategoriesInAndStore(Collections.singletonList(Collections.singletonList(category)), store, pageable);
		} else {
			return Page.empty();
		}
	}
}
