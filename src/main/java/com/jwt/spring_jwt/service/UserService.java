package com.jwt.spring_jwt.service;

import java.util.Map;
import com.jwt.spring_jwt.TestVO;

public interface UserService {
	Map<String, Object> login(String mobile, String password);
	TestVO checkToken(String issUser ,String audience ,String token);
}