package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.tripboard.TripBoard;
import com.itwill.my_real_korea.dto.tripboard.TripBoardComment;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.service.tripboard.TripBoardCommentService;
import com.itwill.my_real_korea.service.tripboard.TripBoardService;
import com.itwill.my_real_korea.util.PageMakerDto;

@Controller
public class TripBoardController {

	private TripBoardService tripBoardService;
	private TripBoardCommentService tripBoardCommentService;
	private CityService cityService;

	@Autowired
	public TripBoardController(TripBoardService tripBoardService, TripBoardCommentService tripBoardCommentService,
			CityService cityService) {
		this.tripBoardService = tripBoardService;
		this.tripBoardCommentService = tripBoardCommentService;
		this.cityService = cityService;
	}

	// 동행게시판 리스트
	@GetMapping("/tripboard-list")
	public String tripBooard_list(@RequestParam(required = false, defaultValue = "1") int pageNo, Model model) {

		try {
			PageMakerDto<TripBoard> tripBoardListPage = tripBoardService.selectAllTb(pageNo);

			List<TripBoard> tempTripBoardList = tripBoardListPage.getItemList();
			List<TripBoard> tripBoardList = new ArrayList<>();

			for (TripBoard tripBoard : tempTripBoardList) {
				int commentCount = tripBoardCommentService.selectAllByTBoNo(tripBoard.getTBoNo()).size();
				tripBoard.setCommentCount(commentCount);
				tripBoardList.add(tripBoard);
			}

			List<City> cityList = cityService.findAllCity();

			model.addAttribute("tripBoardListPage", tripBoardListPage);
			model.addAttribute("tripBoardList", tripBoardList);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("cityList", cityList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "tripboard-list";
	}

	// 동행게시판 게시글 1개 상세보기
	@GetMapping("/tripboard-detail")
	public String tripBoard_detail(@RequestParam Integer tBoNo, Model model, HttpSession session) throws Exception {
		if (tBoNo == null) {
			return "tripboard-list";
		}
		try {
			if (session != null) {
				User loginUser = (User) session.getAttribute("loginUser");
				model.addAttribute("loginUser", loginUser);
			}
			TripBoard tripBoard = tripBoardService.selectByTbNo(tBoNo);
			List<TripBoardComment> tripBoardCommentList = tripBoardCommentService.selectBytBoNo(tBoNo);
			tripBoardService.increaseTbReadCount(tripBoard.getTBoNo());
			int selectCommentCount = tripBoardCommentService.selectCommentCount(tBoNo);

			model.addAttribute("tripBoard", tripBoard);
			model.addAttribute("tripBoardCommentList", tripBoardCommentList);
			model.addAttribute("tBoNo", tBoNo);
			model.addAttribute("selectCommentCount", selectCommentCount);

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "tripboard-detail";
	}

	// 동행게시판 작성 폼
	@LoginCheck
	@GetMapping("/tripboard-write-form")
	public String tripBoardWriteForm() {
		return "tripboard-write-form";
	}

	// 동행게시판 수정 폼
	@LoginCheck
	@GetMapping("/tripboard-modify-form")
	public String tripBoardModifyForm(Integer tBoNo, Model model) throws Exception {
		if (tBoNo == null) {
			return "tripboard-list";
		}
		try {
			TripBoard tripBoard = tripBoardService.selectByTbNo(tBoNo);
			model.addAttribute("tripBoard", tripBoard);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "tripboard-modify-form";
	}

	// 상단바 지역별 동행게시판
	@GetMapping("/tripboard-city-bar-list")
	public String tripBoardCityBarList(@RequestParam(required = false, defaultValue = "1") int pageNo, int cityNo,
			Model model) {
		try {
			PageMakerDto<TripBoard> tripBoardListPage = tripBoardService.selectByCityNoList(pageNo, cityNo);
			List<TripBoard> tripBoardList = tripBoardListPage.getItemList();
			List<City> cityList = cityService.findAllCity();

			model.addAttribute("tripBoardListPage", tripBoardListPage);
			model.addAttribute("tripBoardList", tripBoardList);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("cityList", cityList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "tripboard-city-bar-list";
	}

}
