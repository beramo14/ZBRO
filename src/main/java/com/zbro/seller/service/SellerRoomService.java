package com.zbro.seller.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.model.RoomPhoto;
import com.zbro.model.SellerUser;
import com.zbro.seller.repository.SellerRoomOptionRepository;
import com.zbro.seller.repository.SellerRoomOptionTypeRepository;
import com.zbro.seller.repository.SellerRoomPhotoRepository;
import com.zbro.seller.repository.SellerRoomRepository;
import com.zbro.seller.repository.SellerRoomUserRepository;

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
	private SellerRoomUserRepository sellerUserRepo;

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

	public List<Room> getRooms(Long sellerId) {
		Optional<SellerUser> sellerUser = sellerUserRepo.findById(sellerId);
		
		return roomRepo.findAllBySeller(sellerUser.get());
	}

	public Room getRoom(Long roomId) {
		return roomRepo.findById(roomId).get();
	}

	public List<RoomOption> getRoomOptions(Room findRoom) {
		return roomOptionRepo.findAllByRoom(findRoom);
	}

}