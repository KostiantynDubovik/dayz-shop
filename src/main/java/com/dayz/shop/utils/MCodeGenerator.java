package com.dayz.shop.utils;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.exception.spi.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.Random;

public class MCodeGenerator implements IdentifierGenerator, Configurable {

	public static final String CANDIDATES = "zxcvbnmasdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP1234567890";
	public static final String BASE = "HT-%s-%s";

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
		return generateMCode();
	}

	private String generateMCode() {
		String first = generateChunk();
		String second = generateChunk();
		return String.format(BASE, first, second);
	}

	private String generateChunk() {
		StringBuilder chunk = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			chunk.append(getRandomChar());
		}
		return chunk.toString();
	}

	private String getRandomChar() {
		Random random = new Random();
		return String.valueOf((CANDIDATES.charAt(random.nextInt(CANDIDATES.length()))));
	}

	@Override
	public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
		// no op
	}

	@Override
	public void configure(Properties properties) throws HibernateException {
		// no op
	}
}
