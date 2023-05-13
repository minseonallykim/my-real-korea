package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.notice.Notice;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.exception.NoticeNotFoundException;
import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.service.notice.NoticeService;
import com.itwill.my_real_korea.util.PageMakerDto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
public class NoticeController {

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private CityService cityService;

	// 공지사항 리스트 보기 (공지사항 첫 화면)
	@GetMapping(value = "/notice-list")
	public String notice_list(@RequestParam(required = false, defaultValue = "1") int pageNo, Model model,
			HttpSession session) {

		try {
			if (session != null) {
				User loginUser = (User) session.getAttribute("loginUser");
				model.addAttribute("loginUser", loginUser);
			}
			PageMakerDto<Notice> noticeListPage = noticeService.selectAll(pageNo);
			System.out.println(noticeListPage.getPageMaker().getCurPage());
			List<Notice> noticeList = noticeListPage.getItemList();
			model.addAttribute("noticeListPage", noticeListPage);
			model.addAttribute("noticeList", noticeList);
			model.addAttribute("pageNo", pageNo);

			List<City> cityList = cityService.findAllCity();
			model.addAttribute("cityList", cityList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "notice-list";
	}

	// 공지사항 1개 보기
	@GetMapping(value = "/notice-detail")
	public String notice_detail(@RequestParam int nNo, Model model, HttpSession session) throws Exception {

		try {
			if (session != null) {
				User loginUser = (User) session.getAttribute("loginUser");
				model.addAttribute("loginUser", loginUser);
			}
			Notice notice = noticeService.selectByNo(nNo);
			noticeService.increaseReadCount(nNo);
			model.addAttribute("notice", notice);
			model.addAttribute("nNo", nNo);
			List<City> cityList = cityService.findAllCity();
			model.addAttribute("cityList", cityList);
		} catch (NoticeNotFoundException e) {
			model.addAttribute("msg", e.getMessage());
			return "redirect:notice-list";
		}
		return "notice-detail";
	}

	// 공지사항 작성 폼
	@AdminCheck
	@LoginCheck
	@GetMapping("/notice-write-form")
	public String notice_write_form(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);

		return "notice-write-form";
	}

	// 공지사항 수정 폼
	@AdminCheck
	@LoginCheck
	@GetMapping("/notice-modify-form")
	public String notice_modify_form(@RequestParam int nNo, Model model, HttpSession session) {
		try {
			User loginUser = (User) session.getAttribute("loginUser");
			Notice notice = noticeService.selectByNo(nNo);
			model.addAttribute("notice", notice);
			model.addAttribute("loginUser", loginUser);
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:notice-list";
		}
		return "notice-modify-form";
	}
}
