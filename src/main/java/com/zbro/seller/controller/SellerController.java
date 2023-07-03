package com.zbro.seller.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.model.RoomPhoto;
import com.zbro.model.SellerUser;
import com.zbro.seller.service.SellerRoomService;
import com.zbro.seller.service.SellerService;
import com.zbro.type.CostType;

@Controller
public class SellerController {
	
	// Repository
	@Autowired
	private SellerService sellerService;
	@Autowired
	private SellerRoomService roomService;
	
	@Value("${path.upload}")
	public String uploadFolder;
	
	
	// 매물등록 페이지 들어가기.
	@GetMapping("/room_add")
	public String roomAddView(Model model, RoomOptionType roomOptionType) {
		List<RoomOptionType> optionTypes = roomService.getRoomOptionType();
		model.addAttribute("optionTypes", optionTypes);
		
		return "seller/room_add";
	}

	// 매물 등록하기
	@PostMapping("/room_add")
	public String roomAdd(@RequestParam("isRoomIn") boolean isRoomIn, 
						  @RequestParam("isElevator") boolean isElevator,
						  @RequestParam(value = "optionType", required = false) List<String> optionTypes,
						  @RequestParam(value = "uploadFile", required = false) List<MultipartFile> files,
						  Room room) throws Exception, IOException {
		
		// room insert
		room.setRoomIn(isRoomIn);
		room.setElevator(isElevator);
		roomService.insertRoom(room);
		
		// room insert 결과 확인
//		System.out.println("등록된 room id : " + room.getRoomId());
		
		// 선택한 옵션 확인
//		if (optionTypes != null) {
//	        for (String optionType : optionTypes) {
//	            System.out.println("선택한 옵션: " + optionType.toString());
//	        }
//	    }
		
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
		
		// 업로드한 파일 확인
		for (MultipartFile file : files) {
		    System.out.println("업로드한 파일 확인 : " + file.getOriginalFilename());
		}
		
		
		if(!files.isEmpty()) {	//파일을 업로드했다면
			int imgCnt = 1;
			
			for(MultipartFile file:files) {
				System.out.println(imgCnt);
				// 지정폴더에 파일을 실제 업로드 // ex)room1_파일명.파일형식
				String fileName = "room"+room.getRoomId()+"_"+file.getOriginalFilename();
				file.transferTo(new File(uploadFolder+fileName));
				
				// 테이블 데이터 set
				RoomPhoto roomPhoto = new RoomPhoto();
				roomPhoto.setFileName(fileName);
				roomPhoto.setRoom(room);
				roomPhoto.setImageSeq(imgCnt);
				
				// img insert
				roomService.insertRoomPhoto(roomPhoto);
				imgCnt++;
			}
//			imgCnt = 1;
		}
		
		
		
		return "redirect:add_test";
	}
	
	
	

	@GetMapping("/add_test")
	public String test() {
		return "seller/add_test";
	}
}