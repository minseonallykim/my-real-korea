package com.itwill.my_real_korea.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

// @ConfigurationProperties : 자동으로 application.properties 와 클래스 연결
@ConfigurationProperties("file")
public class FileUploadProperties {

	/*
	 * 업로드 된 파일이 저장될 경로
	 */
	private String location = "upload-dir";

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
