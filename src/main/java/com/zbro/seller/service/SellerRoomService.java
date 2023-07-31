package com.zbro.seller.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.RoomReviewRepository;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.model.RoomPhoto;
import com.zbro.model.RoomReview;
import com.zbro.model.SellerUser;
import com.zbro.seller.repository.SellerReviewRepository;
import com.zbro.seller.repository.SellerRoomOptionRepository;
import com.zbro.seller.repository.SellerRoomOptionTypeRepository;
import com.zbro.seller.repository.SellerRoomPhotoRepository;
import com.zbro.seller.repository.SellerRoomRepository;

@Service
public class SellerRoomService {

	@Autowired
	private SellerRoomRepository roomRepo;
	@Autowired
	private SellerRoomOptionRepository roomOptionRepo;
	@Autowired
	private SellerRoomOptionTypeRepository roomOptionTypeRepo;
	@Autowired
	private SellerRoomPhotoRepository roomPhotoRepo;
	@Autowired
	private SellerReviewRepository sellerReviewRepo;

	public void insertRoom(Room room) {
		roomRepo.save(room);
	}

	public void insertRoomOption(RoomOption roomOption) {
		roomOptionRepo.save(roomOption);
	}

	public List<RoomOptionType> getRoomOptionType() {
		List<RoomOptionType> findOptionTypes = roomOptionTypeRepo.findAll();

		Collections.sort(findOptionTypes, Comparator.comparingInt(RoomOptionType::getSortOrder));

		return findOptionTypes;	
	}

	public void insertRoomPhoto(RoomPhoto roomPhoto) {
		roomPhotoRepo.save(roomPhoto);
	}

	
	 public List<RoomReview> getRoomReviewDetail(Long reviewId) {
	        List<RoomReview> sellerRoomReview2 = sellerReviewRepo.findByreviewId(reviewId);
	        return sellerRoomReview2;
	    }
	 
	 public List<RoomReview> getRoomReview(Long sellerId) {
	        List<RoomReview> sellerRoomReview = sellerReviewRepo.findByRoom_Seller_SellerId(sellerId);
	        return sellerRoomReview;
	    }
	 
	
	}
	 
	
