package com.ssgms.auth.service.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.ssgms.auth.component.Translator;
import com.ssgms.auth.dto.request.AuthRequestDto;
import com.ssgms.auth.dto.request.RefreshTokenRequestDto;
import com.ssgms.auth.dto.response.AuthResponseDto;
import com.ssgms.auth.enums.ErrorCodes;
import com.ssgms.auth.exception.ErrorModel;
import com.ssgms.auth.exception.SSGMSException;
import com.ssgms.auth.service.AuthService;
import com.ssgms.auth.utilities.SSConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("authService")
public class AuthServiceImpl implements AuthService {

	@Value("${keycloak.logout}")
	private String keycloakLogout;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.client-id}")
	private String clientId;

	@Value("${keycloak.server-url}")
	private String serverUrl;

	@Value("${keycloak.client-secret}")
	private String clientSecret;

	@Value("${keycloak.token-uri}")
	private String keycloakTokenUri;
	
	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public AuthResponseDto login(AuthRequestDto request) {
		try {
			AuthResponseDto response = null;
			Keycloak keycloak = KeycloakBuilder.builder().serverUrl(serverUrl).realm(realm)
					.username(request.getUsername()).password(request.getPassword()).clientId(clientId)
					.clientSecret(clientSecret).build();
			AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();
			if (accessTokenResponse != null) {
				log.info("Access token generated.");
				response = new AuthResponseDto(accessTokenResponse.getToken(), accessTokenResponse.getExpiresIn(),
						accessTokenResponse.getRefreshExpiresIn(), accessTokenResponse.getRefreshToken(),
						accessTokenResponse.getTokenType(), accessTokenResponse.getIdToken(),
						accessTokenResponse.getSessionState(), accessTokenResponse.getScope());
			}
			return response;
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			Gson gson = new Gson();
			if (e instanceof HttpClientErrorException) {
				HttpClientErrorException httpException = (HttpClientErrorException) e;
				ErrorModel errorModel = gson.fromJson(httpException.getResponseBodyAsString(), ErrorModel.class);
				throw new SSGMSException(ErrorCodes.BAD_REQUEST, errorModel.getError_description());
			} else
				throw new SSGMSException(ErrorCodes.INTERNAL_SERVER_ERROR, Translator.toLocale("login.failed", null));
		}
	}
	
	@Override
	public AuthResponseDto refreshTokenGenerate(RefreshTokenRequestDto request) {
		try {
			MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
			multiValueMap.add(SSConstants.GRANT_TYPE, SSConstants.REFRESH_TOKEN);
			multiValueMap.add(SSConstants.CLIENT_ID, clientId);
			multiValueMap.add(SSConstants.CLIENT_SECRET, clientSecret);
			multiValueMap.add(SSConstants.REFRESH_TOKEN, request.getRefreshToken());			
			
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(multiValueMap, null);
			return restTemplate.postForObject(keycloakTokenUri, httpEntity, AuthResponseDto.class);
		}catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			Gson gson = new Gson();
			if (e instanceof HttpClientErrorException) {
				HttpClientErrorException httpException = (HttpClientErrorException) e;
				ErrorModel errorModel = gson.fromJson(httpException.getResponseBodyAsString(), ErrorModel.class);
				throw new SSGMSException(ErrorCodes.BAD_REQUEST, errorModel.getError_description());
			} else
				throw new SSGMSException(ErrorCodes.INTERNAL_SERVER_ERROR, Translator.toLocale("refresh.token.failed", null));
		}
	}
	
	@Override
	public String logout(RefreshTokenRequestDto request) {
		try {
			MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
			multiValueMap.add(SSConstants.CLIENT_ID, clientId);
			multiValueMap.add(SSConstants.CLIENT_SECRET, clientSecret);
			multiValueMap.add(SSConstants.REFRESH_TOKEN, request.getRefreshToken());			
			
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(multiValueMap, null);
			ResponseEntity<String> response = restTemplate.exchange(keycloakLogout, HttpMethod.POST, httpEntity, String.class);
			log.info("STATUS CODE " +response.getStatusCodeValue());
			return (response.getStatusCodeValue() == 204) ? "Successfully logout" : "Logout failed";
		}catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			throw new SSGMSException(ErrorCodes.INTERNAL_SERVER_ERROR, Translator.toLocale("logout.failed", null));
		}
	}
}
