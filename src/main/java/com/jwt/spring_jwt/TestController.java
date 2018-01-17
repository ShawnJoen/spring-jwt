package com.jwt.spring_jwt;

import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jwt.spring_jwt.service.UserService;
import com.jwt.spring_jwt.utils.RsaSha256Utils;

@RestController
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/rsaShaExport", method = RequestMethod.GET)
	public void rsaShaExport(Locale locale, Model model) throws Exception {
		RsaSha256Utils.export();
	}
	//http://127.0.0.1:8080/spring-jwt/login?mobile=15512341234&password=123456
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public Map<String, Object> login(Locale locale, Model model, @RequestParam("mobile") String mobile,
			@RequestParam("password") String password) {
		logger.info("mobile Number:{}", mobile);
		
		return userService.login(mobile, password);
	}
	//http://127.0.0.1:8080/spring-jwt/api/app/userInfo?token=
	@RequestMapping(value = "/api/app/userInfo", method = RequestMethod.GET)
	public Map<String, Object> userInfo(Locale locale, Model model) {

		return Common.output("0", null, "");
	}
}