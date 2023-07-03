package com.zbro.main.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.zbro.dto.RoomSearchDTO;
import com.zbro.main.repository.FavoritRepository;
import com.zbro.main.repository.RoomPhotoRepository;
import com.zbro.main.repository.RoomRepository;
import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.model.RoomPhoto;
import com.zbro.type.CostType;
import com.zbro.type.RoomType;

@Service
public class RoomService {
	
	@Value("${file.images.room-photo}")
	private String fileRoomPhotoPath; 
	

	@Autowired
	private RoomPhotoRepository RoomPhotoRepo;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private FavoritRepository favRepository;
	
	
	public List<RoomSearchDTO> searchRoomAndFavorite(RoomSearchDTO roomDTO, Long userId) {
		
		List<Room> findedRooms = new ArrayList<Room>();
		
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
	

	public RoomPhoto getRoomPhotoOne(Room room) {
		
		Optional<RoomPhoto> finedRoomPhoto = RoomPhotoRepo.findByRoomAndImageSeq(room, 1);
		
		return finedRoomPhoto.get();
	}




	public Resource getImageResource(RoomPhoto findedRoomPhoto) throws FileNotFoundException {
		
		File file = new File(fileRoomPhotoPath + findedRoomPhoto.getFileName());
		
		if(file.exists() == false || file.isFile() == false) {
			throw new FileNotFoundException("file not found : " +fileRoomPhotoPath + findedRoomPhoto.getFileName());
		}

		InputStream fis = new FileInputStream(file);
		Resource imageResource = new InputStreamResource(fis);
		return imageResource;
	}
}