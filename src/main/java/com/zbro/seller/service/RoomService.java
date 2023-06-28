package com.zbro.seller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.model.Room;
import com.zbro.seller.repository.RoomRepository;

@Service
public class RoomService {
	
	@Autowired
	private RoomRepository roomRepo;

	public void insertRoom(Room room) {
		roomRepo.save(room);
	}
	
}
