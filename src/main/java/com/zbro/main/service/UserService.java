package com.zbro.main.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zbro.main.repository.ConsumerUserRepository;
import com.zbro.main.repository.SellerUserRepository;
import com.zbro.model.ConsumerUser;
import com.zbro.model.SellerUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	private ConsumerUserRepository consumerRepository;
	
	@Autowired
	private SellerUserRepository sellerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Value("${file.biz}")
	private String fileBizPath;
	
	@Value("${file.images.profile-photo}")
	private String fileProfilePhotoPath;
	
	@Value("${file.images.profile-default-file}")
	private String defaultProfileFileName;
	

	public void consumerUserInsert(ConsumerUser user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		consumerRepository.save(user);
	}

	public boolean consumerUserExistsCheck(String email) {
		
		Optional<ConsumerUser> findedUser = consumerRepository.findByEmail(email);
		
		return findedUser.isPresent();
		
	}

	public boolean sellerUserExistsCheck(String email) {
		Optional<SellerUser> findedUser = sellerRepository.findByEmail(email);
		
		return findedUser.isPresent();
	}

	public void sellerUserInsert(SellerUser user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		sellerRepository.save(user);
		
	}
	
	public SellerUser getSellerUser(Long userId) {
		
		return sellerRepository.findById(userId).get();
	}
	public ConsumerUser getConsumerUser(Long userId) {
		
		return consumerRepository.findById(userId).get();
	}

	public void updateSellerUser(Long userId, SellerUser sellerUser) {
		SellerUser findedSellerUser = sellerRepository.findById(userId).get();
		
		findedSellerUser.setName(sellerUser.getName());
		findedSellerUser.setTel1(sellerUser.getTel1());
		findedSellerUser.setTel2(sellerUser.getTel2());
		findedSellerUser.setTel3(sellerUser.getTel3());
		findedSellerUser.setBiz(sellerUser.isBiz());
		
		if(sellerUser.getProfilePhoto() != null) {
			findedSellerUser.setProfilePhoto(sellerUser.getProfilePhoto());
		}
		
		//판매자 승인되지않은 경우에만 수정
		if(findedSellerUser.isAdmission() == false) {
			if(findedSellerUser.isBiz() == true) {
				findedSellerUser.setBrokerName(sellerUser.getBrokerName());
				if(sellerUser.getBizFile() != null) {
					findedSellerUser.setBizFile(sellerUser.getBizFile());
				}
			}
		}
		
		sellerRepository.save(findedSellerUser);
		
	}
	
	
	
	/**
	 * 프로필이미지 파일 처리
	 * @param profilePhotoFile
	 * @return 파일명+확장자 : String
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public String profileImageSave(MultipartFile profilePhotoFile) throws IllegalStateException, IOException {
		if (!profilePhotoFile.isEmpty()) {
			String uuid = UUID.randomUUID().toString();
			String fileName = uuid.substring(0, 8)+"_"+profilePhotoFile.getOriginalFilename();
    		
    		log.info("original file name = {}", profilePhotoFile.getOriginalFilename());
    		log.info("final file Name = {}", fileName);
    		
    		profilePhotoFile.transferTo(new File(fileProfilePhotoPath+fileName));
    		
    		return fileName;
		} else {
			return defaultProfileFileName;
		}
	}

	/**
	 * 사업자 등록증 파일 처리
	 * @param bizScanFile
	 * @return 파일명+확장자 : String
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public String bizFileSave(MultipartFile bizScanFile) throws IllegalStateException, IOException {
		if (!bizScanFile.isEmpty()) {
    		String uuid = UUID.randomUUID().toString();
    		String fileName = uuid.substring(0, 8)+"_"+bizScanFile.getOriginalFilename();
    		
    		log.info("original file name = {}", bizScanFile.getOriginalFilename());
    		log.info("final file Name = {}", fileName);
    		
    		bizScanFile.transferTo(new File(fileBizPath+fileName));
    		
    		return fileName;
		} else {
			throw new RuntimeException("bizScanFile is not File!");
		}
	}
	
	
	/**
	 * 프로필이미지 파일 삭제
	 */
	public void profileImageDelete(SellerUser sellerUser) {
		
		File profilePhotoFile = new File(fileProfilePhotoPath+sellerUser.getProfilePhoto());
		
		if(profilePhotoFile.exists() && profilePhotoFile.isFile()) {
			boolean isFileDeleted = profilePhotoFile.delete();
			if(isFileDeleted == true) {
				log.info("profilePhotoFileDelete Success!!");
			}
		} else if(profilePhotoFile.exists() == false) {
			log.info("profilePhotoFile is not Exist");
		}
	}
	
	/**
	 * 사업자 등록증 파일 삭제
	 */
	public void bizFileDelete(SellerUser sellerUser) {
		
		String tempBizFile = sellerUser.getBizFile();
		
		sellerUser.setBizFile(null);
		sellerRepository.save(sellerUser);
		
		File bizFile = new File(fileBizPath+tempBizFile);
		
		if(bizFile.exists() && bizFile.isFile()) {
			boolean isFileDeleted = bizFile.delete();
			if(isFileDeleted == true) {
				log.info("bizFileDelete Success!!");
			}
		} else if(bizFile.exists() == false) {
			log.info("bizFile is not Exist");
		}
	}

	/**
	 * 각 유저의 proiflePhoto 파일명을 가지고 이미지 Resource를 반환
	 * @param profilePhotoFilename
	 * @return Resource
	 * @throws FileNotFoundException
	 */
	public Resource getProfileImageResource(String profilePhotoFilename) throws FileNotFoundException {
		
		File file = new File(fileProfilePhotoPath + profilePhotoFilename);
		
		if(file.exists() == false || file.isFile() == false) {
			throw new FileNotFoundException("file not found : " +fileProfilePhotoPath + profilePhotoFilename);
		}

		InputStream fis = new FileInputStream(file);
		Resource imageResource = new InputStreamResource(fis);
		return imageResource;
		
	}


}
