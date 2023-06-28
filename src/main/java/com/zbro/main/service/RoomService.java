package com.zbro.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.dto.RoomSearchDTO;
import com.zbro.main.repository.FavoritRepository;
import com.zbro.main.repository.RoomRepository;
import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.type.CostType;
import com.zbro.type.RoomType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private FavoritRepository favRepository;
	
	
	public List<RoomSearchDTO> searchRoomAndFavorite(RoomSearchDTO roomDTO, Long userId) {
		
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
		
		List<RoomSearchDTO> roomDTOList = new ArrayList<>();
		for(Room room : findedRooms) {
			ConsumerUser user = new ConsumerUser();
			user.setConsumerId(userId);
			Optional<Favorite> findedFavorite = favRepository.findByUserAndRoom(user, room);
			RoomSearchDTO findedRoomDTO = new RoomSearchDTO(room);
			if(findedFavorite.isPresent() == true) {
				findedRoomDTO.setFavorite(true);
				findedRoomDTO.setFavoriteId(findedFavorite.get().getFavoriteId());
			}
			roomDTOList.add(findedRoomDTO);
		}
//		List<RoomSearchDTO> roomDTOList = RoomSearchDTO.convertToDTO(findedRooms);
		
		return roomDTOList;
	}
	
	

}
