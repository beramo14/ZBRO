package com.zbro.seller.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Room;
import com.zbro.model.SellerUser;

public interface SellerRoomRepository extends JpaRepository<Room, Long>{

	List<Room> findAllBySeller(SellerUser sellerUser);
	
}
