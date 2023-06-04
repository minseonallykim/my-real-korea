package com.itwill.my_real_korea.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.my_real_korea.dto.tripboard.TripBoardComment;
import com.itwill.my_real_korea.service.tripboard.TripBoardCommentService;


@Controller
public class TripBoardCommentController {

	@Autowired
	private TripBoardCommentService tripBoardCommentService;
	
	@LoginCheck
	@PostMapping("/tripboard-comment-write-action")
	public String tripBoardCommentWriteAction(@RequestParam Integer tBoNo, TripBoardComment tripBoardComment, RedirectAttributes redirecAttributes) throws Exception {
		try {
			tripBoardCommentService.insertTripBoardComment(tripBoardComment);
			redirecAttributes.addAttribute("tBoNo",tBoNo);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "redirect:tripboard-detail";
	}
}
