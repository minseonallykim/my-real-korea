package com.itwill.my_real_korea.service.wishlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwill.my_real_korea.dao.wishlist.WishlistDao;
import com.itwill.my_real_korea.dto.wishlist.Wishlist;

@Service
public class WishlistServiceImpl implements WishlistService {

	@Autowired
	private WishlistDao wishlistDao;

	public WishlistServiceImpl() {
		System.out.println("WishlistServiceImpl() 기본 생성자 호출");
	}

	@Override
	public List<Wishlist> selectAll(String userId) {
		return wishlistDao.selectAll(userId);
	}

	@Override
	public List<Wishlist> selectAllWithTicketAndTour(String userId) {
		return wishlistDao.selectAllWithTicketAndTour(userId);
	}

	@Override
	public Wishlist selectByWishNo(int wishNo) {
		return wishlistDao.selectByWishNo(wishNo);
	}

	@Override
	public int selectWishlistCount(String userId) {
		return wishlistDao.selectWishlistCount(userId);
	}

	@Override
	public int insertTicketToWishlist(Wishlist wishlist) {
		return wishlistDao.insertTicketToWishlist(wishlist);
	}

	@Override
	public int insertTourToWishlist(Wishlist wishlist) {
		return wishlistDao.insertTourToWishlist(wishlist);
	}

	@Override
	public int deleteWishlist(String userId) {
		return wishlistDao.deleteWishlist(userId);
	}

	@Override
	public int deleteWishlistByNoAndId(int wishNo, String userId) {
		return wishlistDao.deleteWishlistByNoAndId(wishNo, userId);
	}

	@Override
	public int selectWishlistTourCount(String userId, int toNo) {
		return wishlistDao.selectWishlistTourCount(userId, toNo);
	}

	@Override
	public int selectWishlistTicketCount(String userId, int tiNo) {
		return wishlistDao.selectWishlistTicketCount(userId, tiNo);
	}

}
