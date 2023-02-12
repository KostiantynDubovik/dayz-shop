package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.ItemType;
import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.jpa.entities.UserService;
import com.dayz.shop.repository.UserServiceRepository;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClearServices {
	private static final int RETRY_COUNT = 3;

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
			clear(userService, 1);
		}
	}

	public void clear(UserService userService, int retryNumber) throws JSchException, SftpException, IOException {
		try {
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
			userServiceRepository.delete(userService);
		} catch (JSchException | SftpException | IOException e) {
			if (retryNumber < RETRY_COUNT) {
				clear(userService, retryNumber + 1);
			} else {
				throw e;
			}
		}
	}
}
