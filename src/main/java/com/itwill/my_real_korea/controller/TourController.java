package com.itwill.my_real_korea.controller;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.Payment;
import com.itwill.my_real_korea.dto.RsPInfo;
import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.tour.TourImg;
import com.itwill.my_real_korea.dto.tour.TourReview;
import com.itwill.my_real_korea.dto.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwill.my_real_korea.service.city.CityService;
import com.itwill.my_real_korea.service.location.LocationService;
import com.itwill.my_real_korea.service.payment.PaymentService;
import com.itwill.my_real_korea.service.rspinfo.RsPInfoService;
import com.itwill.my_real_korea.service.tour.TourImgService;
import com.itwill.my_real_korea.service.tour.TourReviewService;
import com.itwill.my_real_korea.service.tour.TourService;
import com.itwill.my_real_korea.service.user.UserService;
import com.itwill.my_real_korea.util.PageMakerDto;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.nullValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class TourController {
	private TourService tourService;
	private TourImgService tourImgService;
	private CityService cityService;
	private TourReviewService tourReviewService;
	private PaymentService paymentService;
	private RsPInfoService rsPInfoService;
	private UserService userService;
	private LocationService locationService;

	@Autowired
	public TourController(TourService tourService, TourImgService tourImgService, CityService cityService,
			TourReviewService tourReviewService, PaymentService payService, RsPInfoService rsPInfoService,
			UserService userService, LocationService locationService) {
		this.tourService = tourService;
		this.tourImgService = tourImgService;
		this.cityService = cityService;
		this.tourReviewService = tourReviewService;
		this.paymentService = payService;
		this.rsPInfoService = rsPInfoService;
		this.userService = userService;
		this.locationService = locationService;
	}

	// 1. 투어상품 전체 리스트 보기
	@GetMapping(value = "/tour-list")
	public String tour_list(@RequestParam(required = false, defaultValue = "1") int pageNo,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "0") int cityNo,
			@RequestParam(required = false, defaultValue = "0") int toType,
			@RequestParam(required = false) String sortOrder, Model model, HttpSession session) {
		String forwardPath = "";
		System.out.println(">>>>>>>>>>>>" + pageNo);
		try {
			PageMakerDto<Tour> tourListPage = tourService.findAll(pageNo, keyword, cityNo, toType, sortOrder);
			List<Tour> tourList = new ArrayList<>();
			User loginUser = (User) session.getAttribute("loginUser");
			// 평점 평균 구하기
			for (Tour tour : tourListPage.getItemList()) {
				if (tourReviewService.findByToNo(tour.getToNo()).size() == 0) {
					// 후기가 없을 때
					tour.setToScore(0);
					tourList.add(tour);
				} else {
					// 후기가 있을 때
					int tourScore = tourReviewService.calculateTourScore(tour.getToNo());
					tour.setToScore(tourScore);
					tourList.add(tour); // tourList에 후기 평점 평균 붙이기
				}
			}
			tourListPage.setItemList(tourList);
			List<City> cityList = cityService.findAllCity();
			model.addAttribute("cityList", cityList);
			model.addAttribute("tourListPage", tourListPage);
			model.addAttribute("loginUser", loginUser);
			forwardPath = "tour-list";
		} catch (Exception e) {
			e.printStackTrace();
			forwardPath = "error";
		}
		return forwardPath;
	}

	// 2. 투어상품 상세보기
	@GetMapping(value = "/tour-detail", params = "toNo")
	public String tourDetail(@RequestParam int toNo, Model model, HttpSession session) {
		String forwardPath = "";
		try {
			User loginUser = (User) session.getAttribute("loginUser");
			model.addAttribute("loginUser", loginUser);
			Tour tour = tourService.findTourWithCityByToNo(toNo);
			if (tour != null) {
				int tourScore = tourReviewService.calculateTourScore(toNo);
				tour.setToScore(tourScore);
				tour.setLocation(locationService.findByToNo(toNo));
				model.addAttribute("tour", tour);
				forwardPath = "tour-detail";
			} else {
				forwardPath = "error";
			}
			List<TourReview> tourReviewList = tourReviewService.findByToNo(toNo);
			List<City> cityList = cityService.findAllCity();
			model.addAttribute("cityList", cityList);
			model.addAttribute("tourReviewList", tourReviewList);
		} catch (Exception e) {
			e.printStackTrace();
			forwardPath = "error";
		}
		return forwardPath;
	}

	// 3. 투어상품 예약하기(구매하기) 폼_session 이용 x(성공) ----> 모델? 세션? 어느 코드가 나을까?
	@LoginCheck
	@GetMapping(value = "/tour-payment")
	public String tourPaymentForm(@RequestParam String pStartDate, @RequestParam int pQty, @RequestParam int toNo,
			HttpSession session, Model model) {

		String forwardPath = "";
		try {
			Tour tour = tourService.findTourWithCityByToNo(toNo);
			tour.setTourImgList(tourImgService.findTourImgList(tour.getToNo()));
			User loginUser = (User) session.getAttribute("loginUser");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(pStartDate);
			System.out.println(date);
			Payment payment = new Payment();
			payment.setPPrice(pQty * (tour.getToPrice()));
			payment.setPQty(pQty);
			payment.setPStartDate(date);
			payment.setPPoint(pQty * (tour.getToPrice()) * 1 / 100);
			payment.setTour(tour);
			payment.setUserId(loginUser.getUserId());
			// 1. session에 붙이기
			session.setAttribute("payment", payment);
			session.setAttribute("tour", tour);
			System.out.println("야야야ㅑㅑㅑㅑㅑㅑㅑㅑ" + tour);
			// 2. model에 붙이기
			// model.addAttribute("payment",payment);
			forwardPath = "tour-payment";
		} catch (Exception e) {
			e.printStackTrace();
			forwardPath = "error";
		}

		return forwardPath;
	}

	// 3-1. 투어상품 예약하기(구매하기) 액션
	@LoginCheck
	@RequestMapping(value = "tour-payment-action")
	public String tourPaymentAction(@ModelAttribute RsPInfo rsPInfo,
			@RequestParam(required = false, defaultValue = "") String pMsg, @RequestParam int pMethod,
			@RequestParam int pPrice, @RequestParam int pPoint, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String forwardPath = "";
		try {
			User loginUser = (User) session.getAttribute("loginUser");
			Payment payment = (Payment) session.getAttribute("payment");
			Tour tour = (Tour) session.getAttribute("tour");

			// 페이지에서 받아온 값 payment에 넣어주기
			payment.setPMsg(pMsg);
			payment.setPMethod(pMethod);
			payment.setPPoint(pPoint);
			payment.setPPrice(pPrice);
			paymentService.insertTourPayment(payment);
			// 포인트 적립하기 ----> 사용한 만큼 빼야함
			int newPoint = loginUser.getPoint() + pPoint;
			loginUser.setPoint(newPoint);
			System.out.println(loginUser);
			userService.updatePoint(loginUser);
			// tour에 구매수량만큼 올리기
			tour.setToCount(tour.getToCount() + payment.getPQty());
			tourService.updateTour(tour);

			Tour updatedTour = tourService.findTourWithCityByToNo(tour.getToNo()); // 업데이트 된 투어 찾기
			Payment findPayment = paymentService.findLatestPaymentByUserId(loginUser.getUserId()); // insert된 payment 찾기
			findPayment.setTour(updatedTour); // payment에 업데이트 된 tour 붙이

			rsPInfo.setPNo(findPayment.getPNo());
			rsPInfo.setUserId(loginUser.getUserId());
			rsPInfoService.insertRsPerson(rsPInfo);

			// redirect하는 곳으로 붙여줄 객체 --> 객체 붙일깨는 FlashAttribute
			redirectAttributes.addFlashAttribute("payment", findPayment);
			redirectAttributes.addFlashAttribute("rsPInfo", rsPInfo);
			redirectAttributes.addFlashAttribute("tour", tour);

			session.removeAttribute("payment");
			session.removeAttribute("tour");

			forwardPath = "redirect:tour-payment-confirmation";
		} catch (Exception e) {
			e.printStackTrace();
			forwardPath = "error";
		}
		return forwardPath;
	}

	// 4. 예약한 투어상품 상세 확인
	@LoginCheck
	@RequestMapping(value = "tour-payment-confirmation")
	public String tourPaymentConfirmation(@ModelAttribute Payment payment, @ModelAttribute RsPInfo rsPInfo,
			@ModelAttribute Tour tour, Model model) {
		String forwardPath = "";

		model.addAttribute(payment);
		model.addAttribute(rsPInfo);
		model.addAttribute(tour);
		forwardPath = "tour-payment-confirmation";
		return forwardPath;
	}
}
