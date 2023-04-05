package com.dayz.shop.filters.response.wrapper;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddExpiresHeaderResponse extends HttpServletResponseWrapper {

	public static final List<String> CACHEABLE_CONTENT_TYPES = Arrays.asList(
			"image/svg", "image/ico", "image/png",
			"image/jpeg", "image/gif", "image/jpg");

	public AddExpiresHeaderResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void setContentType(String contentType) {
		if (contentType != null && CACHEABLE_CONTENT_TYPES.contains(contentType)) {
			Calendar inTwoMonths = Calendar.getInstance();
			inTwoMonths.add(Calendar.MONTH, 2);
			super.setDateHeader("Expires", inTwoMonths.getTimeInMillis());
			super.setHeader("Cache-Control", "public");
		}
		super.setContentType(contentType);
	}
}