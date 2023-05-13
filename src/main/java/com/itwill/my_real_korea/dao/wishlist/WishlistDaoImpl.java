package com.itwill.my_real_korea.dao.wishlist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itwill.my_real_korea.dto.wishlist.Wishlist;
import com.itwill.my_real_korea.mapper.NoticeMapper;
import com.itwill.my_real_korea.mapper.WishlistMapper;

@Repository
public class WishlistDaoImpl implements WishlistDao {

	@Autowired
	private WishlistMapper wishlistMapper;

	public WishlistDaoImpl() {
		System.out.println("WishlistDaoImpl 기본생성자 호출");
	}

	@Override
	public Wishlist selectByWishNo(int wishNo) {
		return wishlistMapper.selectByWishNo(wishNo);
	}

	@Override
	public List<Wishlist> selectAll(String userId) {
		return wishlistMapper.selectAll(userId);
	}

	@Override
	public List<Wishlist> selectAllWithTicketAndTour(String userId) {
		return wishlistMapper.selectAllWithTicketAndTour(userId);
	}

	@Override
	public int selectWishlistCount(String userId) {
		return wishlistMapper.selectWishlistCount(userId);
	}

	@Override
	public int insertTicketToWishlist(Wishlist wishlist) {
		return wishlistMapper.insertTicketToWishlist(wishlist);
	}

	@Override
	public int insertTourToWishlist(Wishlist wishlist) {
		return wishlistMapper.insertTourToWishlist(wishlist);
	}

	@Override
	public int deleteWishlist(String userId) {
		return wishlistMapper.deleteWishlist(userId);
	}

	@Override
	public int deleteWishlistByNoAndId(int wishNo, String userId) {
		Map<String, Object> wishMap = new HashMap<>();
		wishMap.put("wishNo", wishNo);
		wishMap.put("userId", userId);
		return wishlistMapper.deleteWishlistByNoAndId(wishMap);
	}

	@Override
	public int selectWishlistTourCount(String userId, int toNo) {
		Map<String, Object> wishMap = new HashMap<>();
		wishMap.put("userId", userId);
		wishMap.put("toNo", toNo);
		return wishlistMapper.selectWishlistTourCount(wishMap);
	}

	@Override
	public int selectWishlistTicketCount(String userId, int tiNo) {
		Map<String, Object> wishMap = new HashMap<>();
		wishMap.put("userId", userId);
		wishMap.put("tiNo", tiNo);
		return wishlistMapper.selectWishlistTicketCount(wishMap);
	}

}
