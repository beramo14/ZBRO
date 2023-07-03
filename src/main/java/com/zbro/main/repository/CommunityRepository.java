package com.zbro.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Community;



public interface CommunityRepository extends JpaRepository<Community, Long>{
	
	
}
