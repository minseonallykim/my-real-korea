package com.itwill.my_real_korea.dao.wishlist;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dao.city.CityDao;
import com.itwill.my_real_korea.dao.ticket.TicketDao;
import com.itwill.my_real_korea.dao.tour.TourDao;
import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.tour.Tour;
import com.itwill.my_real_korea.dto.wishlist.Wishlist;
import com.itwill.my_real_korea.mapper.TicketMapper;
import com.itwill.my_real_korea.mapper.TourMapper;
import com.itwill.my_real_korea.mapper.WishlistMapper;

//@SpringBootApplication
//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
//@MapperScan(basePackageClasses = {WishlistMapper.class, TicketMapper.class, TourMapper.class})
class WishlistDaoImplTest {

	@Autowired
	private WishlistDao wishlistDao;
	@Autowired
	private TicketDao ticketDao;
	@Autowired
	private TourDao tourDao;
	@Autowired
	private CityDao cityDao;
	
	@Disabled
	@Test
	void testSelectAll() {
		List<Wishlist> wishlistList = wishlistDao.selectAll("user1");
		assertNotNull(wishlistList);
		System.out.println(wishlistList);
	}

	
	@Test
	void testSelectAllWithTicketAndTour() {
		List<Wishlist> wishlistList = wishlistDao.selectAllWithTicketAndTour("user1");
		assertNotNull(wishlistList);
		System.out.println(wishlistList);
	}

	@Disabled
	@Test
	void testSelectWishlistCount() {
		int rowCount = wishlistDao.selectWishlistCount("user2");
		assertNotEquals(rowCount, 0);
		System.out.println(rowCount);
	}

	@Disabled
	@Test
	void testInsertTicketToWishlist() throws Exception {
		
		
		City city = cityDao.findByCityNo(1);
		
		Ticket ticket = new Ticket(0, "티켓인서트", null, 1111, "인포", "유의사항", 0, city);
		ticketDao.insertTicket(ticket);
		
		Wishlist ticketWishlist = new Wishlist(0, "user1", ticket, null);
		int rowCount = wishlistDao.insertTicketToWishlist(ticketWishlist);
		assertNotEquals(rowCount, 0);
		System.out.println(rowCount);
	}

	@Disabled
	@Test
	void testInsertTourToWishlist() throws Exception {
		
		City city = cityDao.findByCityNo(2);
		
		Tour tour = new Tour(0, "투어인서트", 1, 12, 3, "버스정류장앞", 50000, "재밌는 투어", "편한 복장으로 오세요", 2, city);
		tourDao.insertTour(tour);
		
		Wishlist tourWishlist = new Wishlist(0, "user2", null, tour);
		int rowCount = wishlistDao.insertTourToWishlist(tourWishlist);
		assertNotEquals(rowCount, 0);
		System.out.println(rowCount);
	}

	@Disabled
	@Test
	void testDeleteWishlist() {
		int rowCount = wishlistDao.deleteWishlist("user3");
		assertNotEquals(rowCount, 0);
		System.out.println(rowCount);
	}

	@Disabled
	@Test
	void testDeleteWishlistByNoAndId() {
		int rowCount = wishlistDao.deleteWishlistByNoAndId(3,"user2");
		assertEquals(rowCount, 1);
		System.out.println(rowCount);
	}

}
