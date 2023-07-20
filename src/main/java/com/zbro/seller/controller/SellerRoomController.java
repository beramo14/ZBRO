package com.zbro.seller.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.model.RoomPhoto;
import com.zbro.model.SellerUser;
import com.zbro.seller.service.SellerRoomService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SellerRoomController {
	
	@Autowired
	private SellerRoomService roomService;
	
	@Value("${file.images.room-photo}")
	public String uploadFolder;
	
	// 매물등록 페이지 들어가기.
		@GetMapping("/seller/room/insert")
		public String roomAddView(Model model, RoomOptionType roomOptionType) {
			List<RoomOptionType> optionTypes = roomService.getRoomOptionType();
			model.addAttribute("optionTypes", optionTypes);
			
			return "seller/room/insert";
		}

		// 매물 등록하기
		@PostMapping("/seller/room/insert")
		public String roomAdd(@RequestParam("isRoomIn") boolean isRoomIn, 
							  @RequestParam("isElevator") boolean isElevator,
							  @RequestParam(value = "optionType", required = false) List<String> optionTypes,
							  @RequestParam(value = "uploadFile", required = false) List<MultipartFile> files,
							  Room room) throws Exception, IOException {
			
			// room insert
			room.setRoomIn(isRoomIn);
			room.setElevator(isElevator);
			roomService.insertRoom(room);
			
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
//			for (MultipartFile file : files) {
//			    System.out.println("업로드한 파일 확인 : " + file.getOriginalFilename());
//			}
			
			int imgCnt = 1;
			for(MultipartFile file : files) {
				if(!file.getOriginalFilename().isBlank()) {	//파일을 업로드했다면
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
			}
			
			return "redirect:/seller/room/list";
		}
		
		
		@RequestMapping("/seller/room/list")
		public String sellerRoomList(Model model,
						   @RequestParam(defaultValue = "1") Long sellerId) {

			List<Room> findRooms = roomService.getRooms(sellerId);
			model.addAttribute("roomList", findRooms);
			
			return "seller/room/list";
		}
		
		
		@GetMapping("/seller/room/detail")
		public String sellerRoomDetail(Model model,
									   @RequestParam Long roomId) {

			Room findRoom = roomService.getRoom(roomId);
			List<RoomOptionType> roomOptionType = roomService.getRoomOptionType();
			List<RoomOption> roomOptions = roomService.getRoomOptions(findRoom);
			List<RoomPhoto> roomPhotos = roomService.getRoomPhotos(findRoom);
			
			model.addAttribute("room", findRoom);
			model.addAttribute("optionTypes", roomOptionType);
			model.addAttribute("thisRoomOptions", roomOptions);
			model.addAttribute("roomPhotos", roomPhotos);
			 
			return "seller/room/detail";
		}
		
		
		@GetMapping("/seller/room/photo")
		public ResponseEntity<Resource> getSellerRoomPhoto(RoomPhoto roomPhoto) throws FileNotFoundException {
			RoomPhoto findedRoomPhoto = roomService.getRoomPhoto(roomPhoto.getPhotoId());
			
			Resource imageResource = roomService.getImageResource(findedRoomPhoto);
			return ResponseEntity.ok().body(imageResource);
		}
		
		
		@PostMapping("/seller/room/edit")
		public String roomEdit(@RequestParam("isRoomIn") boolean isRoomIn, 
				  @RequestParam("isElevator") boolean isElevator,
				  @RequestParam(value = "optionType", required = false) List<String> optionTypes,
				  @RequestParam(value = "uploadFile", required = false) List<MultipartFile> files,
				  Room room) throws Exception, IOException {
			
			// room insert
			room.setRoomIn(isRoomIn);
			room.setElevator(isElevator);
			roomService.insertRoom(room);
			System.out.println("room : " + room.toString());
			
			// 앞단에서 가져온 옵션들
			Set<String> optionTypesSet = new HashSet<>(optionTypes);
			
			// 원래 optionTypes의 optionType만 String으로 불러오기
			List<RoomOption> beforeRoomOption = roomService.getRoomOptions(room);
			Set<String> beforeRoomOptionSet = beforeRoomOption.stream()
											.flatMap(roomOption -> Stream.of(roomOption.getOptionType().getOptionType()))
											.collect(Collectors.toSet());
			
			// 옵션 변경되면 RoomOption delete/insert
			if(!beforeRoomOptionSet.equals(optionTypesSet)) {
////				for (String optionType : optionTypes) {
////					RoomOption roomOption = new RoomOption();
////					RoomOptionType roomOptionType = new RoomOptionType();
////					roomOptionType.setOptionType(optionType);
////					roomOption.setRoom(room);
////					roomOption.setOptionType(roomOptionType);
////					roomService.insertRoomOption(roomOption);
////				}
			}
			
			
			
			return "seller/room/editTest";
		}
		
		
}
