package ua.oak.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
public class LocalizationConfiguration {

	public static final Locale DEFAULT_LOCALE = new Locale("uk", "UA");

	@Bean
	public AcceptHeaderLocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
		localeResolver.setDefaultLocale(DEFAULT_LOCALE);
		Locale.setDefault(DEFAULT_LOCALE);

		List<Locale> localeList = new ArrayList<>();
//		localeList.add(Locale.ENGLISH);
		localeList.add(DEFAULT_LOCALE);
		localeResolver.setSupportedLocales(localeList);
		return localeResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
}
