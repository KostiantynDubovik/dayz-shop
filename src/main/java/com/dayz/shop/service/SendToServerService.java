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
		boolean deletedExisting = existingFile.delete();

		root.getM_CodeArray().addAll(mCodeMapper.mapOrderToRoot(order).getM_CodeArray());
		//TODO dir *.json /b

		File mCode = new File("put");

		om.writerWithDefaultPrettyPrinter().writeValue(mCode, root);

		updateFile(username, password, host, completePath, mCode);
		boolean deletedUpdated = mCode.delete();
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

	private void getFile(String username, String password, String host, String path, File file) throws JSchException, SftpException, IOException {
		Session session = setupJsch(username, password, host);
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
		channelSftp.connect();

		FileOutputStream dst = new FileOutputStream(file);
		channelSftp.get(path, dst);
		dst.close();
		channelSftp.exit();
		session.disconnect();
	}

	private void updateFile(String username, String password, String host, String path, File file) throws JSchException, SftpException, IOException {
		Session session = setupJsch(username, password, host);
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
		channelSftp.connect();

		FileInputStream dst = new FileInputStream(file);
		channelSftp.put(dst, path);
		dst.close();
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
