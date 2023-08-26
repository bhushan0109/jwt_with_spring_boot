package com.bhushan.jwt.app.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorCodeEnum {

	EXCEPTION_OCCURRED(1002, "Exception Occurred in jwt Service");

	@Getter
	private Integer id;

	@Getter
	private String value;

	public static ErrorCodeEnum findByValue(String value) {
		for (ErrorCodeEnum order : ErrorCodeEnum.values()) {
			if (order.getValue().equalsIgnoreCase(value)) {
				return order;
			}
		}
		return null;
	}
}



