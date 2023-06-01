package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itwill.my_real_korea.dto.Payment;
import com.itwill.my_real_korea.dto.RsPInfo;
import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.service.payment.PaymentService;
import com.itwill.my_real_korea.service.rspinfo.RsPInfoService;
import com.itwill.my_real_korea.service.ticket.TicketService;
import com.itwill.my_real_korea.service.tour.TourService;

@RestController
public class PaymentRestController {
	private PaymentService paymentService;
	private RsPInfoService rsPInfoService;
	private TourService tourService;
	private TicketService ticketService;

	public PaymentRestController(PaymentService paymentService, RsPInfoService rsPInfoService, TourService tourService,
			TicketService ticketService) {
		this.paymentService = paymentService;
		this.rsPInfoService = rsPInfoService;
		this.tourService = tourService;
		this.ticketService = ticketService;
	}

	// 1. 구매상품 삭제
	@LoginCheck
	@DeleteMapping(value = "/payment-delete/{pNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> paymentDelete(@PathVariable int pNo, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 0;
		String msg = "";
		List<Payment> data = new ArrayList<>();
		User loginUser = (User) session.getAttribute("loginUser");
		try {
			Payment payment = paymentService.selectPaymentNo(pNo);
			Tour tour = payment.getTour();
			Ticket ticket = payment.getTicket();
			paymentService.deletePayment(pNo);
			// 삭제하면 구매갯수 날리기
			if (tour != null) {
				tour.setToCount(tour.getToCount() - payment.getPQty());
				tourService.updateTour(tour);
			}
			List<Payment> paymentList = paymentService.selectAllUser(loginUser.getUserId());
			data = paymentList;
			code = 1;
			msg = "성공";
		} catch (Exception e) {
			e.printStackTrace();
			code = 2;
			msg = "실패";
		}
		resultMap.put("data", data);
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		return resultMap;
	}

}
