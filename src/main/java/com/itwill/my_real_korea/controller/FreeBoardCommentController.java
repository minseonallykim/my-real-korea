package com.itwill.my_real_korea.controller;

import com.itwill.my_real_korea.dto.freeboard.FreeBoardComment;
import com.itwill.my_real_korea.service.freeboard.FreeBoardCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FreeBoardCommentController {
	@Autowired
	private FreeBoardCommentService freeBoardCommentService;

	@LoginCheck
	@PostMapping("/freeboard-comment-write-action")
	public String freeBoardCommentWriteAction(@RequestParam Integer fBoNo, FreeBoardComment freeBoardComment,
			RedirectAttributes redirectAttributes) throws Exception {
		try {
			freeBoardCommentService.insertComment(freeBoardComment);
			redirectAttributes.addAttribute("fBoNo", fBoNo);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "redirect:freeboard-detail";
	}

	@LoginCheck
	@GetMapping("/freeboard-comment-modify-form")
	public String freeBoardCommentModifyForm(Integer fCoNo, Model model) throws Exception {
		try {
			FreeBoardComment freeBoardComment = freeBoardCommentService.selectByfCoNo(fCoNo);
			model.addAttribute("freeBoardComment", freeBoardComment);

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "freeboard-comment-modify-form";
	}

	@LoginCheck
	@PostMapping("/freeboard-comment-modify-action")
	public String freeBoardCommentModifyAction(@ModelAttribute FreeBoardComment freeBoardComment,
			RedirectAttributes redirectAttributes) throws Exception {
		freeBoardCommentService.updateComment(freeBoardComment);
		redirectAttributes.addAttribute("fBoNo", freeBoardComment.getFBoNo());
		return "redirect:freeboard-detail";
	}

	@PostMapping("/freeboard-comment-delete-action")
	public String freeBoardCommentDeleteAction(@RequestParam Integer fCoNo, @RequestParam Integer fBoNo,
			RedirectAttributes redirectAttributes) throws Exception {
		int deleteComment = freeBoardCommentService.deleteComment(fCoNo);
		System.out.println("deleteComment = " + deleteComment);
		redirectAttributes.addAttribute("fBoNo", fBoNo);
		return "redirect:freeboard-detail";
	}

}
