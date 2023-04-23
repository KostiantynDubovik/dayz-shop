package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Category;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

	private final CategoryRepository categoryRepository;

	@Autowired
	public CategoryController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@GetMapping("all")
	public List<Category> getAllCategories(@RequestAttribute(name = "store") Store store) {
		return categoryRepository.findAllByStoreAndVisible(store, true);
	}

	@GetMapping("by/parent/{categoryName}")
	public List<Category> getByParentCategories(@PathVariable String categoryName, @RequestAttribute(name = "store") Store store) {
		return categoryRepository.findByCategoryNameAndStore(categoryName, store).getChildCategories();
	}

	@GetMapping("with/parent/{categoryName}")
	public List<Category> getWithParentCategories(@PathVariable String categoryName, @RequestAttribute(name = "store") Store store) {
		Category byCategoryNameAndStore = categoryRepository.findByCategoryNameAndStore(categoryName, store);
		byCategoryNameAndStore.getChildCategories().add(0, byCategoryNameAndStore);
		return byCategoryNameAndStore.getChildCategories();
	}
}
