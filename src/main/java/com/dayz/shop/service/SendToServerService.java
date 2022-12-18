package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.ItemType;
import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.json.MCodeArray;
import com.dayz.shop.json.Root;
import com.dayz.shop.utils.MCodeMapper;
import com.dayz.shop.utils.Utils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SendToServerService {
	public static final String SSH_KNOWN_HOSTS = "~/.ssh/known_hosts";
	public static final String SFTP_TYPE = "sftp";
	public static final String PIPE = "|";
	public static final String SEMICOLON = ";";
	public static final String ZERO = "0";
	final MCodeMapper mCodeMapper;

	@Autowired
	public SendToServerService(MCodeMapper mCodeMapper) {
		this.mCodeMapper = mCodeMapper;
	}

	public void sendOrder(Order order, Map<ItemType, Order> separatedTypes) throws JSchException, InterruptedException, IOException, SftpException {
		String username = getUsr(order);
		String password = getPwd(order);
		String host = getIp(order);
		String steamId = order.getUser().getSteamId();
		for (ItemType itemType : separatedTypes.keySet()) {
			switch (itemType) {
				case ITEM:
				case VEHICLE:
					sendSpawningItems(order, username, password, host, steamId);
					break;
				case VIP:
					sendVip(order, username, password, host, steamId);
					break;
				case SET:
					sendSet(order, username, password, host, steamId);
			}
		}
	}

	private void sendVip(Order order, String username, String password, String host, String steamId) throws IOException, JSchException, SftpException {
		String pathToVip = getPathToVip(order);
		String vipFile = "priority.txt";
		String completePath = String.format(pathToVip, order.getServer().getServerName(), vipFile);
		InputStream fileContent = getFileContent(username, password, host, completePath);
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
			existingSteamIds.add(steamId);
			ByteArrayInputStream contentStream = new ByteArrayInputStream(String.join(SEMICOLON, existingSteamIds).concat(SEMICOLON).getBytes(StandardCharsets.UTF_8));
			updateFile(username, password, host, completePath, contentStream);
		}

	}

	private void sendSet(Order order, String username, String password, String host, String steamId) throws IOException, JSchException, SftpException {
		String pathToSet = getPathToSet(order);
		String setsFile = "CustomSpawnPlayerConfig.txt";
		String completePath = String.format(pathToSet, order.getServer().getServerName(), setsFile);
		InputStream existingSets = getFileContent(username, password, host, completePath);
		if (existingSets != null) {
			Map<String, String> setMap = IOUtils.readLines(existingSets, StandardCharsets.UTF_8).stream().collect(Collectors.toMap(input -> StringUtils.split(input, PIPE)[0], input -> input));
			setMap.put(steamId, String.join(PIPE, steamId, ZERO, order.getOrderItems().get(0).getItem().getInGameId(), ZERO));
			ByteArrayInputStream contentStream = new ByteArrayInputStream(String.join(System.lineSeparator(), setMap.values()).getBytes(StandardCharsets.UTF_8));
			updateFile(username, password, host, completePath, contentStream);
		}
	}

	private void sendSpawningItems(Order order, String username, String password, String host, String steamId) throws IOException, JSchException, SftpException {
		String pathToJson = getPathToSpawningItemsJson(order);
		String completePath = String.format(pathToJson, order.getServer().getServerName(), steamId);
		InputStream existingItems = getFileContent(username, password, host, completePath);
		Root root = new Root();
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		if (existingItems != null) {
			root = om.readValue(existingItems, Root.class);
		} else {
			MCodeArray mCodeArray = new MCodeArray();
			root.getM_CodeArray().add(mCodeArray);
		}
		root.getM_CodeArray().addAll(mCodeMapper.mapOrderToRoot(order).getM_CodeArray());
		//TODO dir *.json /b

		InputStream content = new ByteArrayInputStream(om.writerWithDefaultPrettyPrinter().writeValueAsString(root).getBytes(StandardCharsets.UTF_8));
		updateFile(username, password, host, completePath, content);
	}

	private String getPathToSpawningItemsJson(Order order) {
		return Utils.getServerConfig("PATH_TO_JSON", order.getServer());
	}

	private String getPathToSet(Order order) {
		return Utils.getServerConfig("PATH_TO_SET", order.getServer());
	}

	private String getPathToVip(Order order) {
		return Utils.getServerConfig("PATH_TO_VIP", order.getServer());
	}

	private String getUsr(Order order) {
		return Utils.getServerConfig("SSH_USR", order.getServer());
	}

	private String getPwd(Order order) {
		return Utils.getServerConfig("SSH_PWD", order.getServer());
	}

	private String getIp(Order order) {
		return Utils.getServerConfig("SSH_IP", order.getServer());
	}

	private InputStream getFileContent(String username, String password, String host, String path) throws
			JSchException, SftpException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Session session = setupJsch(username, password, host);
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel(SFTP_TYPE);
		channelSftp.connect();

		InputStream result = null;

		try {
			channelSftp.get(path, baos);
			result = new ByteArrayInputStream(baos.toByteArray());
		} catch (SftpException e) {
			if ("No such file".equals(e.getMessage())) {
				System.out.println("Creating new file: " + path);
			} else {
				throw e;
			}
		} finally {
			channelSftp.exit();
			session.disconnect();
		}
		return result;
	}

	private void updateFile(String username, String password, String host, String path, InputStream contentStream)
			throws JSchException, SftpException {
		Session session = null;
		ChannelSftp channelSftp = null;
		try {
			session = setupJsch(username, password, host);
			channelSftp = (ChannelSftp) session.openChannel(SFTP_TYPE);
			channelSftp.connect();
			channelSftp.put(contentStream, path);
		} finally {
			if (channelSftp != null) {
				channelSftp.exit();
			}
			if (session != null) {
				session.disconnect();
			}
		}
	}

	private Session setupJsch(String username, String password, String host) throws JSchException {
		JSch jsch = new JSch();
		jsch.setKnownHosts(SSH_KNOWN_HOSTS);
		Session jschSession = jsch.getSession(username, host);
		jschSession.setPassword(password);
		jschSession.connect();
		return jschSession;
	}
}
