package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{
	
	List<Room> findRoomsByAddressContaining(String searchWord);
}
