package com.zbro.main.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Room;
import com.zbro.model.SellerUser;

public interface RoomRepository extends JpaRepository<Room, Long>{
	
	List<Room> findBySeller(SellerUser selleruser);
	
	 
}
