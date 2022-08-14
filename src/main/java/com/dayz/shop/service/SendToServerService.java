package com.dayz.shop.service;

import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.json.MCodeArray;
import com.dayz.shop.json.Root;
import com.dayz.shop.repository.StoreConfigRepository;
import com.dayz.shop.utils.MCodeMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class SendToServerService {
	public static final int PORT = 22;
	public static final String SSH_KNOWN_HOSTS = "~/.ssh/known_hosts";
	final StoreConfigRepository storeConfigRepository;
	final MCodeMapper mCodeMapper;

	@Autowired
	public SendToServerService(StoreConfigRepository storeConfigRepository, MCodeMapper mCodeMapper) {
		this.storeConfigRepository = storeConfigRepository;
		this.mCodeMapper = mCodeMapper;
	}

	public void sendOrder(Order order) throws JSchException, InterruptedException, IOException, SftpException {
		String username = getUsr(order);
		String password = getPwd(order);
		String host = getIp(order);
		String pathToJson = getPathToJson(order);
		String completePath = String.format(pathToJson, order.getServer().getServerName(), order.getUser().getSteamId());
		File existingFile = new File("get");
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

		File mCode = new File("put");

		om.writerWithDefaultPrettyPrinter().writeValue(mCode, root);

		updateFile(username, password, host, completePath, mCode);
	}

	private String getPathToJson(Order order) {
		return storeConfigRepository.findByKeyAndStore("PATH_TO_JSON", order.getStore()).getValue();
	}

	private String getUsr(Order order) {
		return storeConfigRepository.findByKeyAndStore("SSH_USR", order.getStore()).getValue();
	}

	private String getPwd(Order order) {
		return storeConfigRepository.findByKeyAndStore("SSH_PWD", order.getStore()).getValue();
	}

	private String getIp(Order order) {
		return storeConfigRepository.findByKeyAndStore("SSH_IP", order.getStore()).getValue();
	}

	private void getFile(String username, String password, String host, String path, File file) throws JSchException, SftpException, FileNotFoundException {

		ChannelSftp channelSftp = setupJsch(username, password, host);
		channelSftp.connect();

		channelSftp.get(path, new FileOutputStream(file));

		channelSftp.exit();
	}

	private void updateFile(String username, String password, String host, String path, File file) throws JSchException, SftpException, FileNotFoundException {
		ChannelSftp channelSftp = setupJsch(username, password, host);
		channelSftp.connect();

		channelSftp.put(new FileInputStream(file), path);

		channelSftp.exit();
	}

	private ChannelSftp setupJsch(String username, String password, String host) throws JSchException {
		JSch jsch = new JSch();
		jsch.setKnownHosts(SSH_KNOWN_HOSTS);
		Session jschSession = jsch.getSession(username, host);
		jschSession.setPassword(password);
		jschSession.connect();
		return (ChannelSftp) jschSession.openChannel("sftp");
	}

	private String sendCommand(String username, String password, String host, String command) throws JSchException, InterruptedException {
		Session session = null;
		ChannelExec channel = null;

		try {
			session = new JSch().getSession(username, host, PORT);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(command);
			ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
			channel.setOutputStream(responseStream);
			channel.connect();

			while (channel.isConnected()) {
				Thread.sleep(100);
			}

			return responseStream.toString();
		} finally {
			if (session != null) {
				session.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}
		}
	}

}
