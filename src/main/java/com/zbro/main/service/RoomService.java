package com.zbro.main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.dto.RoomSearchDTO;
import com.zbro.main.repository.RoomRepository;
import com.zbro.model.Room;
import com.zbro.type.CostType;
import com.zbro.type.RoomType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;
	
	public List<RoomSearchDTO> searchRoom(RoomSearchDTO roomDTO) {
		
		List<Room> findedRooms = new ArrayList<>();
		
		if(roomDTO.getCostType() == CostType.none && roomDTO.getType() == RoomType.none) {
			findedRooms = roomRepository.findRoomsByAddressContaining(roomDTO.getSearchWord());
		} else if(roomDTO.getCostType() == CostType.none && roomDTO.getType() != RoomType.none) {
			findedRooms = roomRepository.findRoomsByTypeAndAddressContaining(roomDTO.getType(), roomDTO.getSearchWord());
		} else if(roomDTO.getType() == RoomType.none && roomDTO.getCostType() != CostType.none) {
			findedRooms = roomRepository.findRoomsByCostTypeAndAddressContaining(roomDTO.getCostType(), roomDTO.getSearchWord());
		} else {
			findedRooms = roomRepository.findRoomsByTypeAndCostTypeAndAddressContaining(roomDTO.getType(), roomDTO.getCostType(), roomDTO.getSearchWord());
		}
		
		
		List<RoomSearchDTO> roomDTOList = RoomSearchDTO.convertToDTO(findedRooms);
		return roomDTOList;
	}
	
	

}
