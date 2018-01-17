package com.jwt.spring_jwt.service;

import java.util.HashMap;
import java.util.Map;

import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jwt.spring_jwt.Common;
import com.jwt.spring_jwt.TestVO;
import com.jwt.spring_jwt.utils.EncryptUtils;
import com.jwt.spring_jwt.utils.JwtUtils;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private JwtUtils jwtUtils;
	
	/**
	 * 登入

	 * @param mobile
	 * @param password
	 * @return
	 */
	@Override
	public Map<String, Object> login(String mobile, String password) {

		TestVO userVo = new TestVO();
		userVo.setUserId(999);
		userVo.setMobile(mobile);
		userVo.setName("测试账号");
		userVo.setPassword("413cb80b735234c64f8627eaa390da00");//123456
		//TestVO userVo = UserDao.getByMobile(mobile);
		
		String userIdStr = String.valueOf(userVo.getUserId());
		if (!userVo.getPassword().equals(
					EncryptUtils.encodeMD5String(password, userIdStr)
				)) {
			return Common.output("1", null, "登入密码错误！");
		}
		
		Map<String, Object> payLoadMap = new HashMap<String, Object>();
		payLoadMap.put("userId", userIdStr);
		payLoadMap.put("mobile", mobile);
		payLoadMap.put("name", userVo.getName());
		try {
			//设置了生成到失效1分钟
			String token = jwtUtils.generateToken("APP", "APP", Float.valueOf(1), payLoadMap);
			userVo.setToken(token);
			userVo.setPassword("");
			
			return Common.output("0", userVo, "");
		} catch (JoseException e) {
			logger.error("token生成异常:", e);
		}
		
		return Common.output("1", null, "登入失败！");
	}
	
	/**
	 * 鉴权
	 * 
	 * @param clientId
	 * @param token
	 * @return
	 */
	@Override
	public TestVO checkToken(String issUser ,String audience ,String token) {
		try {
			
			Map<String, Object> payLoadMap = jwtUtils.checkToken(issUser, issUser, token);
			if (payLoadMap.get("userId") != null) {

				int userId = Integer.parseInt(String.valueOf(payLoadMap.get("userId")));
    			TestVO userVo = new TestVO();
    			userVo.setUserId(userId);
    			//TestVO userVo = UserDao.getByUserId(userId);
    			
    			return userVo;
			}
		} catch (InvalidJwtException e) {
			logger.error("鉴权失败:", e);
		} catch (Exception e) {
			logger.error("解析token失败:", e);
		}
		
		return null;
	}
}