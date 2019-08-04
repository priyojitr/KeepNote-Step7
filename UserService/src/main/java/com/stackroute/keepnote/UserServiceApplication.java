package com.stackroute.keepnote;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.stackroute.keepnote.jwtfilter.JwtFilter;

@SpringBootApplication
public class UserServiceApplication {

	private static final String JWT_KEY = "jwt.key";

	@Autowired
	private Environment env;

	@Bean
	public FilterRegistrationBean<Filter> jwtFilter() {
		FilterRegistrationBean<Filter> filterBean = new FilterRegistrationBean<>();
		filterBean.setFilter(new JwtFilter(env.getProperty(JWT_KEY)));
		filterBean.addUrlPatterns("/*");
		return filterBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
}
