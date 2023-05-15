package com.itwill.my_real_korea.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.itwill.my_real_korea.controller.AuthAdminAnnotationInterceptor;
import com.itwill.my_real_korea.controller.AuthLoginAnnotationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	/********************WebMvcConfigurer재정의*********************
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.jsp");
	}
	*************************************************************/
    //web root가 아닌 외부 경로에 있는 리소스를 url로 불러올 수 있도록 설정
    //fileAdd에 실질적인 파일저장위치를 설정해줍니다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/Temp/");
    }
	
	/*********************Interceptor 등록*********************/
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		// @LoginCheck
		AuthLoginAnnotationInterceptor authLoginAnnotationInterceptor = new AuthLoginAnnotationInterceptor();
		registry.addInterceptor(authLoginAnnotationInterceptor)
		.addPathPatterns("/**") // 모든 요청을 처리하도록 등록 후
		.excludePathPatterns("/css/**") // interceptor 의 영향을 받지 않아야 하는 것들 설정
		.excludePathPatterns("/js/**")
		.excludePathPatterns("/image/**");
		
		// @AdminCheck
		AuthAdminAnnotationInterceptor authAdminAnnotationInterceptor = new AuthAdminAnnotationInterceptor();
		registry.addInterceptor(authAdminAnnotationInterceptor)
		.addPathPatterns("/**") // 모든 요청을 처리하도록 등록 후
		.excludePathPatterns("/css/**") // interceptor 의 영향을 받지 않아야 하는 것들 설정
		.excludePathPatterns("/js/**")
		.excludePathPatterns("/image/**");
		
	}
}
