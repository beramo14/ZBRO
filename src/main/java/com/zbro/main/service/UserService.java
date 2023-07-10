package com.zbro.main.service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${file.biz}")
	private String fileBizPath;
	
	@Value("${file.images.profile-photo}")
	private String fileProfilePhotoPath;
	
	@Value("${file.images.profile-default-file}")
	private String defaultProfileFileName;
	

	public void consumerUserSave(ConsumerUser user) {
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

	public void sellerUserSave(SellerUser user) {
		sellerRepository.save(user);
		
	}
	
	
	
	
	public String profileImageSave(MultipartFile profilePhotoFile) throws IllegalStateException, IOException {
		if (!profilePhotoFile.isEmpty()) {
    		UUID uuid = UUID.randomUUID();
    		String fileName = uuid.toString()+"_"+profilePhotoFile.getOriginalFilename();
    		
    		log.info("original file name = {}", profilePhotoFile.getOriginalFilename());
    		log.info("final file Name = {}", fileName);
    		
    		profilePhotoFile.transferTo(new File(fileProfilePhotoPath+fileName));
    		
    		return fileName;
		} else {
			return defaultProfileFileName;
		}
	}

	public String bizFileSave(MultipartFile bizScanFile) throws IllegalStateException, IOException {
		if (!bizScanFile.isEmpty()) {
    		UUID uuid = UUID.randomUUID();
    		String fileName = uuid.toString()+"_"+bizScanFile.getOriginalFilename();
    		
    		log.info("original file name = {}", bizScanFile.getOriginalFilename());
    		log.info("final file Name = {}", fileName);
    		
    		bizScanFile.transferTo(new File(fileBizPath+fileName));
    		
    		return fileName;
		} else {
			throw new RuntimeException("bizScanFile is not File!");
		}
	}

}
