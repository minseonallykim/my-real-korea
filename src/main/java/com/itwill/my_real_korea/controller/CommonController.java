package com.itwill.my_real_korea.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.service.city.CityService;

@Controller
public class CommonController {
	@Autowired
	private CityService cityService;

	// 메인 페이지
	@GetMapping("/index")
	public String index(Model model) {
		try {
			List<City> cityList = cityService.findAllCity();
			model.addAttribute("cityList", cityList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "index";
	}

	// 에러 페이지
	@GetMapping("/error")
	public String error() {
		return "error";
	}

}
