package com.itwill.my_real_korea.dto.wishlist;

import java.util.ArrayList;
import java.util.List;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.ticket.TicketImg;
import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.tour.TourImg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
이름      널?       유형           
------- -------- ------------ 
WISH_NO NOT NULL NUMBER       
USER_ID          VARCHAR2(50) 
TI_NO            NUMBER       
TO_NO            NUMBER   
 */

@Data
@NoArgsConstructor
@ToString
public class Wishlist {

	private int wishNo;
	/* FK */
	private String userId;
	/* FK : ticket 상품 데이터 가져오기 */
	private Ticket ticket;
	/* FK : tour 상품 데이터 가져오기 */
	private Tour tour;
	
	private List<TourImg> tourImgList = new ArrayList<TourImg>();
	private List<TicketImg> ticketImgList = new ArrayList<TicketImg>();
	
	private City city;
	
	public Wishlist(int wishNo, String userId, Ticket ticket, Tour tour) {
		super();
		this.wishNo = wishNo;
		this.userId = userId;
		this.ticket = ticket;
		this.tour = tour;
	}
}
