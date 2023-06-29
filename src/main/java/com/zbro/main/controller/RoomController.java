package com.zbro.main.controller;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbro.main.service.RoomService;
import com.zbro.model.Room;
import com.zbro.model.RoomPhoto;

@Controller
@RequestMapping("/room")
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
	
	@RequestMapping("/search")
	public String searchView() {
		return "/main/room/search";
	}
	
	//상세페이지 매핑
	@RequestMapping("/roomdetailrefix")
	public String Getlayout() {
		
		return "main/roomdetailrefix";
		
	}
	
	
	
	@GetMapping("/photo")
	public ResponseEntity<Resource> getRoomPhotoImage(Room room) throws FileNotFoundException {
		
		RoomPhoto findedRoomPhoto = roomService.getRoomPhotoOne(room);
		
		Resource imageResource = roomService.getImageResource(findedRoomPhoto);
		
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
	}
	
	
}
