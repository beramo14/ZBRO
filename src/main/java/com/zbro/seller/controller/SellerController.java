package com.zbro.seller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zbro.model.Room;
import com.zbro.model.SellerUser;
import com.zbro.seller.service.RoomService;
import com.zbro.seller.service.SellerService;
import com.zbro.type.CostType;

@Controller
public class SellerController {
	
	// Repository
	@Autowired
	private SellerService sellerService;
	@Autowired
	private RoomService roomService;
	
	
	@GetMapping("/room_add")
	public String roomAddView() {
		return "seller/room_add";
	}
	
	@PostMapping("/room_add")
	public String roomAdd(@RequestParam("isRoomIn") boolean isRoomIn, 
						  @RequestParam("isElevator") boolean isElevator,
						  Room room) {
		room.setRoomIn(isRoomIn);
		room.setElevator(isElevator);
		System.out.println(room.toString());
		roomService.insertRoom(room);
		

		return "redirect:add_test";
	}
	
	@GetMapping("/add_test")
	public String test() {
		return "seller/add_test";
	}
}
