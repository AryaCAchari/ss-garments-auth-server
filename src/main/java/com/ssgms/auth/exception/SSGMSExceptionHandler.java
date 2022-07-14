package com.ssgms.auth.exception;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ssgms.auth.enums.ErrorCodes;

@ControllerAdvice
@RestController
public class SSGMSExceptionHandler {
	
	/**
	 * Returns custom error response on occurrence of custom exception.
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = SSGMSException.class)
	public ErrorResponse handleContentNotFoundException(SSGMSException e, HttpServletResponse response) {
		response.setStatus(e.getErrorCode());
		ErrorResponse error = new ErrorResponse();
		error.setMessage(e.getMessage());
		error.setErrorCode(e.getErrorCode());
		error.setSuccess(false);
		return error;
	}	

	/**
	 * Returns custom error response on occurrence spring validation exception.
	 *
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		ErrorResponse error = new ErrorResponse();
		StringBuilder builder = new StringBuilder();
		for (FieldError fError : fieldErrors) {
			if (!builder.toString().isEmpty()) {
				builder.append(", ");
			}
			builder.append(fError.getField());
			builder.append(" ");
			builder.append(fError.getDefaultMessage());
		}
		error.setErrorCode(ErrorCodes.BAD_REQUEST.getCode());
		error.setMessage(builder.toString());
		error.setSuccess(false);
		return error;
	}
}
