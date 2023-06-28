package com.zbro.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.RoomOptionRepository;
import com.zbro.main.repository.RoomRepository;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;

@Service
public class RoomService {
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	RoomOptionRepository roomOptionRepository;
	


//	public Optional<Room> findById(Long roomId) {
//	    return roomRepository.findById(roomId);
//	}
	public Room findById(Long roomId) {
		Optional<Room> roomOptional =  roomRepository.findById(roomId);
		if (roomOptional.isPresent()) {
			return roomOptional.get();
	    }
		return null;
	}




	public List<RoomOption> getroomOption(Room room) {
		List<RoomOption> getRoomOption = roomOptionRepository.findByRoom(room);
		return getRoomOption;
	}
	 
	
	
	
}
