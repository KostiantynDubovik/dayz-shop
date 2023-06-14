package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.*;
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

	public void sendOrder(Order order, Map<ItemType, List<OrderItem>> separatedTypes) throws JSchException, IOException, SftpException, InterruptedException {
		String steamId = order.getUserTo().getSteamId();
		try {
			for (ItemType itemType : separatedTypes.keySet()) {
				switch (itemType) {
					case ITEM:
					case VEHICLE:
						spawningItems(separatedTypes.get(itemType), true);
						break;
					case VIP:
						vip(order, steamId, true);
						break;
					case SET:
						set(separatedTypes.get(itemType), steamId, true);
						break;
					case CUSTOM_SET_ITEM:
						customSet(separatedTypes.get(itemType), separatedTypes.get(ItemType.CUSTOM_SET), steamId, true);
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
				if (add && !existingSteamIds.contains(steamId)) {
					throw new SftpException(-1, "no such item in file");
				}
			}
		} catch (JSchException | SftpException e) {
			if (retryNumber < RETRY_COUNT) {
				TimeUnit.SECONDS.sleep(1);
				vip(order, steamId, add, retryNumber + 1);
			} else {
				logSendError(order, completePath);
				throw e;
			}
		}
	}

	public void set(List<OrderItem> orderItems, String steamId, boolean add) throws IOException, JSchException, SftpException, InterruptedException {
		set(orderItems, steamId, add, 1);
	}

	private void set(List<OrderItem> orderItems, String steamId, boolean add, int retryNumber) throws IOException, JSchException, SftpException, InterruptedException {
		if (!orderItems.isEmpty()) {
			OrderItem orderItem = orderItems.stream().findFirst().get();
			Order order = orderItem.getOrder();
			String pathToSet = SFTPUtils.getPathToSet(order);
			String completePath = String.format(pathToSet, order.getServer().getInstanceName(), SETS_FILE);
			try {
				InputStream existingSets = SFTPUtils.getFileContent(order, completePath);
				if (existingSets != null) {
					Map<String, String> setMap = IOUtils.readLines(existingSets, StandardCharsets.UTF_8).stream().collect(Collectors.toMap(input -> StringUtils.split(input, PIPE)[0], input -> input, (s, s2) -> s2, LinkedHashMap::new));
					if (add) {
						setMap.put(steamId, String.join(PIPE, steamId, ZERO, orderItem.getItem().getInGameId(), ZERO));
					} else {
						setMap.remove(steamId);
					}
					ByteArrayInputStream contentStream = new ByteArrayInputStream(String.join(System.lineSeparator(), setMap.values()).getBytes(StandardCharsets.UTF_8));
					SFTPUtils.updateFile(order, completePath, contentStream);

					existingSets = SFTPUtils.getFileContent(order, completePath);
					if (existingSets != null) {
						setMap = IOUtils.readLines(existingSets, StandardCharsets.UTF_8).stream().collect(Collectors.toMap(input -> StringUtils.split(input, PIPE)[0], input -> input));
						if (add && !setMap.containsKey(steamId)) {
							throw new SftpException(-1, "no such item in file");
						}
					}
				}
			} catch (JSchException | SftpException | IOException e) {
				if (retryNumber < RETRY_COUNT) {
					TimeUnit.SECONDS.sleep(1);
					set(orderItems, steamId, add, retryNumber + 1);
				} else {
					logSendError(order, completePath);
					throw e;
				}
			}
		}
	}

	public void customSet(List<OrderItem> orderItems, List<OrderItem> customSetItems, String steamId, boolean add) throws JSchException, SftpException, IOException, InterruptedException {
		if (!orderItems.isEmpty()) {
			OrderItem orderItem = orderItems.stream().findFirst().get();
			OrderItem customSetItem = customSetItems.stream().findFirst().get();
			Order order = orderItem.getOrder();
			String filename = steamId.concat(".txt");
			String content = orderItems.stream().map(orderItem1 -> orderItem1.getItem().getInGameId()).collect(Collectors.joining(PIPE));
			for (Server server : customSetItem.getItem().getServers()) {
				List<OrderItem> stubOrderItems = getStubOrderItems(order, server);
				set(stubOrderItems, steamId, add);
				customSetContent(add, order, filename, content, server, 1);
			}
		}
	}

	private void customSetContent(boolean add, Order order, String filename, String content, Server server, int retryNumber)
			throws JSchException, SftpException, IOException, InterruptedException {
		String pathToCustomSet = SFTPUtils.getPathToCustomSet(server);
		String setContentPath = String.format(pathToCustomSet, server.getInstanceName(), filename);
		try {
			content = add ? content : StringUtils.EMPTY;
			SFTPUtils.updateFile(server, setContentPath, new ByteArrayInputStream(content.getBytes()));
			InputStream existingSet = SFTPUtils.getFileContent(server, setContentPath);
			if (existingSet != null) {
				String fileContent = IOUtils.readLines(existingSet, StandardCharsets.UTF_8).get(0);
				if (add && !fileContent.equals(content)) {
					throw new SftpException(-1, "no such item in file");
				}
			}
		} catch (JSchException | SftpException | IOException e) {
			if (retryNumber < RETRY_COUNT) {
				TimeUnit.SECONDS.sleep(1);
				customSetContent(add, order, filename, content, server, retryNumber + 1);
			} else {
				logSendError(order, setContentPath);
				throw e;
			}
		}
	}

	private static List<OrderItem> getStubOrderItems(Order order, Server server) {
		Order stubOrder = new Order();
		User user = order.getUserTo();
		stubOrder.setUser(user);
		stubOrder.setServer(server);
		Item stubItem = new Item();
		stubItem.setId(server.getId());
		stubItem.setInGameId(user.getSteamId());
		OrderItem stubOrderItem = new OrderItem();
		stubOrderItem.setOrder(stubOrder);
		stubOrderItem.setItem(stubItem);
		List<OrderItem> stubOrderItems = Collections.singletonList(stubOrderItem);
		stubOrder.setOrderItems(stubOrderItems);
		return stubOrderItems;
	}

	private void logSendError(Order order, String completePath) {
		List<Object> msgParams = new ArrayList<>();
		msgParams.add(order.getServer().getId());
		msgParams.add(completePath);
		msgParams.add(order.getOrderItems().stream().map(OrderItem::getId).collect(Collectors.toSet()));
		msgParams.add(order.getUserTo().getSteamId());
		LOGGER.log(Level.SEVERE, "Cannot write SET to server: {0}, with path {1}, using order items: {2}, for user steam id:{3}", msgParams.toArray());
	}

	public void spawningItems(List<OrderItem> orderItems, boolean add) throws IOException, JSchException, SftpException, InterruptedException {
		spawningItems(orderItems, add, 1);
	}

	private void spawningItems(List<OrderItem> orderItems, boolean add, int retryNumber) throws IOException, JSchException, SftpException, InterruptedException {
		if (!orderItems.isEmpty()) {
			Order order = orderItems.stream().findFirst().get().getOrder();
			Server server = order.getServer();
			String pathToJson = SFTPUtils.getPathToSpawningItemsJson(server);
			String steamId = order.getUserTo().getSteamId();
			String completePath = String.format(pathToJson, server.getInstanceName(), steamId);
			try {
				InputStream existingItems = null;
				try {
					existingItems = SFTPUtils.getFileContent(order, completePath);
				} catch (JSchException | SftpException e) {
					LOGGER.info("There is no such file on server: " + server.getInstanceName() + ", will create for user: " + steamId);
				}
				Root root = new Root();
				ObjectMapper om = new ObjectMapper();
				om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
				if (existingItems != null) {
					root = om.readValue(existingItems, Root.class);
				}
				List<MCodeArray> m_codeArray = mCodeMapper.mapOrderToRoot(orderItems).getM_CodeArray();
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
					LOGGER.info("There is no such file on server: " + server.getInstanceName() + ", will create for user: " + steamId);
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
					TimeUnit.SECONDS.sleep(1);
					spawningItems(orderItems, add, retryNumber + 1);
				} else {
					logSendError(order, completePath);
					throw e;
				}
			}
		}
	}
}
