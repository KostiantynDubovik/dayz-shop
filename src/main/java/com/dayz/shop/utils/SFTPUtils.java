package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.jpa.entities.Server;
import com.dayz.shop.repository.ServerConfigRepository;
import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class SFTPUtils {
	public static final String SFTP_TYPE = "sftp";
	public static final String SSH_KNOWN_HOSTS = "~/.ssh/known_hosts";

	private static ServerConfigRepository serverConfigRepository;

	@Autowired
	public SFTPUtils(ServerConfigRepository serverConfigRepository) {
		SFTPUtils.serverConfigRepository = serverConfigRepository;
	}

	public static String getUsr(Server server) {
		return serverConfigRepository.findByKeyAndServer("SSH_USR", server).getValue();
	}

	public static String getPwd(Server server) {
		return serverConfigRepository.findByKeyAndServer("SSH_PWD", server).getValue();
	}

	public static String getIp(Server server) {
		return serverConfigRepository.findByKeyAndServer("SSH_IP", server).getValue();
	}

	public static String getPathToSpawningItemsJson(Order order) {
		return serverConfigRepository.findByKeyAndServer("PATH_TO_JSON", order.getServer()).getValue();
	}

	public static String getPathToSet(Order order) {
		return serverConfigRepository.findByKeyAndServer("PATH_TO_SET", order.getServer()).getValue();
	}

	public static String getPathToVip(Order order) {
		return serverConfigRepository.findByKeyAndServer("PATH_TO_VIP", order.getServer()).getValue();
	}

	public static ByteArrayInputStream getFileContent(Order order, String path) throws JSchException, SftpException {
		Server server = order.getServer();
		String username = SFTPUtils.getUsr(server);
		String password = SFTPUtils.getPwd(server);
		String host = SFTPUtils.getIp(server);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Session session = setupJsch(username, password, host);
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel(SFTP_TYPE);
		channelSftp.connect();

		ByteArrayInputStream result = null;

		try {
			channelSftp.get(path, baos);
			result = new ByteArrayInputStream(baos.toByteArray());
		} finally {
			channelSftp.exit();
			session.disconnect();
		}
		return result;
	}

	public static void updateFile(Order order, String path, InputStream contentStream)
			throws JSchException, SftpException {
		Server server = order.getServer();
		String username = SFTPUtils.getUsr(server);
		String password = SFTPUtils.getPwd(server);
		String host = SFTPUtils.getIp(server);
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

	private static Session setupJsch(String username, String password, String host) throws JSchException {
		JSch jsch = new JSch();
		jsch.setKnownHosts(SSH_KNOWN_HOSTS);
		Session jschSession = jsch.getSession(username, host);
		jschSession.setPassword(password);
		jschSession.connect();
		return jschSession;
	}

}

