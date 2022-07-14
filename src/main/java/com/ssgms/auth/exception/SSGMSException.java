package com.ssgms.auth.exception;

import com.ssgms.auth.enums.ErrorCodes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSGMSException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Integer errorCode;
	private String message;

	public SSGMSException(String message) {
		super(message);
		this.message = message;
	}

	public SSGMSException(Integer error, String message) {
		super(message);
		this.errorCode = error;
		this.message = message;
	}

	public SSGMSException(Integer error, Exception ex) {
		super(ex);
		this.errorCode = error;
		this.message = ex.getMessage();
	}

	public SSGMSException(Exception ex) {
		super(ex);
		this.message = ex.getMessage();
	}

	public SSGMSException(ErrorCodes errorCode, String message) {
		super(message);
		this.errorCode = errorCode.getCode();
		this.message = message;
	}

	public SSGMSException(ErrorCodes errorCode, Exception ex) {
		super(ex);
		this.errorCode = errorCode.getCode();
		this.message = ex.getCause().getMessage();

	}

}
