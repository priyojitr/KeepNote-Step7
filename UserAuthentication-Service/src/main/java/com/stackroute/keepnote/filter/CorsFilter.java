package com.stackroute.keepnote.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

public class CorsFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final String origin = "http://localhost:4200";

		HttpServletRequest req = (HttpServletRequest) request;

		if (req.getMethod().equals(HttpMethod.OPTIONS.name())) {
			((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		} else {
			((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "Authorization");
		}

		((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", origin);
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Credentials", "false");

		chain.doFilter(request, response);
	}

}
