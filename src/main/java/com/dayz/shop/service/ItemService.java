package com.dayz.shop.service;

import com.dayz.shop.repository.ItemRepository;
import com.dayz.shop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

	private final ItemRepository itemRepository;

	@Autowired
	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public void deleteById(Long itemId) {
		itemRepository.deleteById(itemId);
	}

	public Map<String, String> getItemRevenue(Long storeId, LocalDate from, LocalDate to) {
		List<List<String>> resultSet = itemRepository.getItemRevenue(storeId, from, to);

		return Utils.transformResultSetToMap(resultSet);
	}

	public Map<String, String> getItemRevenuePerServer(Long storeId, LocalDate from, LocalDate to) {
		List<List<String>> resultSet = itemRepository.getItemRevenuePerServer(storeId, from, to);

		return Utils.transformResultSetToMap2(resultSet);
	}
}
