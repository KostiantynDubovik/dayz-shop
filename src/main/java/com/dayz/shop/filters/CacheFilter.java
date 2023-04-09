package com.dayz.shop.filters;

import com.dayz.shop.filters.response.wrapper.AddExpiresHeaderResponse;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@org.springframework.core.annotation.Order(2)
@Component
public class CacheFilter extends HttpFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, new AddExpiresHeaderResponse(response));
	}
}
