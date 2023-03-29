package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.jpa.entities.UserService;
import com.dayz.shop.repository.UserServiceRepository;
import com.dayz.shop.utils.OrderUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ClearServices {
	private static final java.util.logging.Logger LOGGER = Logger.getLogger(ClearServices.class.getName());

	private final UserServiceRepository userServiceRepository;
	private final SendToServerService sendToServerService;


	@Autowired
	public ClearServices(UserServiceRepository userServiceRepository, SendToServerService sendToServerService) {
		this.userServiceRepository = userServiceRepository;
		this.sendToServerService = sendToServerService;
	}

	@Scheduled(cron = "0 0 */3 * * *")
	public void clearAll() throws JSchException, SftpException, IOException, InterruptedException {
		List<UserService> userServices = userServiceRepository.findAllByEndDateIsBefore(LocalDateTime.now());

		ObjectMapper objectMapper = new ObjectMapper();
		String values = objectMapper.writeValueAsString(userServices.stream().map((Function<UserService, Map.Entry<String, String>>) userService -> new AbstractMap.SimpleEntry<>(userService.getUser().getSteamId().concat("-").concat(userService.getItemType().name()), userService.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss")))));
		Object[] msgParams = {LocalDateTime.now(), values};
		LOGGER.log(Level.WARNING, "Current date: {0}, services going to delete: {1}", msgParams);
		for (UserService userService : userServices) {
			clear(userService);
		}
	}

	public void clear(UserService userService) throws JSchException, SftpException, IOException, InterruptedException {
		Order order = userService.getOrder();
		ItemType itemType = userService.getItemType();
		String steamId = order.getUser().getSteamId();
		switch (itemType) {
			case VIP:
				sendToServerService.vip(order, steamId, false);
				break;
			case SET:
				sendToServerService.set(OrderUtils.getItemsByType(order, ItemType.SET), steamId, false);
		}
		userServiceRepository.delete(userService);
	}
}
