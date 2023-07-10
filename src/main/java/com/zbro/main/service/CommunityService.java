package com.zbro.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.CommunityRepository;
import com.zbro.model.Community;
import com.zbro.model.ConsumerUser;
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
			return commuRepo.findByUser_NameContainingAndTypeAndCategoryType(searchWord, type, categoryType, pageable);
		} else {
	        return commuRepo.findByTypeAndCategoryType(type, categoryType, pageable);
	    }
	}


	public void postInsert(PostType postType, String categoryType, String title, String content, long userId) {
		ConsumerUser user = new ConsumerUser();
		user.setConsumerId(userId);
		
		// 나머지 매개변수를 사용하여 Community 객체 생성 및 저장
		Community community = new Community();
		community.setType(postType);
		community.setCategoryType(categoryType);
		community.setTitle(title);
		community.setContent(content);
		community.setUser(user);
		
		commuRepo.save(community);
	}


	public Community getPost(Long postId) {
		Optional<Community> findPost = commuRepo.findById(postId);
		
		if(findPost.isPresent()) {
			Community post = findPost.get();
			post.setViewCount(post.getViewCount()+1);
			commuRepo.save(post);
			return post;
		}
		else return null;
	}


	public void postRevise(PostType postType, String categoryType, Long postId, String title, String content,
			long userId) {
		ConsumerUser user = new ConsumerUser();
		user.setConsumerId(userId);
		
		Community community = new Community();
		community.setType(postType);
		community.setCategoryType(categoryType);
		community.setPostId(postId);
		community.setTitle(title);
		community.setContent(content);
		community.setUser(user);
		
		commuRepo.save(community);
	}


	public void postDelete(Long postId) {
		commuRepo.deleteById(postId);
	}

}
