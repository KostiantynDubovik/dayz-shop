package ua.oak.shop.service;

import ua.oak.shop.repository.CategoryRepository;
import ua.oak.shop.repository.ItemRepository;
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

//	public Page<Item> findAllByCategory(String categoryName, Pageable pageable) {
//		Category category = categoryRepository.findByCategoryName(categoryName);
//		if (category != null) {
//			return itemRepository.findAllByCategoriesInAndBuyable(Collections.singletonList(category), true, pageable);
//		} else {
//			return Page.empty();
//		}
//	}
//
//	public Page<Item> findAllByCategoryNameAndStore(String categoryName, Store store, Pageable pageable) {
//		Category category = categoryRepository.findByCategoryName(categoryName);
//		if (category != null) {
//			return itemRepository.findAllByCategoriesInAndStoreAndBuyable(Collections.singletonList(category), store, true, pageable);
//		} else {
//			return Page.empty();
//		}
//	}
}
