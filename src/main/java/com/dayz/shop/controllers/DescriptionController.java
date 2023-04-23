package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Description;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/description")
public class DescriptionController {

	@GetMapping("/{descriptionId}")
	public Description getSelf(@PathVariable("descriptionId") Description description) {
		return description;
	}
}
