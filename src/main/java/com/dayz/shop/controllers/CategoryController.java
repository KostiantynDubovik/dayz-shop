package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Category;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
