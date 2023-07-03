package com.zbro.seller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Room;

public interface SellerRoomRepository extends JpaRepository<Room, Long>{
	
}
