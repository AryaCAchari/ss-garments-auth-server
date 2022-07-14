package com.ssgms.auth.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssgms.auth.component.Translator;
import com.ssgms.auth.dto.request.AuthRequestDto;
import com.ssgms.auth.dto.request.RefreshTokenRequestDto;
import com.ssgms.auth.dto.response.AuthResponseDto;
import com.ssgms.auth.service.AuthService;
import com.ssgms.auth.utilities.ResponseHelper;
import com.ssgms.auth.utilities.SSGMSResultResponse;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	@SuppressWarnings("unchecked")
	public SSGMSResultResponse<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
		AuthResponseDto response = authService.login(request);
		return ResponseHelper.createResponse(new SSGMSResultResponse<AuthResponseDto>(), response,
				Translator.toLocale("login.success", null), Translator.toLocale("login.failed", null));
	}

	@PostMapping("/refresh-token")
	@SuppressWarnings("unchecked")
	public SSGMSResultResponse<AuthResponseDto> refreshTokenGenerate(
			@Valid @RequestBody RefreshTokenRequestDto request) {
		AuthResponseDto response = authService.refreshTokenGenerate(request);
		return ResponseHelper.createResponse(new SSGMSResultResponse<AuthResponseDto>(), response,
				Translator.toLocale("refresh.token.success", null), Translator.toLocale("refresh.token.failed", null));
	}

	@PostMapping("/logout")
	@SuppressWarnings("unchecked")
	public SSGMSResultResponse<String> logout(@Valid @RequestBody RefreshTokenRequestDto request) {
		String response = authService.logout(request);
		return ResponseHelper.createResponse(new SSGMSResultResponse<String>(), response,
				Translator.toLocale("logout.success", null), Translator.toLocale("logout.failed", null));
	}
}
