package com.zbro.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbro.dto.RoomSearchDTO;
import com.zbro.main.service.RoomService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/room")
public class RoomController {
	
	@Autowired
	private RoomService roomService;

	
	@GetMapping("/search")
	public String searchView(RoomSearchDTO roomDTO, Model model) {
		
		List<RoomSearchDTO> roomDTOList = roomService.searchRoom(roomDTO);
		
		model.addAttribute("rooms", roomDTOList);
		model.addAttribute("roomSearchDTO", roomDTO);
		
		return "/main/room/search";
	}
	
	
	//상세페이지 매핑
	@RequestMapping("/roomdetailrefix")
	public String Getlayout() {
		
		return "main/roomdetailrefix";
		
	}
}
