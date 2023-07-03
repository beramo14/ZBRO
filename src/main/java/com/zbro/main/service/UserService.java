package com.zbro.main.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zbro.main.repository.ConsumerUserRepository;
import com.zbro.model.ConsumerUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	private ConsumerUserRepository consumerRepository;
	
	@Value("${file.images.profile-photo}")
	private String fileProfilePhotoPath;
	
	@Value("${file.images.profile-default-file}")
	private String defaultProfileFileName;

	public String profileImageSave(MultipartFile file) throws IllegalStateException, IOException {
		if (!file.isEmpty()) {
    		UUID uuid = UUID.randomUUID();
    		String fileName = uuid.toString()+"_"+file.getOriginalFilename();
    		
    		log.info("original file name = {}", file.getOriginalFilename());
    		log.info("final file Name = {}", fileName);
    		
    		file.transferTo(new File(fileProfilePhotoPath+fileName));
    		
    		return fileName;
		} else {
			return defaultProfileFileName;
		}
	}

	public void consumerUserSave(ConsumerUser user) {
		consumerRepository.save(user);
	}

}
