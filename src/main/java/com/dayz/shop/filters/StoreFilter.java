package com.dayz.shop.filters;

import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.utils.Utils;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Order(1)
@Component
public class StoreFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Store requestedStore = Utils.extractStoreFromRequest(request);

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		httpRequest.setAttribute("store", requestedStore);

		HttpSession session = httpRequest.getSession(false);

		if (session != null) {
			SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
			if (securityContext != null) {
				Authentication authentication = securityContext.getAuthentication();
				User user = (User) authentication.getPrincipal();
				if (!Utils.isAppAdmin(user)) {
					Store userStore = user.getStore();
					if (!userStore.getId().equals(requestedStore.getId()) && !Utils.isAppAdmin(user)) {
						SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
						logoutHandler.setClearAuthentication(true);
						logoutHandler.setInvalidateHttpSession(true);
						logoutHandler.logout(httpRequest, (HttpServletResponse) response, authentication);
						((HttpServletResponse) response).sendRedirect("/");
						return;
					}
				}
			}
		}

		chain.doFilter(request, response);
	}

}
