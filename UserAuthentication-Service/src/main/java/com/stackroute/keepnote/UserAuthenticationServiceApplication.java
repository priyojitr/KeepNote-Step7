package com.stackroute.keepnote;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.stackroute.keepnote.filter.CorsFilter;


@SpringBootApplication
public class UserAuthenticationServiceApplication {

	@Bean
	public FilterRegistrationBean<Filter> jwtFilter() {
		FilterRegistrationBean<Filter> filterBean = new FilterRegistrationBean<>();
		filterBean.setFilter(new CorsFilter());
		filterBean.addUrlPatterns("/*");
		return filterBean;
	}
	
    public static void main(String[] args) {
        SpringApplication.run(UserAuthenticationServiceApplication.class, args);
    }
}
