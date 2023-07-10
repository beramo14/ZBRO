package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Community;
import com.zbro.model.Favorite;




public interface CommunityRepository extends JpaRepository<Community, Long> {

	 List<Community> findByUserConsumerId(Long consumerId);
	 
	
	 
	

}