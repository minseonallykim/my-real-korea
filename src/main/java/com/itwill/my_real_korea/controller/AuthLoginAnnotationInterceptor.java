package com.itwill.my_real_korea.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.itwill.my_real_korea.dto.user.User;
/*
 * 로그인 체크하는 interceptor
 */
public class AuthLoginAnnotationInterceptor implements HandlerInterceptor {
	public AuthLoginAnnotationInterceptor() {
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
		
		// HandlerMethod 객체에 @LoginCheck 이 없는 경우, 인증이 필요 없는 요청
		LoginCheck loginCheck = handlerMethod.getMethodAnnotation(LoginCheck.class);
		if (loginCheck == null) {
			return true;
		}
		
		// HandlerMethod 객체에 @LoginCheck 이 있는 경우, 세션이 있는지 체크
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		//Referer : HTTP 요청 헤더의 일종으로, 요청 보낸 페이지의 URL 의미
		String requestUrl = request.getHeader("Referer");
		// session 에 요청 보낸 페이지의 URL 저장(관리자 아닐 경우 이전 페이지로 돌려보내기 위해)
		request.getSession().setAttribute("requestUrl", requestUrl);
		
		if (loginUser == null) {
			// 로그인이 안되어 있는 상태, 로그인 폼으로 다시 돌려보냄
		    session.setAttribute("requestUrl", requestUrl);
		    response.sendRedirect("user-login");
		    return false;
		} 

		return true;
	}

   
}
	



