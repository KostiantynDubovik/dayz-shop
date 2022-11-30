package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Category;
import com.dayz.shop.repository.CategoryRepository;
import com.dayz.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

	private final CategoryRepository categoryRepository;
//	private final CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryRepository categoryRepository, ItemService itemService) {
		this.categoryRepository = categoryRepository;
//		this.itemService = itemService;
	}

	@GetMapping("all")
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}
}
