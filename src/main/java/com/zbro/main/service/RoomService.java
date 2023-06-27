package com.zbro.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.dto.RoomSearchDTO;
import com.zbro.main.repository.RoomRepository;
import com.zbro.model.Room;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;
	
	public List<Room> searchRoom(RoomSearchDTO roomDTO) {
		return roomRepository.findRoomsByAddressContaining(roomDTO.getSearchWord());
	}
	
	

}
