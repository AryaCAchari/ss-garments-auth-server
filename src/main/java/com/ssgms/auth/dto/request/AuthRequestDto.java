package com.ssgms.auth.dto.request;

import javax.validation.constraints.NotBlank;

import com.ssgms.auth.utilities.SSConstants;

import lombok.Data;

@Data
public class AuthRequestDto {
	
	@NotBlank(message = SSConstants.MANDATORY)
	private String username;
	
	@NotBlank(message = SSConstants.MANDATORY)
	private String password;
}
