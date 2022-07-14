package com.ssgms.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AuthResponseDto {
	
	private String access_token;

    private long expires_in;

    private long refresh_expires_in;

    private String refresh_token;

    private String token_type;

    private String id_token;

    private String session_state;
    
    private String scope;
}
