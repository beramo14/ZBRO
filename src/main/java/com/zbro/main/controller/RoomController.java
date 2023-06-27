package com.zbro.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/room")
public class RoomController {
	

	
	@RequestMapping("/search")
	public String searchView() {
		return "/main/room/search";
	}
	
	//상세페이지 매핑
	@RequestMapping("/roomdetailrefix")
	public String Getlayout() {
		
		return "main/roomdetailrefix";
		
	}
}
