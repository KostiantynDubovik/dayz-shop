package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.ItemType;
import com.dayz.shop.jpa.entities.Order;
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
import java.util.stream.Collectors;

@Service
public class SendToServerService {
	public static final String PIPE = "|";
	public static final String SEMICOLON = ";";
	public static final String ZERO = "0";
	public static final String SETS_FILE = "CustomSpawnPlayerConfig.txt";
	public static final String VIP_FILE = "priority.txt";
	final MCodeMapper mCodeMapper;

	@Autowired
	public SendToServerService(MCodeMapper mCodeMapper) {
		this.mCodeMapper = mCodeMapper;
	}

	public void sendOrder(Order order, Map<ItemType, Order> separatedTypes) throws JSchException, InterruptedException, IOException, SftpException {
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
		} catch (JSchException | SftpException e) {
			System.out.println(e);
		}
	}

	public void vip(Order order, String steamId, boolean add) throws JSchException, SftpException {
		String pathToVip = SFTPUtils.getPathToVip(order);
		String completePath = String.format(pathToVip, order.getServer().getInstanceName(), VIP_FILE);
		ByteArrayInputStream fileContent = SFTPUtils.getFileContent(order, completePath);
		if (fileContent != null) {
			Scanner scanner = new Scanner(fileContent);
			List<String> existingSteamIds = new ArrayList<>();
			scanner.useDelimiter(SEMICOLON);
			while (scanner.hasNext()) {
				String existingSteamId = scanner.next();
				if (Objects.equals(existingSteamId, steamId)) {
					return;
				} else {
					existingSteamIds.add(existingSteamId);
				}
			}
			if (add) {
				existingSteamIds.add(steamId);
			} else {
				existingSteamIds.remove(steamId);
			}
			ByteArrayInputStream contentStream = new ByteArrayInputStream(String.join(SEMICOLON, existingSteamIds).concat(SEMICOLON).getBytes(StandardCharsets.UTF_8));
			SFTPUtils.updateFile(order, completePath, contentStream);
		}
	}

	public void set(Order order, String steamId, boolean add) throws IOException, JSchException, SftpException {
		String pathToSet = SFTPUtils.getPathToSet(order);
		String completePath = String.format(pathToSet, order.getServer().getInstanceName(), SETS_FILE);
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
		}
	}

	public void spawningItems(Order order, String steamId, boolean add) throws IOException, JSchException, SftpException {
		String pathToJson = SFTPUtils.getPathToSpawningItemsJson(order);
		String completePath = String.format(pathToJson, order.getServer().getInstanceName(), steamId);
		InputStream existingItems = SFTPUtils.getFileContent(order, completePath);
		Root root = new Root();
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		if (existingItems != null) {
			root = om.readValue(existingItems, Root.class);
		}
		if (add) {
			root.getM_CodeArray().addAll(mCodeMapper.mapOrderToRoot(order).getM_CodeArray());
		} else {
			root.getM_CodeArray().removeAll(mCodeMapper.mapOrderToRoot(order).getM_CodeArray());
		}

		InputStream content = new ByteArrayInputStream(om.writerWithDefaultPrettyPrinter().writeValueAsString(root).getBytes(StandardCharsets.UTF_8));
		SFTPUtils.updateFile(order, completePath, content);
	}
}
