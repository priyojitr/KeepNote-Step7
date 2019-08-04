package com.stackroute.keepnote.jwtfilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * THe filter should intercept request to verify whether bearer token in
 * authorization header is present and valid.
 * 
 * @author ubuntu
 *
 */
public class JwtFilter extends GenericFilterBean {

	private static final String HDR_AUTH = "Authorization";

	private final String secret;

	public JwtFilter(String secret) {
		this.secret = secret;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final String origin = "http://localhost:4200";

		HttpServletRequest req = (HttpServletRequest) request;

		if (req.getMethod().equals(HttpMethod.OPTIONS.name())) {
			((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		} else {
			((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", HDR_AUTH);
		}

		((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", origin);
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Credentials", "false");

		// bypass authorization check for options request & user registration
		if (!req.getMethod().equals(HttpMethod.OPTIONS.name())) {
			if (!req.getRequestURI().toLowerCase().contains("register")) {
				String authHeader = req.getHeader(HDR_AUTH);
				if (null == authHeader || !authHeader.startsWith("Bearer ")) {
					throw new ServletException("missing bearer token");
				}
				try {
					Claims token = Jwts.parser().setSigningKey(secret).parseClaimsJws(authHeader.substring(7))
							.getBody();
					request.setAttribute("token", token);
				} catch (Exception e) {
					throw new ServletException(e.getMessage());
				}
			}
		}

		chain.doFilter(request, response);

	}
}
