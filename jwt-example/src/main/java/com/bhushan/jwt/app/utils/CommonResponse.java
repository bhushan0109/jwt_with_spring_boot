package com.bhushan.jwt.app.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {

	public Integer status_code;
	public String message;
	public Object data;

	public CommonResponse(Integer status_code, String message) {
		this.status_code = status_code;
		this.message = message;
	}

}