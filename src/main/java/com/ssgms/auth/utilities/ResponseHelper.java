package com.ssgms.auth.utilities;

import com.ssgms.auth.enums.ErrorCodes;
import com.ssgms.auth.exception.SSGMSException;

public class ResponseHelper<T> {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static SSGMSResultResponse createResponse(SSGMSResultResponse response, Object data,
			String successMessage, String errorMessage) {
		if (data != null) {
			response.setSuccess(true);
			response.setData(data);
			response.setMessage(successMessage);
		} else {
			throw new SSGMSException(ErrorCodes.INTERNAL_SERVER_ERROR, errorMessage);
		}
		return response;
	}
}