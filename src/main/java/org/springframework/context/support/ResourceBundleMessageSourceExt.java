package org.springframework.context.support;

import ua.com.oak.shop.jpa.entities.Store;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

@Component
public class ResourceBundleMessageSourceExt extends ResourceBundleMessageSource {

	private final ResourceBundleMessageSource resourceBundleMessageSource;

	public ResourceBundleMessageSourceExt(ResourceBundleMessageSource resourceBundleMessageSource) {
		this.resourceBundleMessageSource = resourceBundleMessageSource;
		this.resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
	}

	public String getMessage(String code, Locale locale, Store store, Object... args) {
		return getMessage(code, locale, store.getStoreName(), args);
	}

	public String getMessage(String code, Locale locale, String store, Object... args) {
		ResourceBundle bundle = resourceBundleMessageSource.getResourceBundle("storeText/" + store, locale);
		if (bundle != null) {
			MessageFormat messageFormat = resourceBundleMessageSource.getMessageFormat(bundle, code, locale);
			if (messageFormat != null) {
				messageFormat.format(args);
			}
		}
		return StringUtils.EMPTY;
	}

	@Override
	public void setBundleClassLoader(ClassLoader classLoader) {
		this.resourceBundleMessageSource.setBundleClassLoader(classLoader);
	}

	@Override
	protected ClassLoader getBundleClassLoader() {
		return this.resourceBundleMessageSource.getBundleClassLoader();
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.resourceBundleMessageSource.setBeanClassLoader(classLoader);
	}

	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		return this.resourceBundleMessageSource.resolveCodeWithoutArguments(code, locale);
	}

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		return this.resourceBundleMessageSource.resolveCode(code, locale);
	}

	@Override
	protected ResourceBundle getResourceBundle(String basename, Locale locale) {
		return this.resourceBundleMessageSource.getResourceBundle(basename, locale);
	}

	@Override
	protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
		return this.resourceBundleMessageSource.doGetBundle(basename, locale);
	}

	@Override
	protected ResourceBundle loadBundle(Reader reader) throws IOException {
		return this.resourceBundleMessageSource.loadBundle(reader);
	}

	@Override
	protected ResourceBundle loadBundle(InputStream inputStream) throws IOException {
		return this.resourceBundleMessageSource.loadBundle(inputStream);
	}

	@Override
	protected MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale) throws MissingResourceException {
		return this.resourceBundleMessageSource.getMessageFormat(bundle, code, locale);
	}

	@Override
	protected String getStringOrNull(ResourceBundle bundle, String key) {
		return this.resourceBundleMessageSource.getStringOrNull(bundle, key);
	}

	@Override
	public String toString() {
		return this.resourceBundleMessageSource.toString();
	}

	@Override
	public void setBasename(String basename) {
		this.resourceBundleMessageSource.setBasename(basename);
	}

	@Override
	public void setBasenames(String... basenames) {
		this.resourceBundleMessageSource.setBasenames(basenames);
	}

	@Override
	public void addBasenames(String... basenames) {
		this.resourceBundleMessageSource.addBasenames(basenames);
	}

	@Override
	public Set<String> getBasenameSet() {
		return this.resourceBundleMessageSource.getBasenameSet();
	}

	@Override
	protected String getDefaultEncoding() {
		return this.resourceBundleMessageSource.getDefaultEncoding();
	}

	@Override
	public void setFallbackToSystemLocale(boolean fallbackToSystemLocale) {
		this.resourceBundleMessageSource.setFallbackToSystemLocale(fallbackToSystemLocale);
	}

	@Override
	@Deprecated
	protected boolean isFallbackToSystemLocale() {
		return this.resourceBundleMessageSource.isFallbackToSystemLocale();
	}

	@Override
	public void setDefaultLocale(@Nullable Locale defaultLocale) {
		this.resourceBundleMessageSource.setDefaultLocale(defaultLocale);
	}

	@Override
	protected Locale getDefaultLocale() {
		return this.resourceBundleMessageSource.getDefaultLocale();
	}

	@Override
	public void setCacheSeconds(int cacheSeconds) {
		this.resourceBundleMessageSource.setCacheSeconds(cacheSeconds);
	}

	@Override
	public void setCacheMillis(long cacheMillis) {
		this.resourceBundleMessageSource.setCacheMillis(cacheMillis);
	}

	@Override
	protected long getCacheMillis() {
		return this.resourceBundleMessageSource.getCacheMillis();
	}

	@Override
	public void setParentMessageSource(@Nullable MessageSource parent) {
		this.resourceBundleMessageSource.setParentMessageSource(parent);
	}

	@Override
	public MessageSource getParentMessageSource() {
		return this.resourceBundleMessageSource.getParentMessageSource();
	}

	@Override
	public void setCommonMessages(@Nullable Properties commonMessages) {
		this.resourceBundleMessageSource.setCommonMessages(commonMessages);
	}

	@Override
	protected Properties getCommonMessages() {
		return this.resourceBundleMessageSource.getCommonMessages();
	}

	@Override
	public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
		this.resourceBundleMessageSource.setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
	}

	@Override
	protected boolean isUseCodeAsDefaultMessage() {
		return this.resourceBundleMessageSource.isUseCodeAsDefaultMessage();
	}

	@Override
	protected String getMessageInternal(@Nullable String code, @Nullable Object[] args, @Nullable Locale locale) {
		return this.resourceBundleMessageSource.getMessageInternal(code, args, locale);
	}

	@Override
	protected String getMessageFromParent(String code, @Nullable Object[] args, Locale locale) {
		return this.resourceBundleMessageSource.getMessageFromParent(code, args, locale);
	}

	@Override
	protected String getDefaultMessage(MessageSourceResolvable resolvable, Locale locale) {
		return this.resourceBundleMessageSource.getDefaultMessage(resolvable, locale);
	}

	@Override
	protected String getDefaultMessage(String code) {
		return this.resourceBundleMessageSource.getDefaultMessage(code);
	}

	@Override
	protected Object[] resolveArguments(@Nullable Object[] args, Locale locale) {
		return this.resourceBundleMessageSource.resolveArguments(args, locale);
	}

	@Override
	public void setAlwaysUseMessageFormat(boolean alwaysUseMessageFormat) {
		this.resourceBundleMessageSource.setAlwaysUseMessageFormat(alwaysUseMessageFormat);
	}

	@Override
	protected boolean isAlwaysUseMessageFormat() {
		return this.resourceBundleMessageSource.isAlwaysUseMessageFormat();
	}

	@Override
	protected String renderDefaultMessage(String defaultMessage, @Nullable Object[] args, Locale locale) {
		return this.resourceBundleMessageSource.renderDefaultMessage(defaultMessage, args, locale);
	}

	@Override
	protected String formatMessage(String msg, @Nullable Object[] args, Locale locale) {
		return this.resourceBundleMessageSource.formatMessage(msg, args, locale);
	}

	@Override
	protected MessageFormat createMessageFormat(String msg, Locale locale) {
		return this.resourceBundleMessageSource.createMessageFormat(msg, locale);
	}
}
