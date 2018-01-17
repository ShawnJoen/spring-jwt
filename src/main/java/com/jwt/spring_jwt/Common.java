package com.jwt.spring_jwt;

import java.util.HashMap;
import java.util.Map;

public class Common {
	
	final public static Map<String, Object> output(String code, Object object, String message) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("data", object);
		map.put("message", message);
		return map;
	}
}