package com.dayz.shop.utils;

import com.dayz.shop.jpa.entities.Order;
import com.dayz.shop.jpa.entities.Server;
import com.jcraft.jsch.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component
public class SFTPUtils {
	public static final String SFTP_TYPE = "sftp";
	public static final String SSH_KNOWN_HOSTS = "~/.ssh/known_hosts";

	public static String getUsr(Server server) {
		return server.getString("SSH_USR");
	}

	public static String getPwd(Server server) {
		return server.getString("SSH_PWD");
	}

	public static String getIp(Server server) {
		return server.getString("SSH_IP");
	}

	public static String getPathToSpawningItemsJson(Server server) {
		return server.getString("PATH_TO_JSON");
	}

	public static String getPathToSet(Order order) {
		return getPathToSet(order.getServer());
	}

	public static String getPathToSet(Server server) {
		return server.getString("PATH_TO_SET");
	}

	public static String getPathToVip(Order order) {
		return getPathToVip(order.getServer());
	}
	public static String getPathToVip(Server server) {
		return server.getString("PATH_TO_VIP");
	}

	public static ByteArrayInputStream getFileContent(Order order, String path) throws JSchException, SftpException {
		return getFileContent(order.getServer(), path);
	}

	public static ByteArrayInputStream getFileContent(Server server, String path) throws JSchException, SftpException {
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

	public static void updateFile(Order order, String path, InputStream contentStream) throws JSchException, SftpException {
		updateFile(order.getServer(), path, contentStream);
	}

	public static void updateFile(Server server, String path, InputStream contentStream)
			throws JSchException, SftpException {
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

