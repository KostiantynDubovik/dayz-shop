package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.Server;
import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServerService {
	private final ServerRepository serverRepository;

	@Autowired
	public ServerService(ServerRepository serverRepository) {
		this.serverRepository = serverRepository;
	}

	public List<Server> getAllByStore(Store store) {
		return serverRepository.findAllByStore(store);
	}

	public Map<String, String> getServersRevenue(Long id, LocalDate from, LocalDate to) {
		List<List<String>> resultSet = serverRepository.getServersRevenue(id, from, to);

		return resultSet.stream()
				.map(input -> new AbstractMap.SimpleEntry<>(input.get(0), input.get(1)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
	}

	public Map<String, String> getServersRevenueNoConstantine(Long id, LocalDate from, LocalDate to) {
		List<List<String>> resultSet = serverRepository.getServersRevenueNoConstantine(id, from, to);

		return resultSet.stream()
				.map(input -> new AbstractMap.SimpleEntry<>(input.get(0), input.get(1)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
	}
}
