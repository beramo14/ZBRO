package com.zbro.main.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessControlContext;
import java.security.Permission;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.security.auth.Subject;

import org.hibernate.engine.transaction.spi.JoinStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zbro.main.service.MainService;
import com.zbro.main.service.UserService;
import com.zbro.model.ConsumerPasswordToken;
import com.zbro.model.ConsumerUser;
import com.zbro.model.RoomPhoto;
import com.zbro.model.SellerPasswordToken;
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
		
		return "join/seller_join";
	}
	
	
	@GetMapping("/join/consumer")
	public String joinConsumerView(@RequestParam(value="response", required=false, defaultValue= "") String response) {
		
		log.info("### join/consumer response : {}",response);
		
		return "join/consumer_join";
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
	public ResponseEntity<Map<String, Object>> loginTest(Authentication authentication ) {
		
		log.info("#### authentication : {}", authentication);
		
		Map<String, Object> responseMap = new HashMap<>();

		/*
		 * ## 로그인 여부를 authentication가 null인지 아닌지로 구분(null이면 비로그인상태...)
		 * ※주의!!!	authentication null 체크 필수!!!!!(아래 if문 처럼 null처리 후 값을 불러와야함)
		 *	        null 체크 안하면 비로그인시 nullPointerException 발생할 수 있음
		 * */
		if(authentication != null) {
			//## authentication.getName() : 현재 로그인된 유저의 이메일
			log.info("#### authentication.getName() : {}", authentication.getName());
			//## authentication.getAuthorities() : 현재 로그인된 유저의 권한을 조회(구매자:ROLE_CONSUMER, 판매자:ROLE_SELLER)
			log.info("#### authentication.getAuthorities() : {}", authentication.getAuthorities().toArray()[0].toString());
			
			// Collection<GrantedAuthority> => List<String>
			List<String> authorityList = authentication.getAuthorities().stream().map(authority->authority.getAuthority()).collect(Collectors.toList());
			
			//## 현재 로그인된 유저 이메일을 사용하여 유저 Entity를 조회
			if(authorityList.contains("ROLE_CONSUMER")) {
				log.info("#### userService.getConsumerUserByEmail(authentication.getName()) : {}", userService.getConsumerUserByEmail(authentication.getName()));
				ConsumerUser consumerUser = userService.getConsumerUserByEmail(authentication.getName());
				responseMap.put("findedUser", consumerUser);
			} else if(authorityList.contains("ROLE_SELLER")) {
				SellerUser sellerUser = userService.getSellerUserByEmail(authentication.getName());
				responseMap.put("findedUser", sellerUser);
			}
			responseMap.put("username", authentication.getName());
		}
		
		responseMap.put("authentication", authentication);
		
		return ResponseEntity.ok().body(responseMap);
	}
	
	
	
	@GetMapping("/consumer/login/find/account")
	@ResponseBody
	public ResponseEntity<?> findCounsumerAccount(@RequestParam("email") String email) {
		return ResponseEntity.ok().body(userService.findConsumerAccountByEmail(email).isPresent());
	}
	
	@GetMapping("/seller/login/find/account")
	@ResponseBody
	public ResponseEntity<?> findSellerAccount(@RequestParam("email") String email) {
		return ResponseEntity.ok().body(userService.findSellerAccountByEmail(email).isPresent());
	}
	
	
	@GetMapping("/consumer/login/find/password")
	@ResponseBody
	public ResponseEntity<?> findConsumerPassword(@RequestParam("email") String email) throws MessagingException {
		
		Optional<ConsumerUser> findedUser = userService.findConsumerAccountByEmail(email);
		
		if(findedUser.isEmpty() == true) {
			return ResponseEntity.ok().body("email-not-found");
		}
		
		userService.consumerPasswordChangeMail(findedUser.get());
		return ResponseEntity.ok().body("email-sended");
	}
	
	@GetMapping("/seller/login/find/password")
	@ResponseBody
	public ResponseEntity<?> findSellerPassword(@RequestParam("email") String email) throws MessagingException {
		
		Optional<SellerUser> findedUser = userService.findSellerAccountByEmail(email);
		
		if(findedUser.isEmpty() == true) {
			return ResponseEntity.ok().body("email-not-found");
		}
		
		userService.sellerPasswordChangeMail(findedUser.get());
		return ResponseEntity.ok().body("email-sended");
	}
	
	
	
	@GetMapping("/consumer/password/{token}")
	public String consumerPasswordChangeView(@PathVariable("token") String token, Model model) {
		
		Optional<ConsumerPasswordToken> findedConsumerPasswordToken = userService.getConsumerPasswordTokenByToken(token);
		
		if(findedConsumerPasswordToken.isPresent() == true) {
			ConsumerPasswordToken consumerPasswordToken = findedConsumerPasswordToken.get();
			//현재 시간이 토큰만료일 후 인지 확인
			if(LocalDateTime.now().isAfter(consumerPasswordToken.getExpiredDate()) == true) {
				model.addAttribute("errorMessage", "링크가 만료되었습니다.");
				return "login/password_change_error";
			}
		} else if(findedConsumerPasswordToken.isEmpty() == true) {
			model.addAttribute("errorMessage", "토큰이 존재하지 않습니다.");
			return "login/password_change_error";
		}
		
		model.addAttribute("type", "consumer");
		model.addAttribute("token", token);
		
		return "login/password_change";
	}
	
	@GetMapping("/seller/password/{token}")
	public String sellerPasswordChangeView(@PathVariable("token") String token, Model model) {
		
		Optional<SellerPasswordToken> findedSellerPasswordToken = userService.getSellerPasswordTokenByToken(token);
		
		if(findedSellerPasswordToken.isPresent() == true) {
			SellerPasswordToken sellerPasswordToken = findedSellerPasswordToken.get();
			//현재 시간이 토큰만료일 후 인지 확인
			if(LocalDateTime.now().isAfter(sellerPasswordToken.getExpiredDate()) == true) {
				model.addAttribute("errorMessage", "링크가 만료되었습니다.");
				return "login/password_change_error";
			}
		} else if(findedSellerPasswordToken.isEmpty() == true) {
			model.addAttribute("errorMessage", "토큰이 존재하지 않습니다.");
			return "login/password_change_error";
		}
		
		model.addAttribute("type", "seller");
		model.addAttribute("token", token);
		
		return "login/password_change";
	}
	
	@PostMapping("/consumer/password/change")
	public String consumerPasswordChange(@RequestParam("token") String token, @RequestParam("changePassword") String password, Model model) {
		Optional<ConsumerPasswordToken> findedConsumerPasswordToken = userService.getConsumerPasswordTokenByToken(token);
		
		if(findedConsumerPasswordToken.isPresent() == true) {
			ConsumerPasswordToken consumerPasswordToken = findedConsumerPasswordToken.get();
			//현재 시간이 토큰만료일 후 인지 확인
			if(LocalDateTime.now().isAfter(consumerPasswordToken.getExpiredDate()) == true) {
				model.addAttribute("errorMessage", "링크가 만료되었습니다.");
				return "login/password_change_error";
			}
			//비밀번호 변경
			userService.updateConsumerPassword(consumerPasswordToken.getUser(), password);
			//토큰 삭제
			userService.deleteConsumerPasswordToken(consumerPasswordToken);
		} else if(findedConsumerPasswordToken.isEmpty() == true) {
			model.addAttribute("errorMessage", "토큰이 존재하지 않습니다.");
			return "login/password_change_error";
		}
		
		return "redirect:/login";
	}
	@PostMapping("/seller/password/change")
	public String sellerPasswordChange(@RequestParam("token") String token, @RequestParam("changePassword") String password, Model model) {
		Optional<SellerPasswordToken> findedSellerPasswordToken = userService.getSellerPasswordTokenByToken(token);
		
		if(findedSellerPasswordToken.isPresent() == true) {
			SellerPasswordToken sellerPasswordToken = findedSellerPasswordToken.get();
			//현재 시간이 토큰만료일 후 인지 확인
			if(LocalDateTime.now().isAfter(sellerPasswordToken.getExpiredDate()) == true) {
				model.addAttribute("errorMessage", "링크가 만료되었습니다.");
				return "login/password_change_error";
			}
			//비밀번호 변경
			userService.updateSellerPassword(sellerPasswordToken.getUser(), password);
			//토큰 삭제
			userService.deleteSellerPasswordToken(sellerPasswordToken);
		} else if(findedSellerPasswordToken.isEmpty() == true) {
			model.addAttribute("errorMessage", "토큰이 존재하지 않습니다.");
			return "login/password_change_error";
		}
		
		return "redirect:/login";
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
