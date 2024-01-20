package ua.com.oak.shop;


import ua.com.oak.shop.jpa.entities.Item;
import ua.com.oak.shop.jpa.entities.ItemType;
import ua.com.oak.shop.jpa.entities.Store;
import ua.com.oak.shop.repository.ItemRepository;
import ua.com.oak.shop.repository.StoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class Insert {
	private final ItemRepository itemRepository;
	private final StoreRepository storeRepository;

	@Autowired
	public Insert(ItemRepository itemRepository, StoreRepository storeRepository) {
		this.itemRepository = itemRepository;
		this.storeRepository = storeRepository;
	}

	@Test
	public void insert() throws Exception {

		File processedTypesJson = new File("C:\\Users\\red_b\\AppData\\Roaming\\JetBrains\\IntelliJIdea2021.2\\scratches\\processedTypes.json");
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, String>> values = new ArrayList<>();
		values = objectMapper.readValue(processedTypesJson, values.getClass());

		Store store = storeRepository.getById(-1L);
		List<Item> items = new ArrayList<>();
		for (Map<String, String> value : values) {
			Item item = new Item();
			item.setItemType(defineType(value));
			item.setStore(store);
			item.setCount(1);
			item.setInGameId(value.get("name"));
			item.setName(value.get("name"));
			items.add(item);
		}
		itemRepository.saveAll(items);
	}

	private ItemType defineType(Map<String, String> value) {
		String type = value.get("category");
		return type.startsWith("vehicle") ? ItemType.VEHICLE : ItemType.ITEM;
	}
}
