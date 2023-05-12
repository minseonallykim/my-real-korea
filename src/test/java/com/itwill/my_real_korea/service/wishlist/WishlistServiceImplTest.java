package com.itwill.my_real_korea.service.wishlist;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.itwill.my_real_korea.dto.City;
import com.itwill.my_real_korea.dto.ticket.Ticket;
import com.itwill.my_real_korea.dto.wishlist.Wishlist;

//@SpringBootTest
@MapperScan(basePackages = "com.itwill.my_real_korea.mapper")
class WishlistServiceImplTest {

	@Autowired
	private WishlistService wishlistService;
	
	@Disabled
	@Test
	void testSelectAll() {
		List<Wishlist> wishlistList = wishlistService.selectAll("user1");
		assertNotNull(wishlistList);
		System.out.println(wishlistList);
	}

	@Disabled
	@Test
	void testSelectAllWithTicketAndTour() {
		List<Wishlist> wishlistList = wishlistService.selectAllWithTicketAndTour("user1");
		assertNotNull(wishlistList);
		System.out.println(wishlistList);
	}

	@Disabled
	@Test
	void testSelectWishlistCount() {
		int rowCount = wishlistService.selectWishlistCount("user2");
		assertNotEquals(rowCount, 0);
		System.out.println(rowCount);
	}
	@Test
	void testSelectWishlistTourCount() {
		int rowCount = wishlistService.selectWishlistTourCount("admin",1);
		assertNotEquals(rowCount, 0);
		System.out.println(rowCount);
	}
	@Test
	void testSelectWishlistTicketCount() {
		int rowCount = wishlistService.selectWishlistTicketCount("aaa111",1);
		assertNotEquals(rowCount, 0);
		System.out.println(rowCount);
	}

	@Disabled
	@Test
	void testInsertTicketToWishlist() {
		
	}

	
	@Disabled
	@Test
	void testInsertTourToWishlist() {
	}
	
	@Disabled
	@Test
	void testDeleteWishlist() {
	}

	@Disabled
	@Test
	void testDeleteWishlistByNoAndId() {
	}

}
