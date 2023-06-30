package com.zbro.main.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zbro.dto.RoomSearchDTO;
import com.zbro.main.service.FavoriteService;
import com.zbro.main.service.RoomService;
import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.model.RoomPhoto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/room")
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private FavoriteService favService;

	
	@GetMapping("/search")
	public String searchView(RoomSearchDTO roomDTO, Model model) {
		Long userId = 1L;
		List<RoomSearchDTO> roomDTOList = roomService.searchRoomAndFavorite(roomDTO, userId);
		
		model.addAttribute("rooms", roomDTOList);
		model.addAttribute("roomSearchDTO", roomDTO);
		
		return "/main/room/search";
	}
	
	
	//상세페이지 매핑
	@RequestMapping("/roomdetailrefix")
	public String Getlayout() {
		
		return "main/roomdetailrefix";
	}
	
	
	@PostMapping("/favorite")
	@ResponseBody
	public ResponseEntity<?> doFavorite(Room Room) {
		
		log.info("doFavorite = {}", Room);
		Favorite fav = new Favorite();
		
		ConsumerUser user = new ConsumerUser();
		user.setConsumerId(1L);
		fav.setUser(user);
		fav.setRoom(Room);
		
		Favorite savedFav = favService.favorite(fav);
		
		return ResponseEntity.ok().body(savedFav.getFavoriteId());
	}
	
	
	@PostMapping("/unfavorite")
	@ResponseBody
	public ResponseEntity<?> doUnfavorite(Favorite fav) {
		
		log.info("doUnfavorite = {}", fav);
		
		favService.unfavorite(fav);
		return ResponseEntity.ok().body("success");
	}
	
	
	
	@GetMapping("/photo")
	public ResponseEntity<Resource> getRoomPhotoImage(Room room) throws FileNotFoundException {
		
		RoomPhoto findedRoomPhoto = roomService.getRoomPhotoOne(room);
		
		Resource imageResource = roomService.getImageResource(findedRoomPhoto);
		
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
	}
	
	
}
