package com.zbro.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zbro.main.repository.RoomOptionRepository;
import com.zbro.main.repository.RoomOptionTypeRepository;
import com.zbro.main.service.MypageService;
import com.zbro.main.service.RoomOptionTypeService;
import com.zbro.model.Comment;
import com.zbro.model.Community;
import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.model.RoomPhoto;
import com.zbro.type.PostType;

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
	    ConsumerUser consumerUser = mypageService.getConsumerUser();
	    
        
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
        model.addAttribute("consumerUser", consumerUser);
        
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
		ConsumerUser consumerUser = mypageService.getConsumerUser();
		
                      
        model.addAttribute("favorites", favorites);  
        model.addAttribute("consumerUser", consumerUser);

        return "main/mypage/favoriteList";
    }
	
		
	
    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<String> deleteFavorite(@PathVariable Long favoriteId) {
    	mypageService.deleteFavorite(favoriteId);
        return ResponseEntity.ok("Favorite deleted");
    }	
    
    @GetMapping("/contentList")
    public String getAllCommunities(Model model) {
        List<Community> communities = mypageService.getCommunitiesByUser();
        ConsumerUser consumerUser = mypageService.getConsumerUser();


        model.addAttribute("communities", communities);
        model.addAttribute("consumerUser", consumerUser);


        return "main/mypage/contentList";
    }
	
    @GetMapping("/commentList")
    public String getAllComments(Model model) {
        List<Comment> comments = mypageService.getCommentsByUser();
        ConsumerUser consumerUser = mypageService.getConsumerUser();

        model.addAttribute("comments", comments);
        model.addAttribute("consumerUser", consumerUser);
        
        return "main/mypage/commentList";
    }
    
    
    
    @GetMapping("/sidebar")
    public String getSidebar(Model model) {
        ConsumerUser consumerUser = mypageService.getConsumerUser();

        model.addAttribute("consumerUser", consumerUser);

        return "main/mypage/sidebar";
    }
	
    @GetMapping("/memberInfo")
    public String getMenberInfo(Model model) {
        ConsumerUser consumerUser = mypageService.getConsumerUser();

        model.addAttribute("consumerUser", consumerUser);

        return "main/mypage/memberInfo";
    }
	
    @PostMapping("/memberInfo")
    public String updateUserInfo(@RequestParam("imageUpload") MultipartFile file, ConsumerUser consumerUser, Model model) {

        if (!file.isEmpty()) {
            String fileName = saveProfileImage(file);
            consumerUser.setProfilePhoto(fileName);
        }
        
        ConsumerUser consumerUserUP = mypageService.getConsumerUser();
        consumerUserUP.setPassword(consumerUser.getPassword());
        consumerUserUP.setProfilePhoto(consumerUser.getProfilePhoto());
        
        mypageService.updateUserInfo(consumerUserUP);
        
        return "redirect:/memberInfo";
    }
    
    
    private String saveProfileImage(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        return fileName;

    }
    
    @PostMapping("/memberDelete")
    public String memberDelete() {
      mypageService.memberDelete();
      return "redirect:/"; 
    }
    
  
    @GetMapping("/myMain")
    public String myMainPage(Model model) {
		List<Favorite> favorites = mypageService.getFavoritesByUserConsumerId();
		List<List<RoomPhoto>> roomPhotos = new ArrayList<>();
		List<Community> communities = mypageService.getCommunitiesByUser();
		List<Comment> comments = mypageService.getCommentsByUser();
		ConsumerUser consumerUser = mypageService.getConsumerUser();
		
        for (Favorite favorite : favorites) {
         
            List<RoomPhoto> photos = mypageService.getRoomPhotosByRoomId(favorite.getRoom().getRoomId());
            roomPhotos.add(photos);
        }
                      
        model.addAttribute("favorites", favorites);
        model.addAttribute("roomPhotos", roomPhotos); 
        model.addAttribute("communities", communities);
        model.addAttribute("comments", comments);
        model.addAttribute("consumerUser", consumerUser);

        return "main/mypage/myMain";
    }
    

}
	



