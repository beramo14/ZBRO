package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Room;
import com.zbro.model.RoomOption;

public interface RoomOptionRepository extends JpaRepository<RoomOption, Long>{
	
	
	//옵션타입을 불러오는 쿼리문
	 List<RoomOption> findByRoom(Room room);
}
