package com.zbro.seller.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.model.RoomPhoto;
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
	
	// 매물등록 페이지 들어가기.
	@GetMapping("/room_add")
	public String roomAddView(Model model, RoomOptionType roomOptionType) {
		List<RoomOptionType> optionTypes = roomService.getRoomOptionType();
		model.addAttribute("optionTypes", optionTypes);
		
		return "seller/room_add";
	}
	
	@PostMapping("/room_add")
	public String roomAdd(@RequestParam("isRoomIn") boolean isRoomIn, 
						  @RequestParam("isElevator") boolean isElevator,
						  @RequestParam(value = "optionType", required = false) List<String> optionTypes,
						  Room room) {
		
		// room insert
		room.setRoomIn(isRoomIn);
		room.setElevator(isElevator);
		roomService.insertRoom(room);
		
		// room insert 결과 확인
		System.out.println("등록된 room id : " + room.getRoomId());
		
		if (optionTypes != null) {
	        for (String optionType : optionTypes) {
	            System.out.println("선택한 옵션: " + optionType.toString());
	        }
	    }
		 
		
		// RoomOption insert
		if(optionTypes != null) {
			for (String optionType : optionTypes) {
				RoomOption roomOption = new RoomOption();
				RoomOptionType roomOptionType = new RoomOptionType();
				roomOptionType.setOptionType(optionType);
				roomOption.setRoom(room);
				roomOption.setOptionType(roomOptionType);
				roomService.insertRoomOption(roomOption);
			}
		}
		
		
		
//		System.out.println(roomOption.toString());
		return "redirect:add_test";
	}
	
	@GetMapping("/add_test")
	public String test() {
		return "seller/add_test";
	}
}
