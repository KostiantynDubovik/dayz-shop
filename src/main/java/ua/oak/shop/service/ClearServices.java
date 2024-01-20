package ua.oak.shop.service;

import ua.oak.shop.jpa.entities.ItemType;
import ua.oak.shop.jpa.entities.Order;
import ua.oak.shop.jpa.entities.UserService;
import ua.oak.shop.repository.UserServiceRepository;
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
			userServiceRepository.delete(userService);
	}
}
