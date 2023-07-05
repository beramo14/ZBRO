package com.zbro.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.CommunityRepository;
import com.zbro.model.Community;
import com.zbro.type.PostType;

@Service
public class CommunityService {
	
	@Autowired
	private CommunityRepository commuRepo;

	
	public Page<Community> getPosts(Pageable pageable, String searchType, String searchWord, PostType type, String categoryType) {
		if(searchType.equalsIgnoreCase("title")) {
			return commuRepo.findByTitleContainingAndTypeAndCategoryType(searchWord, type, categoryType, pageable);
		} else if(searchType.equalsIgnoreCase("content")) {
			return commuRepo.findByContentContainingAndTypeAndCategoryType(searchWord, type, categoryType, pageable);
		} else if(searchType.equalsIgnoreCase("user")) {
			return commuRepo.findByUserContainingAndTypeAndCategoryType(searchWord, type, categoryType, pageable);
		} else {
	        return commuRepo.findByTypeAndCategoryType(type, categoryType, pageable);
	    }
	}

}
