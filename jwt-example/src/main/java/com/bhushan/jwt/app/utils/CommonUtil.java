package com.bhushan.jwt.app.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class CommonUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);
	private static String LOCALHOST_IPV4 = "127.0.0.1";
	private static String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
	public static final String SUCCESS = "SUCCESS";
	public static final String NOT_FOUND = "NOT FOUND";
	public static final String successMessage = "Data Retrived Successfully";
	public static final String SOMETHING_WENT_WRONG = "Internal Server Error";

	public static void entryLog(String methodName) {
		LOGGER.info("Entry in " + methodName);
	}

	public static void exitLog(String methodName) {
		LOGGER.info("Exit from " + methodName);
	}

	public static void exceptionLog(Exception e) {
		LOGGER.error("Exception :  ", e);
	}

	public static void clientUrlLog(String url) {
		LOGGER.info("Client Called Url :  " + url);
	}



	public static Boolean isNullOrEmpty(String string) {
		return (string == null || string.isEmpty() || string.length() == 0);
	}

	public static String trim(String string) {
		if (!isNullOrEmpty(string)) {
			return string.trim();
		}
		return null;
	}

	public static String captializeAllFirstLetter(String str) {
		String trimName = str.trim();
		String words[] = trimName.split("\\s");
		String capitalizeStr = "";

		for (String word : words) {
			if (word.isEmpty()) {
				continue;
			}
			// Capitalize first letter
			String firstLetter = word.substring(0, 1);
			// Get remaining letter
			String remainingLetters = word.substring(1).toLowerCase();
			capitalizeStr += firstLetter.toUpperCase() + remainingLetters + " ";
		}
		System.out.println(capitalizeStr);

		return capitalizeStr.trim();
	}

	public static CommonResponse errorResponse(Exception e) {
		ErrorCodeEnum errorCodeEnum = ErrorCodeEnum.findByValue(e.getMessage());
		if (errorCodeEnum != null) {
			return new CommonResponse(errorCodeEnum.getId(), errorCodeEnum.getValue(), null);
		} else {
			CommonUtil.exceptionLog(e);
		}
		return new CommonResponse(ErrorCodeEnum.EXCEPTION_OCCURRED.getId(), ErrorCodeEnum.EXCEPTION_OCCURRED.getValue(),
				e.getMessage());
	}

	@SuppressWarnings("deprecation")
	public static String getClientIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}

		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}

		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
				try {
					InetAddress inetAddress = InetAddress.getLocalHost();
					ipAddress = inetAddress.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}

		if (!StringUtils.isEmpty(ipAddress) && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
			ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
		}
		return ipAddress;
	}

}