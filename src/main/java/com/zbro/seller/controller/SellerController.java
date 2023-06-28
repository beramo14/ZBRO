package com.zbro.seller.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

//	// 매물 등록하기
//	@PostMapping("/room_add")
//	public String roomAdd(@RequestParam("isRoomIn") boolean isRoomIn, 
//						  @RequestParam("isElevator") boolean isElevator,
//						  @RequestParam(value = "optionType", required = false) List<String> optionTypes,
//						  Room room) {
//		
//		// room insert
//		room.setRoomIn(isRoomIn);
//		room.setElevator(isElevator);
//		roomService.insertRoom(room);
//		
//		// room insert 결과 확인
////		System.out.println("등록된 room id : " + room.getRoomId());
//		
//		// 선택한 옵션 확인
////		if (optionTypes != null) {
////	        for (String optionType : optionTypes) {
////	            System.out.println("선택한 옵션: " + optionType.toString());
////	        }
////	    }
//		
//		// RoomOption insert
//		if(optionTypes != null) {
//			for (String optionType : optionTypes) {
//				RoomOption roomOption = new RoomOption();
//				RoomOptionType roomOptionType = new RoomOptionType();
//				roomOptionType.setOptionType(optionType);
//				roomOption.setRoom(room);
//				roomOption.setOptionType(roomOptionType);
//				roomService.insertRoomOption(roomOption);
//			}
//		}
//		
//		return "redirect:add_test";
//	}
	
	
	
	
	
	
	// 매물 등록하기
	@PostMapping("/room_add")
    public String roomAdd(
            @RequestParam("isRoomIn") boolean isRoomIn,
            @RequestParam("isElevator") boolean isElevator,
            @RequestParam(value = "optionType", required = false) List<String> optionTypes,
            Room room, RoomPhoto roomPhoto,
            @RequestPart("fileName") List<MultipartFile> files) throws IOException {

        // room insert
        room.setRoomIn(isRoomIn);
        room.setElevator(isElevator);
        roomService.insertRoom(room);

        // RoomOption insert
        if (optionTypes != null) {
            for (String optionType : optionTypes) {
                RoomOption roomOption = new RoomOption();
                RoomOptionType roomOptionType = new RoomOptionType();
                roomOptionType.setOptionType(optionType);
                roomOption.setRoom(room);
                roomOption.setOptionType(roomOptionType);
                roomService.insertRoomOption(roomOption);
            }
        }
        
        String uploadFile = roomPhoto.getFileName();
        
        System.out.println(uploadFile);
        
        
        // File upload
        for (MultipartFile file : files) {
        	// 파일 이름 가져오기
            String fileName = file.getOriginalFilename();
            System.out.println("File Name: " + fileName);

            // 파일 크기 가져오기
            long fileSize = file.getSize();
            System.out.println("File Size: " + fileSize + " bytes");

            // 파일의 내용(byte 배열) 가져오기
            byte[] fileContent = file.getBytes();
            // 파일의 내용을 이용하여 원하는 작업 수행
            // 예: 파일 저장, 파일 분석 등

            // 파일의 MIME 타입 가져오기
            String mimeType = file.getContentType();
            System.out.println("MIME Type: " + mimeType);
            
            if (!file.isEmpty()) {
                // 파일 업로드 로직을 구현하세요.
                // 예를 들어, 서버에 파일 저장이 필요한 경우:
                // String fileName = file.getOriginalFilename();
                // file.transferTo(new File("저장할_경로/" + fileName));
            	System.out.println("파일이 넘어왔습니다");
            }
            if (file.isEmpty()) {
            	System.out.println("파일이 안넘어옴");
            }
        }

        return "redirect:add_test";
    }
	
	
	
	
	
	@GetMapping("/add_test")
	public String test() {
		return "seller/add_test";
	}
}
