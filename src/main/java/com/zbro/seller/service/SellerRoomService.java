package com.zbro.seller.service;

import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.RoomPhotoRepository;
import com.zbro.model.Room;
import com.zbro.model.RoomPhoto;
=======
import org.springframework.stereotype.Service;

import com.zbro.model.Room;
>>>>>>> roomsearch
import com.zbro.seller.repository.SellerRoomRepository;

@Service
public class SellerRoomService {
	
	@Autowired
	private SellerRoomRepository roomRepo;
<<<<<<< HEAD
	
=======
>>>>>>> roomsearch

	public void insertRoom(Room room) {
		roomRepo.save(room);
	}
	
<<<<<<< HEAD

	
=======
>>>>>>> roomsearch
}
