package com.ssgms.auth.service;

import org.springframework.stereotype.Service;

import com.ssgms.auth.dto.request.AuthRequestDto;
import com.ssgms.auth.dto.request.RefreshTokenRequestDto;
import com.ssgms.auth.dto.response.AuthResponseDto;

@Service
public interface AuthService {

	AuthResponseDto login(AuthRequestDto request);

	AuthResponseDto refreshTokenGenerate(RefreshTokenRequestDto request);

	String logout(RefreshTokenRequestDto request);

}
