package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.json.MCodeArray;
import com.dayz.shop.json.Root;
import com.dayz.shop.repository.ServerConfigRepository;
import com.dayz.shop.repository.StoreConfigRepository;
import com.dayz.shop.utils.MCodeMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class SendToServerService {
	public static final int PORT = 22;
	public static final String SSH_KNOWN_HOSTS = "~/.ssh/known_hosts";
	public static final String SFTP_TYPE = "sftp";
	final StoreConfigRepository storeConfigRepository;
	final ServerConfigRepository serverConfigRepository;
	final MCodeMapper mCodeMapper;

	@Autowired
	public SendToServerService(StoreConfigRepository storeConfigRepository, ServerConfigRepository serverConfigRepository, MCodeMapper mCodeMapper) {
		this.storeConfigRepository = storeConfigRepository;
		this.serverConfigRepository = serverConfigRepository;
		this.mCodeMapper = mCodeMapper;
	}

	public void sendOrder(Order order) throws JSchException, InterruptedException, IOException, SftpException {
		String username = getUsr(order);
		String password = getPwd(order);
		String host = getIp(order);
		String pathToJson = getPathToJson(order);
		String steamId = order.getUser().getSteamId();
		String completePath = String.format(pathToJson, order.getServer().getServerName(), steamId);
		File existingFile = new File("get/" + steamId + ".json");
		try {
			if (existingFile.createNewFile()) {
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

				File mCode = new File("put/" + steamId + ".json");
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

	private String getPathToJson(Order order) {
		return storeConfigRepository.findByKeyAndStore("PATH_TO_JSON", order.getStore()).getValue();
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
