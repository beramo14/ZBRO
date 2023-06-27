package com.zbro.seller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.SellerUser;

public interface SellerRepository extends JpaRepository<SellerUser, Long>{
	
	
	
}
