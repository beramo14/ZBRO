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

@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	RoomService roomService;
	
	
	
	@RequestMapping("/search")
	public String searchView() {
		return "/main/room/search";
	}
	
	
	
//	@RequestMapping("/roomdetailrefix/{roomId}")
//	public String Getlayout(@PathVariable("roomId") Long roomId, Model model) {
//	    Optional<Room> roomOptional = roomService.findById(roomId);
//	    if (roomOptional.isPresent()) {
//	        Room room = roomOptional.get();
//	        model.addAttribute("room", room);
//	        
//	    }
//	    return "main/room/roomdetailrefix";
//	}
	
	@RequestMapping("/roomdetailrefix/{roomId}")
	public String Getlayout(@PathVariable("roomId") Long roomId, Model model) {
	    Room room = roomService.findById(roomId);
	    List<RoomOption> roomOption = roomService.getroomOption(room);
	        model.addAttribute("room", room);
	        model.addAttribute("roomOptions", roomOption);
	        System.out.println(roomOption);
	    return "main/room/roomdetailrefix";
	}




	 
}
