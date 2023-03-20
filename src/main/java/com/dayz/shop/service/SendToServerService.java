package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.ItemType;
import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.jpa.entities.OrderItem;
import com.dayz.shop.json.MCodeArray;
import com.dayz.shop.json.Root;
import com.dayz.shop.utils.MCodeMapper;
import com.dayz.shop.utils.SFTPUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SendToServerService {
	private static final String CLASSNAME = SendToServerService.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

	public static final String PIPE = "|";
	public static final String SEMICOLON = ";";
	public static final String ZERO = "0";
	public static final String SETS_FILE = "CustomSpawnPlayerConfig.txt";
	public static final String VIP_FILE = "priority.txt";
	private static final int RETRY_COUNT = 3;
	final MCodeMapper mCodeMapper;

	@Autowired
	public SendToServerService(MCodeMapper mCodeMapper) {
		this.mCodeMapper = mCodeMapper;
	}

	public void sendOrder(Order order, Map<ItemType, Order> separatedTypes) throws JSchException, IOException, SftpException, InterruptedException {
		String steamId = order.getUser().getSteamId();
		try {
			for (ItemType itemType : separatedTypes.keySet()) {
				switch (itemType) {
					case ITEM:
					case VEHICLE:
						spawningItems(order, steamId, true);
						break;
					case VIP:
						vip(order, steamId, true);
						break;
					case SET:
						set(order, steamId, true);
				}
			}
		} catch (JSchException | SftpException | IOException e) {
			LOGGER.log(Level.SEVERE, "Cant send order", e);
			throw e;
		}
	}

	public void vip(Order order, String steamId, boolean add) throws JSchException, SftpException, InterruptedException {
		vip(order, steamId, add, 1);
	}

	public void vip(Order order, String steamId, boolean add, int retryNumber) throws JSchException, SftpException, InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		String pathToVip = SFTPUtils.getPathToVip(order);
		String completePath = String.format(pathToVip, order.getServer().getInstanceName(), VIP_FILE);
		try {
			ByteArrayInputStream fileContent = SFTPUtils.getFileContent(order, completePath);
			if (fileContent != null) {
				Scanner scanner = new Scanner(fileContent);
				Set<String> existingSteamIds = new HashSet<>();
				scanner.useDelimiter(SEMICOLON);
				while (scanner.hasNext()) {
					existingSteamIds.add(scanner.next());
				}
				if (add) {
					existingSteamIds.add(steamId);
				} else {
					existingSteamIds.remove(steamId);
				}
				ByteArrayInputStream contentStream = new ByteArrayInputStream(String.join(SEMICOLON, existingSteamIds).concat(SEMICOLON).getBytes(StandardCharsets.UTF_8));
				SFTPUtils.updateFile(order, completePath, contentStream);


				fileContent = SFTPUtils.getFileContent(order, completePath);
				scanner = new Scanner(fileContent);

				existingSteamIds = new HashSet<>();
				scanner.useDelimiter(SEMICOLON);
				while (scanner.hasNext()) {
					existingSteamIds.add(scanner.next());
				}
				if (!existingSteamIds.contains(steamId)) {
					throw new SftpException(-1, "no such item in file");
				}
			}
		} catch (JSchException | SftpException e) {
			if (retryNumber < RETRY_COUNT) {
				vip(order, steamId, add, retryNumber + 1);
			} else {
				logSendError(order, completePath);
				throw e;
			}
		}
	}

	public void set(Order order, String steamId, boolean add) throws IOException, JSchException, SftpException, InterruptedException {
		set(order, steamId, add, 1);
	}

	private void set(Order order, String steamId, boolean add, int retryNumber) throws IOException, JSchException, SftpException, InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		String pathToSet = SFTPUtils.getPathToSet(order);
		String completePath = String.format(pathToSet, order.getServer().getInstanceName(), SETS_FILE);
		try {
			InputStream existingSets = SFTPUtils.getFileContent(order, completePath);
			if (existingSets != null) {
				Map<String, String> setMap = IOUtils.readLines(existingSets, StandardCharsets.UTF_8).stream().collect(Collectors.toMap(input -> StringUtils.split(input, PIPE)[0], input -> input));
				if (add) {
					setMap.put(steamId, String.join(PIPE, steamId, ZERO, order.getOrderItems().get(0).getItem().getInGameId(), ZERO));
				} else {
					setMap.remove(steamId);
				}
				ByteArrayInputStream contentStream = new ByteArrayInputStream(String.join(System.lineSeparator(), setMap.values()).getBytes(StandardCharsets.UTF_8));
				SFTPUtils.updateFile(order, completePath, contentStream);

				existingSets = SFTPUtils.getFileContent(order, completePath);
				if (existingSets != null) {
					setMap = IOUtils.readLines(existingSets, StandardCharsets.UTF_8).stream().collect(Collectors.toMap(input -> StringUtils.split(input, PIPE)[0], input -> input));
					if (!setMap.containsKey(steamId)) {
						throw new SftpException(-1, "no such item in file");
					}
				}
			}
		} catch (JSchException | SftpException | IOException e) {
			if (retryNumber < RETRY_COUNT) {
				set(order, steamId, add, retryNumber + 1);
			} else {
				logSendError(order, completePath);
				throw e;
			}
		}
	}

	private void logSendError(Order order, String completePath) {
		List<Object> msgParams = new ArrayList<>();
		msgParams.add(order.getServer().getId());
		msgParams.add(completePath);
		msgParams.add(order.getOrderItems().stream().map(OrderItem::getId).collect(Collectors.toSet()));
		msgParams.add(order.getUser().getSteamId());
		LOGGER.log(Level.SEVERE, "Cannot write SET to server: {0}, with path {1}, using order items: {2}, for user steam id:{3}", msgParams.toArray());
	}

	public void spawningItems(Order order, String steamId, boolean add) throws IOException, JSchException, SftpException, InterruptedException {
		spawningItems(order, steamId, add, 1);
	}

	private void spawningItems(Order order, String steamId, boolean add, int retryNumber) throws IOException, JSchException, SftpException, InterruptedException {
		TimeUnit.SECONDS.sleep(3);
		String pathToJson = SFTPUtils.getPathToSpawningItemsJson(order);
		String completePath = String.format(pathToJson, order.getServer().getInstanceName(), steamId);
		try {
			InputStream existingItems = null;
			try {
				existingItems = SFTPUtils.getFileContent(order, completePath);
			} catch (JSchException | SftpException e) {
				LOGGER.info("There is no such file on server: " + order.getServer().getInstanceName() + ", will create for user: " + steamId);
			}
			Root root = new Root();
			ObjectMapper om = new ObjectMapper();
			om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			if (existingItems != null) {
				root = om.readValue(existingItems, Root.class);
			}
			List<MCodeArray> m_codeArray = mCodeMapper.mapOrderToRoot(order).getM_CodeArray();
			if (add) {
				root.getM_CodeArray().addAll(m_codeArray);
			} else {
				root.getM_CodeArray().removeAll(m_codeArray);
			}

			InputStream content = new ByteArrayInputStream(om.writerWithDefaultPrettyPrinter().writeValueAsString(root).getBytes(StandardCharsets.UTF_8));
			SFTPUtils.updateFile(order, completePath, content);
			try {
				existingItems = SFTPUtils.getFileContent(order, completePath);
			} catch (JSchException | SftpException e) {
				LOGGER.info("There is no such file on server: " + order.getServer().getInstanceName() + ", will create for user: " + steamId);
			}

			root = new Root();
			om = new ObjectMapper();
			if (existingItems != null) {
				root = om.readValue(existingItems, Root.class);
			}
			if (root.getM_CodeArray().stream().filter(mCodeArray -> {
				boolean result = false;
				for (MCodeArray codeArray : m_codeArray) {
					result = mCodeArray.getM_code().equals(codeArray.getM_code());
					if (result) {
						break;
					}
				}
				return result;
			}).collect(Collectors.toSet()).isEmpty()) {
				throw new SftpException(-1, "no such item in file");
			}

		} catch (JSchException | SftpException | IOException e) {
			if (retryNumber < RETRY_COUNT) {
				spawningItems(order, steamId, add, retryNumber + 1);
			} else {
				logSendError(order, completePath);
				throw e;
			}
		}
	}
}
