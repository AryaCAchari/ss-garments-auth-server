package com.ssgms.auth.exception;

import lombok.Data;

@Data
public class ErrorModel {
	
	private String error;
	
	private String error_description;
}
