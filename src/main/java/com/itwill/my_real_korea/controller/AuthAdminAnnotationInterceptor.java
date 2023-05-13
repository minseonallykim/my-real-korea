package com.itwill.my_real_korea.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.exception.IsNotAdminException;
import com.itwill.my_real_korea.service.user.UserService;

import ch.qos.logback.core.joran.conditional.IfAction;
/*
 * 관리자 체크하는 interceptor
 */
public class AuthAdminAnnotationInterceptor implements HandlerInterceptor {
	public AuthAdminAnnotationInterceptor() {
	}

	@Override
	public boolean preHandle(	HttpServletRequest request, 
								HttpServletResponse response, 
								Object handler) throws Exception {
		
		// HandlerMethod : @Controller 객체에 @RequestMapping이 붙은 메소드
		if (handler instanceof HandlerMethod == false) {
			return true;
		}
		
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		// HandlerMethod 객체에 @AdminCheck 이 없는 경우, 인증이 필요 없는 요청
		AdminCheck adminCheck = handlerMethod.getMethodAnnotation(AdminCheck.class);
		if (adminCheck == null) {
			return true;
		}
		
		// HandlerMethod 객체에 @AdminCheck 이 있는 경우, 세션이 있는지 체크
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		//Referer : HTTP 요청 헤더의 일종으로, 요청 보낸 페이지의 URL 의미
		String requestUrl = request.getHeader("Referer");
		// session 에 요청 보낸 페이지의 URL 저장(관리자 아닐 경우 이전 페이지로 돌려보내기 위해)
		request.getSession().setAttribute("requestUrl", requestUrl);
		
		// loginUser.getUserId() 가 admin 이 아니고 요청URL이 존재할 때, 요청 URL로 돌려보냄
		if (!loginUser.getUserId().equals("admin")) {
			if (requestUrl != null) {
				session.removeAttribute("requestUrl");
				response.sendRedirect(requestUrl);
			} else {
				// 요청 URL 없을 때 메인으로 이동
				response.sendRedirect("index");
			}
			return false;
		} 

		// true : 컨트롤러의 uri로 이동
		return true;
	}

}
	



