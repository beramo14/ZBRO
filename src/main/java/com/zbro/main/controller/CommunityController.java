package com.zbro.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommunityController {
	
	
	@GetMapping("/post_list")
	public String postListView() {
		return "/main/community/post_list";
	}

}
