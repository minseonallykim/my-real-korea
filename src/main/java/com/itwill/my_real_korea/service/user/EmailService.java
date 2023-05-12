package com.itwill.my_real_korea.service.user;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private int authNum; //랜덤 인증 코드
    private String tempPassword;

    //이메일 인증 코드 생성(6자리 숫자)
    public void createAuthCode() {
    	authNum = 0;
        Random random = new Random();
        for(int i=0; i<6; i++) {
            authNum = authNum * 10 + random.nextInt(10);
        }
    }
    
    //임시 비밀번호 생성(8자리 영문자+숫자)
    public void createTempPassword(){
		StringBuffer key = new StringBuffer();
		Random random = new Random();
		tempPassword = "";
		for (int i=0; i<8; i++) { //임시 비밀번호 8자리
			int index = random.nextInt(3); // 0~2 까지 랜덤

			switch (index) {
			case 0:
				key.append((char) ((int) (random.nextInt(26)) + 97));
				//  a~z  (ex. 1+97=98 => (char)98 = 'b')
				break;
			case 1:
				key.append((char) ((int) (random.nextInt(26)) + 65));
				//  A~Z 
				break;
			case 2:
				key.append((random.nextInt(10)));
				// 0~9
				break;
			}
		}
		tempPassword = key.toString();
    }
    
    
    //1. 이메일 인증코드 발송
    public int sendAuth(String toEmail) throws MessagingException, UnsupportedEncodingException {
    	createAuthCode(); // 인증 코드 생성

        //메일 전송에 필요한 정보 설정 (인증번호 입력하는 방식)
        String setFrom = "minsun947@gmail.com";
        String title = "My Real Korea 회원가입 인증";

        MimeMessage emailForm = emailSender.createMimeMessage();
        emailForm.addRecipients(MimeMessage.RecipientType.TO, toEmail);	//수신자 설정
        emailForm.setSubject(title);	//제목 설정
        emailForm.setFrom(setFrom);		//발신자 설정
        emailForm.setText(setAuthContext(authNum), "utf-8", "html");

        emailSender.send(emailForm);
        return authNum;	//인증 코드 반환
    }
    
    //2. 임시 비밀번호 발송
    public String sendTempPassword(String toEmail) throws MessagingException, UnsupportedEncodingException {
        createTempPassword(); //임시 비밀번호 생성

        String setFrom = "minsun947@gmail.com";
        String title = "My Real Korea 임시 비밀번호 발송";

        MimeMessage emailForm = emailSender.createMimeMessage();
        emailForm.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        emailForm.setSubject(title);
        emailForm.setFrom(setFrom);
        emailForm.setText(setPasswordContext(tempPassword), "utf-8", "html");

        emailSender.send(emailForm);
        return tempPassword;	//임시 비밀번호 반환
    }
        
        
    //타임리프를 이용한 context 설정
    public String setAuthContext(int authNum) {
        Context context = new Context();
        context.setVariable("authNum", authNum);
        return templateEngine.process("user-mail-auth", context);	//mail-auth.html
    }
    
    public String setPasswordContext(String tempPassword) {
    	Context context = new Context();
    	context.setVariable("tempPassword", tempPassword);
    	return templateEngine.process("user-mail-temp-pw", context);	//mail-temp-password.html
    }
}
