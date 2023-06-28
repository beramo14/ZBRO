package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Favorite;



public interface MypageRepository extends JpaRepository<Favorite, Long>{
	
	List<Favorite> findByUserConsumerId(Long consumerId);

}
