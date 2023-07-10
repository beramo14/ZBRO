package com.zbro.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbro.main.repository.RoomOptionRepository;
import com.zbro.main.service.RoomService;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.SellerUser;

@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	RoomService roomService;
	
	
	
	@RequestMapping("/search")
	public String searchView() {
		return "/main/room/search";
	}
	
	
	@RequestMapping("/detail/{roomId}")
	public String detailView(@PathVariable("roomId") Long roomId, Model model) {
	    Room room = roomService.findById(roomId);
	    SellerUser selleruser = new SellerUser();
	    selleruser.setSellerId(room.getSeller().getSellerId());
	    List<Room> roomsame = roomService.findBySellerId(selleruser);
	    List<RoomOption> roomOption = roomService.getroomOption(room);
	    
	        model.addAttribute("room", room);
	        model.addAttribute("roomsame", roomsame);
	        model.addAttribute("roomOptions", roomOption);
	    return "main/room/detail";
	}




	 
}
