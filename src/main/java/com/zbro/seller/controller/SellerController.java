package com.zbro.seller.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zbro.main.service.UserService;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.model.RoomPhoto;
import com.zbro.model.SellerUser;
import com.zbro.seller.service.SellerRoomService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SellerController {
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	// Repository
	@Autowired
	private UserService sellerUserService;
	@Autowired
	private SellerRoomService roomService;
	
	@Value("${file.images.room-photo}")
	public String uploadFolder;
	
	@Value("${file.biz}")
	private String fileBizPath;
	
	
	@GetMapping("/seller")
	public String sellerMainPage() {
		
		return "seller/index";
	}
	
	@GetMapping("/seller/user")
	public String sellerUserView(Model model){
		
		Long tempUserId = 1L;
		
		SellerUser sellerUser = sellerUserService.getSellerUser(tempUserId);
		model.addAttribute("sellerUser", sellerUser);
		
		return "seller/user/detail";
	}
	
	@PostMapping("/seller/user/update")
	public String sellerUserUpdate(@RequestParam("profilePhotoFile") MultipartFile profilePhotoFile, @RequestParam("bizScanFile") MultipartFile bizScanFile, @RequestParam("isBiz") String isBizString, SellerUser sellerUser) throws IllegalStateException, IOException {
		
		Long tempUserId = 1L;
		
		SellerUser findedSellerUser = sellerUserService.getSellerUser(tempUserId);
		
		//isBiz String -> boolean
		sellerUser.setBiz("true".equalsIgnoreCase(isBizString));
		
		log.info("#### sellerUserUpdate : {}", sellerUser);
		
		if(profilePhotoFile.isEmpty() == false) {
			sellerUserService.profileImageDelete(findedSellerUser);
			String profileImageFilename = sellerUserService.profileImageSave(profilePhotoFile);
			sellerUser.setProfilePhoto(profileImageFilename);
		}
		
		if(bizScanFile.isEmpty() == false && sellerUser.isBiz()) {
			sellerUserService.bizFileDelete(findedSellerUser);
			String bizFilename = sellerUserService.bizFileSave(bizScanFile);
			sellerUser.setBizFile(bizFilename);
		} else if(sellerUser.isBiz() == false) {
			sellerUserService.bizFileDelete(findedSellerUser);
		}
		
		sellerUserService.updateSellerUser(tempUserId, sellerUser);
		
		return "redirect:/seller/user";
	}
	
	
	
	@GetMapping("/download/seller/bizfile")
	public ResponseEntity<Resource> downloadBizFile(SellerUser user) {
		SellerUser sellerUser = sellerUserService.getSellerUser(user.getSellerId());
		
		try {
			Resource resource =  resourceLoader.getResource("file:/"+fileBizPath + sellerUser.getBizFile());
			File file = resource.getFile();
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName())
					.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
					.body(resource);
			
		} catch (IOException e) {
			 return ResponseEntity.badRequest().body(null);
		}
		
	}
	
}