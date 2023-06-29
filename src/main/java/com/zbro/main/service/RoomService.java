package com.zbro.main.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.RoomPhotoRepository;
import com.zbro.model.Room;
import com.zbro.model.RoomPhoto;

@Service
public class RoomService {
	
	@Value("${file.images.room-photo}")
	private String fileRoomPhotoPath; 
	

	@Autowired
	private RoomPhotoRepository RoomPhotoRepo;
	
	
	

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
