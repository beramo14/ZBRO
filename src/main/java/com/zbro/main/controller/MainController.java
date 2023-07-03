package com.zbro.main.controller;

import java.io.IOException;

import org.hibernate.engine.transaction.spi.JoinStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zbro.main.service.MainService;
import com.zbro.main.service.UserService;
import com.zbro.model.ConsumerUser;
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
	public String joinSellerView() {
		return "/join/seller_join";
	}
	
	@PostMapping("/join/seller")
	public String joinSeller(SellerUser user) {
		
		log.info("### joinConsumer = {}", user);
		
		/*join logic*/
		
		return "redirect:/"; //셀러 메인 페이지로 수정해야함
	}
	
	@GetMapping("/join/consumer")
	public String joinConsumerView() {
		return "/join/consumer_join";
	}
	
	
	
	@PostMapping("/join/consumer")
	public String joinConsumer(ConsumerUser user, @RequestParam("profilePhotoFile") MultipartFile file, Model model) {
		
		log.info("### joinConsumer = {}", user);
		
		/*뷰단 & 여기 에서 이메일 중복확인*/
		
		/*join logic*/
		String filename;
		try {
			filename = userService.profileImageSave(file);
			user.setProfilePhoto(filename);
			userService.consumerUserSave(user);
		} catch (Exception e) {
			log.error(e.getMessage(), e.getStackTrace());
			
			model.addAttribute("response", "error");
			return "redirect:/join/consumer";
		}
		
		
		
		return "redirect:/login";
	}
	
	
	@GetMapping("/login")
	public String loginSelectView() {
		
		return "login/login_select";
	}
	
	@GetMapping("/login/consumer")
	public String loginConsumerView() {
		
		return "login/consumer_login";
	}
	
	@PostMapping("/login/consumer")
	public String loginConsumer(ConsumerUser user) {
		log.info("### loginConsumer = {}", user);
		
		/*Login logic*/
		
		return "redirect:/";
	}
	
	
	
}
