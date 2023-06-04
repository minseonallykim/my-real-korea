package com.itwill.my_real_korea.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.itwill.my_real_korea.dto.user.UserAddInfo;
import com.itwill.my_real_korea.service.user.UserAddInfoService;

@Controller
public class UserAddInfoController {
	
	@Autowired
	private UserAddInfoService userAddInfoService;
	
	@LoginCheck
	@PostMapping("user-add-modify-action")
	public String user_add_modify_action(@ModelAttribute UserAddInfo userAddInfo,HttpServletRequest request) throws Exception {
		userAddInfoService.updateUserAddInfo(userAddInfo);
		return "redirect:user-view";
	}
}





