package com.zbro.seller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.RoomPhotoRepository;
import com.zbro.model.Room;
import com.zbro.model.RoomPhoto;
import com.zbro.seller.repository.SellerRoomRepository;

@Service
public class SellerRoomService {
	
	@Autowired
	private SellerRoomRepository roomRepo;
	

	public void insertRoom(Room room) {
		roomRepo.save(room);
	}
	

	
}
