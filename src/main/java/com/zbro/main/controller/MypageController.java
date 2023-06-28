package com.zbro.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.zbro.main.service.MypageService;
import com.zbro.model.Favorite;

@Controller
public class MypageController {
	
	@Autowired
	private MypageService mypageService;
	    
    
    @GetMapping("/favoriteCompare")
    public String favoriteComparePage(Model model) {

        List<Favorite> favorites = mypageService.getFavoritesLogInUserId();

        model.addAttribute("favorites", favorites);

        return "main/mypage/favoriteCompare";
    }
}
