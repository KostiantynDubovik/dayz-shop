package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.ItemType;
import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.json.MCodeArray;
import com.dayz.shop.json.Root;
import com.dayz.shop.repository.ServerConfigRepository;
import com.dayz.shop.utils.MCodeMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SendToServerService {
	public static final String SSH_KNOWN_HOSTS = "~/.ssh/known_hosts";
	public static final String SFTP_TYPE = "sftp";
	public static final String PIPE = "|";
	public static final String SEMICOLON = ";";
	public static final String ZERO = "0";
	public static final String TEMP_FILE_PUT_PATH = "put/";
	public static final String TEMP_FILE_GET_PATH = "get/";
	final ServerConfigRepository serverConfigRepository;
	final MCodeMapper mCodeMapper;

	@Autowired
	public SendToServerService(ServerConfigRepository serverConfigRepository, MCodeMapper mCodeMapper) {
		this.serverConfigRepository = serverConfigRepository;
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
		File existingVipFile = new File(TEMP_FILE_GET_PATH + vipFile);
		Scanner scanner;
		try {
			if (existingVipFile.exists()) {
				new FileWriter(existingVipFile, false).close();
			}
			if (existingVipFile.createNewFile() || existingVipFile.exists()) {
				getFile(username, password, host, completePath, existingVipFile);
				scanner = new Scanner(existingVipFile);
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
				File newVipFile = new File(TEMP_FILE_PUT_PATH + vipFile);
				try {
					if (newVipFile.createNewFile()) {
						Files.write(newVipFile.toPath(), String.join(SEMICOLON, existingSteamIds).concat(SEMICOLON).getBytes());
						updateFile(username, password, host, completePath, newVipFile);
					}
				} finally {
					newVipFile.delete();
				}
			}
			System.out.println();
		} finally {
			existingVipFile.delete();
		}
	}

	private void sendSet(Order order, String username, String password, String host, String steamId) throws IOException, JSchException, SftpException {
		String pathToSet = getPathToSet(order);
		String setsFile = "CustomSpawnPlayerConfig.txt";
		String completePath = String.format(pathToSet, order.getServer().getServerName(), setsFile);
		File existingSetFile = new File(TEMP_FILE_GET_PATH + setsFile);
		try {
			if (existingSetFile.exists()) {
				new FileWriter(existingSetFile, false).close();
			}
			if (existingSetFile.createNewFile() || existingSetFile.exists()) {
				getFile(username, password, host, completePath, existingSetFile);
				Map<String, String> setMap = Files.readAllLines(existingSetFile.toPath()).stream().collect(Collectors.toMap(input -> StringUtils.split(input, PIPE)[0], input -> input));
				setMap.put(steamId, String.join(PIPE, steamId, ZERO, order.getOrderItems().get(0).getItem().getInGameId(), ZERO));
				File newSetsFile = new File(TEMP_FILE_PUT_PATH + setsFile);
				try {
					if (newSetsFile.createNewFile()) {
						Files.write(newSetsFile.toPath(), setMap.values());
						updateFile(username, password, host, completePath, newSetsFile);
					}
				} finally {
					newSetsFile.delete();
				}
			}
		} finally {
			existingSetFile.delete();
		}
	}

	private void sendSpawningItems(Order order, String username, String password, String host, String steamId) throws IOException, JSchException, SftpException {
		String pathToJson = getPathToSpawningItemsJson(order);
		String completePath = String.format(pathToJson, order.getServer().getServerName(), steamId);
		File existingFile = new File(TEMP_FILE_GET_PATH + steamId + ".json");
		try {
			if (existingFile.exists()) {
				new FileWriter(existingFile, false).close();
			}
			if (existingFile.createNewFile() || existingFile.exists()) {
				getFile(username, password, host, completePath, existingFile);
				Root root = new Root();
				ObjectMapper om = new ObjectMapper();
				om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
				if (existingFile.length() > 0) {
					root = om.readValue(existingFile, Root.class);
				} else {
					MCodeArray mCodeArray = new MCodeArray();
					root.getM_CodeArray().add(mCodeArray);
				}
				root.getM_CodeArray().addAll(mCodeMapper.mapOrderToRoot(order).getM_CodeArray());
				//TODO dir *.json /b

				File mCode = new File(TEMP_FILE_PUT_PATH + steamId + ".json");
				try {
					if (mCode.createNewFile()) {
						om.writerWithDefaultPrettyPrinter().writeValue(mCode, root);
						updateFile(username, password, host, completePath, mCode);
					}
				} finally {
					mCode.delete();
				}
			}
		} finally {
			existingFile.delete();
		}
	}

	private String getPathToSpawningItemsJson(Order order) {
		return serverConfigRepository.findByKeyAndServer("PATH_TO_JSON", order.getServer()).getValue();
	}

	private String getPathToSet(Order order) {
		return serverConfigRepository.findByKeyAndServer("PATH_TO_SET", order.getServer()).getValue();
	}

	private String getPathToVip(Order order) {
		return serverConfigRepository.findByKeyAndServer("PATH_TO_VIP", order.getServer()).getValue();
	}

	private String getUsr(Order order) {
		return serverConfigRepository.findByKeyAndServer("SSH_USR", order.getServer()).getValue();
	}

	private String getPwd(Order order) {
		return serverConfigRepository.findByKeyAndServer("SSH_PWD", order.getServer()).getValue();
	}

	private String getIp(Order order) {
		return serverConfigRepository.findByKeyAndServer("SSH_IP", order.getServer()).getValue();
	}

	private void getFile(String username, String password, String host, String path, File file) throws
			JSchException, SftpException, IOException {
		Session session = setupJsch(username, password, host);
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel(SFTP_TYPE);
		channelSftp.connect();

		try (FileOutputStream dst = new FileOutputStream(file)) {
			channelSftp.get(path, dst);
		} catch (SftpException e) {
			if ("No such file".equals(e.getMessage())) {
				System.out.println("Creating new file: " + path);
			} else {
				throw e;
			}
		}
		channelSftp.exit();
		session.disconnect();
	}

	private void updateFile(String username, String password, String host, String path, File file) throws
			JSchException, SftpException, IOException {
		Session session = setupJsch(username, password, host);
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel(SFTP_TYPE);
		channelSftp.connect();

		try (FileInputStream dst = new FileInputStream(file)) {
			channelSftp.put(dst, path);
		}
		channelSftp.exit();
		session.disconnect();
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
