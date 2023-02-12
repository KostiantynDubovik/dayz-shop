package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.*;
import com.dayz.shop.jpa.entities.UserService;
import com.dayz.shop.repository.UserServiceRepository;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClearServices {

	private final UserServiceRepository userServiceRepository;
	private final SendToServerService sendToServerService;


	@Autowired
	public ClearServices(UserServiceRepository userServiceRepository, SendToServerService sendToServerService) {
		this.userServiceRepository = userServiceRepository;
		this.sendToServerService = sendToServerService;
	}

	@Scheduled(cron = "0 0 */3 * * *")
	public void clearAll() throws JSchException, SftpException, IOException {
		List<UserService> userServices = userServiceRepository.findAllByEndDateIsBefore(LocalDateTime.now());
		for (UserService userService : userServices) {
			clear(userService);
		}
	}

	public void synchronizeServices(Store store) throws JSchException, SftpException, IOException {
		List<UserService> userServices = userServiceRepository.findAllByStoreIdAndEndDateInFuture(store.getId());
		Map<Server, List<UserService>> splitByServer = userServices.stream().collect(Collectors.groupingBy(UserService::getServer, Collectors.toCollection(ArrayList::new)));
		for (Map.Entry<Server, List<UserService>> userServicesByServer : splitByServer.entrySet()) {
			Map<ItemType, List<UserService>> splitByType = userServicesByServer.getValue().stream().collect(Collectors.groupingBy(UserService::getItemType));
			for (Map.Entry<ItemType, List<UserService>> userServicesByType : splitByType.entrySet()) {
				batchSync(userServicesByType.getKey(), userServicesByServer.getKey(), userServicesByType.getValue());
			}
		}
	}

	public void clear(UserService userService) throws JSchException, SftpException, IOException {
		Order order = userService.getOrder();
		ItemType itemType = userService.getItemType();
		String steamId = order.getUser().getSteamId();
		switch (itemType) {
			case VIP:
				sendToServerService.vip(order, steamId, false);
				break;
			case SET:
				sendToServerService.set(order, steamId, false);
		}
		userServiceRepository.deleteUserServiceByUserAndItemTypeAndServer(userService.getUser().getId(), userService.getItemType().name(), userService.getServer().getId());
	}

	public void batchSync(ItemType itemType, Server server, List<UserService> userServices) throws JSchException, SftpException, IOException {
		switch (itemType) {
			case VIP:
				sendToServerService.batchVip(server, userServices);
				break;
			case SET:
				sendToServerService.batchSet(server, userServices);
		}
	}
}
