package com.zbro.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zbro.main.repository.RoomOptionRepository;
import com.zbro.main.repository.RoomOptionTypeRepository;
import com.zbro.main.service.MypageService;
import com.zbro.main.service.RoomOptionTypeService;
import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.model.RoomPhoto;

@Controller
public class MypageController {
	
	@Autowired
	private MypageService mypageService;
	
	@Autowired
	private RoomOptionTypeService roomOptionTypeService;
	
	
	@GetMapping("/favoriteCompare")
	public String favoriteComparePage(Model model) {

	    List<Favorite> favorites = mypageService.getFavoritesByUserConsumerId();
	    List<List<RoomOption>> roomOptions = new ArrayList<>();
	    List<List<RoomPhoto>> roomPhotos = new ArrayList<>();
	    
        
        for (Favorite favorite : favorites) {
            List<RoomOption> options = mypageService.getRoomOptionsByRoomId(favorite.getRoom().getRoomId());
            roomOptions.add(options);
            
            List<RoomPhoto> photos = mypageService.getRoomPhotosByRoomId(favorite.getRoom().getRoomId());
            roomPhotos.add(photos);
        }
        
        List<RoomOptionType> roomOptionTypes = roomOptionTypeService.getAllRoomOptionTypes();
        
        
        model.addAttribute("roomOptions", roomOptions);
        model.addAttribute("favorites", favorites);
        model.addAttribute("roomOptionTypes", roomOptionTypes);
        model.addAttribute("roomPhotos", roomPhotos); 
        
        return "main/mypage/favoriteCompare";
    }
	
	@PostMapping("/saveMemo")
	public ResponseEntity<String> saveMemo(@RequestParam("favoriteId") Long favoriteId, @RequestParam("memo") String memo) {
	    mypageService.saveMemo(favoriteId, memo);
	    return ResponseEntity.ok().build();
	}
	

	@GetMapping("/favoriteList")
	public String favoriteListPage(Model model) {
		List<Favorite> favorites = mypageService.getFavoritesByUserConsumerId();
		List<List<RoomPhoto>> roomPhotos = new ArrayList<>();
		
        for (Favorite favorite : favorites) {
         
            List<RoomPhoto> photos = mypageService.getRoomPhotosByRoomId(favorite.getRoom().getRoomId());
            roomPhotos.add(photos);
        }
                      
        model.addAttribute("favorites", favorites);
        model.addAttribute("roomPhotos", roomPhotos); 

        return "main/mypage/favoriteList";
    }
	
	
	@GetMapping("/contentList")
	public String contentListPage(Model model) {
				
        return "main/mypage/contentList";
    }
	
	
	
	
    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<String> deleteFavorite(@PathVariable Long favoriteId) {
    	mypageService.deleteFavorite(favoriteId);
        return ResponseEntity.ok("Favorite deleted");
    }	
	
	
	
	@GetMapping("/memberInfo")
	public String memberInfoPage(Model model) {
				
        return "main/mypage/memberInfo";
    }
	
	

}
	








