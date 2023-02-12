package com.dayz.shop.filters;

import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.utils.Utils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.dayz.shop.utils.Utils.US_STORE_KEY;

@org.springframework.core.annotation.Order(1)
@Component
public class StoreFilter extends HttpFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request.getRequestURL().toString().endsWith("fk-verify.html")) {
			chain.doFilter(request, response);
		} else {
			Store requestedStore = Utils.extractStoreFromRequest(request);
			if (requestedStore == null) {
				response.sendError(404);
			} else {
				if (!request.getParameterMap().containsKey(US_STORE_KEY) && !request.getServerName().startsWith(requestedStore.getStoreName())) {
					String serverName = "https://".concat(request.getServerName().replace("dayz-shop", String.join(".", requestedStore.getStoreName(), "dayz-shop")));
					String query = String.join("?", request.getRequestURI(), request.getQueryString());
					response.sendRedirect(serverName.concat(query));
				} else {
					request.setAttribute("store", requestedStore);

					HttpSession session = request.getSession(false);

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
									logoutHandler.logout(request, response, authentication);
									response.sendRedirect("/");
									return;
								}
							}
						}
					}
					chain.doFilter(request, response);
				}
			}
		}
	}
}
