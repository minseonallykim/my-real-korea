package com.itwill;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class MyRealKoreaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyRealKoreaApplication.class, args);
	}
	//@PostConstruct 는 Bean이 초기화된 후에 단 한번만 호출되는 빈 생명주기를 통해 TimeZone을 자동설정
	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

}
