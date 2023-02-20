package com.dayz.shop.service;

import com.dayz.shop.repository.CategoryRepository;
import com.dayz.shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	public Map<String, String> getItemRevenue(Long storeId, LocalDate from, LocalDate to) {
		List<List<String>> resultSet = itemRepository.getItemRevenue(storeId, from, to);

		return resultSet.stream()
				.map(input -> new AbstractMap.SimpleEntry<>(input.get(0), input.get(1)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
	}
}
