package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Server;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/server")
public class ServerController {
	private final ServerService serverService;

	@Autowired
	public ServerController(ServerService serverService) {
		this.serverService = serverService;
	}

	@GetMapping("list")
	public List<Server> getServers(@RequestAttribute Store store) {
		return serverService.getAllByStore(store);
	}

	@GetMapping("revenuenoconstantine")
	@PreAuthorize("hasAnyAuthority('STORE_WRITE')")
	public Map<String, String> getServersRevenueNoConstantine(@RequestAttribute Store store, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return serverService.getServersRevenueNoConstantine(store.getId(), from, to);
	}

	@GetMapping("revenue")
	@PreAuthorize("hasAnyAuthority('STORE_WRITE')")
	public Map<String, String> getServersRevenue(@RequestAttribute Store store, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return serverService.getServersRevenue(store.getId(), from, to);
	}
}
