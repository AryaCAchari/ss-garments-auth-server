package com.ssgms.auth.dto.request;

import javax.validation.constraints.NotBlank;

import com.ssgms.auth.utilities.SSConstants;

import lombok.Data;

@Data
public class RefreshTokenRequestDto {
	
	@NotBlank(message = SSConstants.MANDATORY)
	private String refreshToken;
}
