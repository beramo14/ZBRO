package com.zbro.main.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessControlContext;
import java.security.Permission;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;

import org.hibernate.engine.transaction.spi.JoinStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zbro.main.service.MainService;
import com.zbro.main.service.UserService;
import com.zbro.model.ConsumerUser;
import com.zbro.model.RoomPhoto;
import com.zbro.model.SellerUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	
	@Autowired
	private MainService mainService;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/")
	public String mainPage() {
		
		//sysout
		System.out.println("called index mapper");
		
		//Slf4j log
		log.debug("called index mapper"); //debug level에서 실행시 표시(별도 설정 필요)
		log.info("called index mapper");
		log.warn("called index mapper");
		log.error("called index mapper");
		
		//slf4j log 사용시 되도록이면 + 연산자 사용하지 말고 slf4j의 치환문자를 사용하자
		log.info("Hello {}{}!!!","world"," tester");
		
		// 개발할때 잠깐 확인용으로 sysout 사용해도 상관없음
		// 가능 하면 Slf4j logger사용
		// logger사용 이유 : 어디서 로그 찍었는지 확인할 수 있음
		
		
		return "index";
		
		
	}
	
	
	@GetMapping("/join/seller")
	public String joinSellerView(@RequestParam(value="response", required=false, defaultValue= "") String response) {
		
		log.info("### join/seller response : {}",response);
		
		return "/join/seller_join";
	}
	
	
	@GetMapping("/join/consumer")
	public String joinConsumerView(@RequestParam(value="response", required=false, defaultValue= "") String response) {
		
		log.info("### join/consumer response : {}",response);
		
		return "/join/consumer_join";
	}
	
	
	
	@PostMapping("/join/consumer")
	public String joinConsumer(ConsumerUser user, @RequestParam("profilePhotoFile") MultipartFile file, Model model) {
		
		log.info("### joinConsumer = {}", user);
		
		//Todo... 백단에서 이메일 체크 후 중복시 회원가입으로 redirect할때 프론트에서 오류 처리
		boolean isUserExists = userService.consumerUserExistsCheck(user.getEmail());
		if(isUserExists == true) {
			model.addAttribute("response", "error");
			return "redirect:/join/consumer";
		}
		
		try {
			String filename = userService.profileImageSave(file);
			user.setProfilePhoto(filename);
			userService.consumerUserInsert(user);
		} catch (Exception e) {
			log.error("joinConsumer(POST) Error : ", e);
			
			model.addAttribute("response", "error");
			return "redirect:/join/consumer";
		}
		
		return "redirect:/login";
	}
	
	@PostMapping("/join/seller")
	public String joinSeller(SellerUser user, @RequestParam("profilePhotoFile") MultipartFile profilePhotoFile, @RequestParam("bizScanFile") MultipartFile bizScanFile, @RequestParam("isBiz") String isBizString, Model model) {
		
		log.info("### joinSeller = {}", user);
		
		//Todo... 백단에서 이메일 체크 후 중복시 회원가입으로 redirect할때 프론트에서 오류 처리
		boolean isUserExists = userService.sellerUserExistsCheck(user.getEmail());
		if(isUserExists == true) {
			model.addAttribute("response", "error");
			return "redirect:/join/seller";
		}
		
		try {
			//강제 비승인 처리
			user.setAdmission(false);
			user.setBiz("true".equalsIgnoreCase(isBizString));
			
			user.setProfilePhoto(null);
			if(profilePhotoFile.isEmpty() == false) {
				String profileImageFilename = userService.profileImageSave(profilePhotoFile);
				user.setProfilePhoto(profileImageFilename);
			}
			
			user.setBizFile(null);
			if(bizScanFile.isEmpty() == false && user.isBiz() == true) {
				String bizFilename = userService.bizFileSave(bizScanFile);
				user.setBizFile(bizFilename);
			}
			
			userService.sellerUserInsert(user);
		} catch (Exception e) {
			log.error("joinSeller(POST) Error : ", e);
			
			model.addAttribute("response", "error");
			return "redirect:/join/seller";
		}
		
		return "redirect:/login";
	}
	
	@PostMapping("/join/consumer/check/exists")
	public ResponseEntity<?> consumerUserExistsCheck(@RequestParam("email") String email) {
		boolean isUserExists = userService.consumerUserExistsCheck(email);
		
		return ResponseEntity.ok().body(isUserExists);
	}
	
	@PostMapping("/join/seller/check/exists")
	public ResponseEntity<?> sellerUserExistsCheck(@RequestParam("email") String email) {
		boolean isUserExists = userService.sellerUserExistsCheck(email);
		
		return ResponseEntity.ok().body(isUserExists);
	}
	
	
	@GetMapping("/login")
	public String loginSelectView() {
		
		return "login/login_select";
	}
	
	@GetMapping("/consumer/login")
	public String loginConsumerView() {
		
		return "login/consumer_login";
	}
	
	@GetMapping("/seller/login")
	public String loginSellerView() {
		
		return "login/seller_login";
	}
	
	
	/**
	 * 로그인된 회원 유저 정보 가져오는 코드 예시......
	 * 로그인 후 /login/test 접속
	 * @param authentication
	 * @return
	 */
	@GetMapping("/login/test")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> loginTest(Authentication authentication ){
		
		log.info("#### authentication : {}", authentication);
		
		Map<String, Object> testMap = new HashMap<>();

		
		//## 로그인 여부를 authentication가 null인지 아닌지로 구분(null이면 비로그인상태...)
		// ※주의!!! authentication null 체크 필수!!!!!(아래 if문 처럼 null처리 후 값을 불러와야함)
		//	null 체크 안하면 비로그인시 nullPointerException 발생
		//	별도로 비로그인처리를 해야하는 경우, null 체크 if문에 else 등으로 별도 처리(로그인 페이지로 redirect등...)
		if(authentication != null) {
			//## authentication.getName() : 현재 로그인된 유저의 이메일
			log.info("#### authentication.getName() : {}", authentication.getName());
			//## authentication.getAuthorities() : 현재 로그인된 유저의 권한을 조회(구매자:ROLE_CONSUMER, 판매자:ROLE_SELLER)
			log.info("#### authentication.getAuthorities() : {}", authentication.getAuthorities().toArray()[0]);
			String userType =  authentication.getAuthorities().toArray()[0].toString();
			
			//## 현재 로그인된 유저 이메일을 사용하여 유저 Entity를 조회
			if(userType.equals("ROLE_CONSUMER") == true) {
				log.info("#### userService.getConsumerUserByEmail(authentication.getName()) : {}", userService.getConsumerUserByEmail(authentication.getName()));
				ConsumerUser consumerUser = userService.getConsumerUserByEmail(authentication.getName());
				testMap.put("findedUser", consumerUser);
			} else if(userType.equals("ROLE_SELLER") == true) {
				SellerUser sellerUser = userService.getSellerUserByEmail(authentication.getName());
				testMap.put("findedUser", sellerUser);
				
			}
			
		}
		
		testMap.put("username", authentication.getName());
		testMap.put("authentication", authentication);
		
		
		return ResponseEntity.ok().body(testMap);
	}
	
	
	@GetMapping("/seller/profile/photo")
	public ResponseEntity<Resource> getSellerProfilePhoto(SellerUser user) throws FileNotFoundException {
		SellerUser findedSellerUser = userService.getSellerUser(user.getSellerId());
		
		Resource imageResource = userService.getProfileImageResource(findedSellerUser.getProfilePhoto());
		return ResponseEntity.ok().body(imageResource);
	}
	
	@GetMapping("/consumer/profile/photo")
	public ResponseEntity<Resource> getConsumerProfilePhoto(ConsumerUser user) throws FileNotFoundException {
		ConsumerUser findedConsumerUser = userService.getConsumerUser(user.getConsumerId());
		
		Resource imageResource = userService.getProfileImageResource(findedConsumerUser.getProfilePhoto());
		return ResponseEntity.ok().body(imageResource);
	}
	
	
	
}
