package com.zbro.seller.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
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
	
	@Value("${file.images.room-photo}")
	private String fileRoomPhotoPath;

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

	public List<RoomPhoto> getRoomPhotos(Room findRoom) {
		return roomPhotoRepo.findAllByRoom(findRoom);
	}

	public RoomPhoto getRoomPhoto(Long photoId) {
		return roomPhotoRepo.findById(photoId).get();
	}

	public Resource getImageResource(RoomPhoto roomPhoto) throws FileNotFoundException {
		
		File file = new File(fileRoomPhotoPath+roomPhoto.getFileName());
		
		if(file.exists() == false || file.isFile() == false) {
			throw new FileNotFoundException("file not found : " +fileRoomPhotoPath + roomPhoto.getUploadFile());
		}
		
		InputStream fis = new FileInputStream(file);
		Resource imageResource = new InputStreamResource(fis);
		return imageResource;
	}

}