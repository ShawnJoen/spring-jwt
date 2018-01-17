package com.jwt.spring_jwt.interceptors;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.alibaba.fastjson.JSONObject;
import com.jwt.spring_jwt.Common;
import com.jwt.spring_jwt.TestVO;
import com.jwt.spring_jwt.service.UserService;

public class BaseApiInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(BaseApiInterceptor.class);
	
	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			String token = request.getHeader("token");
			if (StringUtils.isBlank(token)) {
				String token1 = request.getParameter("token");
				if (StringUtils.isBlank(token1)) {
					
					writeErrorMsg(response, 
							Common.output("-1", null, "请求失败！缺少token参数！"));
					return false;
				}
				
				token = token1;
			}

			TestVO userInfo = userService.checkToken("APP", "APP", token);
			if (userInfo != null) {

				logger.info("userInfo:{}", userInfo);
			} else {

				writeErrorMsg(response, 
						Common.output("-2", null, "鉴权失败！"));
				return false;
			}
		}
		
		return super.preHandle(request, response, handler);
	}

	public void writeErrorMsg(HttpServletResponse response, Object obj) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		logger.info("Error Messages:{}", obj);
		PrintWriter writer = response.getWriter();
		writer.print(JSONObject.toJSONString(obj));
	}

	/**
	 * 该方法将在Controller执行之后，返回视图之前执行，ModelMap表示请求Controller处理之后返回的Model对象，所以可以在
	 * 这个方法中修改ModelMap的属性，从而达到改变返回的模型的效果。
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}
	
	/**
	 * 整个请求完成之后，也就是说在视图渲染之后进行调用，用于进行一些资源的释放
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}
	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}
}