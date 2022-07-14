package com.ssgms.auth.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class WebMvcConfig extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS").allowedOrigins("*")
				.allowedHeaders("Authorization", "Cache-Control", "Content-Type", "Accept", "X-Requested-With",
						"Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Origin", "Accept-Language")
				.exposedHeaders("Access-Control-Expose-Headers", "Authorization", "Cache-Control", "Content-Type",
						"Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Origin", "Accept-Language");
	}

	List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("fr"));

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String headerLang = request.getHeader("Accept-Language");
		return headerLang == null || headerLang.isEmpty() ? Locale.getDefault()
				: Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
	}

	@Bean("messageSource")
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages/message_ss");
		return messageSource;
	}
}
