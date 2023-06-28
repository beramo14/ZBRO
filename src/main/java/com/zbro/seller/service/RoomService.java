package com.zbro.seller.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.seller.repository.RoomOptionRepository;
import com.zbro.seller.repository.RoomOptionTypeRepository;
import com.zbro.seller.repository.RoomRepository;

@Service
public class RoomService {
	
	@Autowired
	private RoomRepository roomRepo;
	@Autowired
	private RoomOptionRepository roomOptionRepo;
	@Autowired
	private RoomOptionTypeRepository roomOptionTypeRepo;

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

}
