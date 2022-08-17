package com.dayz.shop.handlers;

import com.dayz.shop.jpa.entities.Server;
import com.dayz.shop.jpa.entities.Store;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/server")
public class ServerController {
	@GetMapping("list")
	public List<Server> getServers(@RequestAttribute Store store) {
		return store.getServers();
	}
}
