package com.dayz.shop.config;

import com.dayz.shop.service.CustomUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.openid4java.consumer.ConsumerManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.web.context.request.RequestContextListener;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityWebConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.expressionHandler(webSecurityExpressionHandler())
				.antMatchers("/shutdown").hasIpAddress("127.0.0.1")
				.antMatchers("/").permitAll()
				.antMatchers("/js/**", "/img/**", "/fonts/**","/css/**", "/bower_components/**").permitAll()
				.antMatchers("/api/**").permitAll()
				.antMatchers(HttpMethod.POST, "/api/**").permitAll()
				.antMatchers("/login/**").permitAll()
				.antMatchers("/v3/**").hasAuthority("APP_WRITE")
				.antMatchers("/swagger-ui/**").hasAuthority("APP_WRITE")
				.antMatchers("/swagger-resources/**").hasAuthority("APP_WRITE")
				.antMatchers("/resources/**", "/webjars/**", "/built/**", "/static/**").permitAll()
				.and()
				.openidLogin()
				.consumerManager(consumerManager())
				.loginPage("/").permitAll()
				.authenticationUserDetailsService(authenticationUserDetailsService())
				.failureUrl("/?fail")
				.defaultSuccessUrl("/", true)
				.and()
				.csrf().disable();
	}

	private ConsumerManager consumerManager() {
		ConsumerManager consumerManager = new ConsumerManager();
		consumerManager.setMaxAssocAttempts(0);
		return consumerManager;
	}

	@Bean
	public AuthenticationUserDetailsService<OpenIDAuthenticationToken> authenticationUserDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public RequestContextListener requestContextListener(){
		return new RequestContextListener();
	}

	@Bean
	public AffirmativeBased defaultAccessDecisionManager(RoleHierarchy roleHierarchy){
		System.out.println("arrive public AffirmativeBased defaultAccessDecisionManager()");
		List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();

		// webExpressionVoter
		WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
		DefaultWebSecurityExpressionHandler
				expressionHandler = new DefaultWebSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy);
		webExpressionVoter.setExpressionHandler(expressionHandler);

		decisionVoters.add(webExpressionVoter);
		decisionVoters.add(roleHierarchyVoter(roleHierarchy));
		// return new AffirmativeBased(Arrays.asList((AccessDecisionVoter) webExpressionVoter));
		return new AffirmativeBased(decisionVoters);
	}

	@Bean
	public RoleHierarchyVoter roleHierarchyVoter(RoleHierarchy roleHierarchy) {
		System.out.println("arrive public RoleHierarchyVoter roleHierarchyVoter");
		return new RoleHierarchyVoter(roleHierarchy);
	}

	@Bean
	public SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler() {
		DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		defaultWebSecurityExpressionHandler.setDefaultRolePrefix(StringUtils.EMPTY);
		defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
		return defaultWebSecurityExpressionHandler;
	}

	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		String hierarchy = "APP_ADMIN>STORE_ADMIN\nSTORE_ADMIN>USER";
		roleHierarchy.setHierarchy(hierarchy);
		return roleHierarchy;
	}

}
