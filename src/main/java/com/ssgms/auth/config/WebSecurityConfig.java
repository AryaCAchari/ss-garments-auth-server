package com.ssgms.auth.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity security) throws Exception {
		security.csrf().disable().authorizeRequests().antMatchers("/v1/api/auth/**").permitAll().anyRequest()
				.authenticated().and().oauth2ResourceServer().jwt()
				.jwtAuthenticationConverter(jwtAuthenticationConverter());

	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		// Convert realm_access.roles claims to granted authorities, for use in access
		// decisions
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
		return jwtAuthenticationConverter;
	}

	class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

		@Override
		@SuppressWarnings("unchecked")
		public Collection<GrantedAuthority> convert(final Jwt jwt) {
			final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
			return ((List<String>) realmAccess.get("roles")).stream().map(roleName -> "ROLE_" + roleName)
					.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		}

	}
}
