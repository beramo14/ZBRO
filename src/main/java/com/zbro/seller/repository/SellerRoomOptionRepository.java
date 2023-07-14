package com.zbro.seller.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zbro.model.Room;
import com.zbro.model.RoomOption;

public interface SellerRoomOptionRepository extends JpaRepository<RoomOption, Long>{

	List<RoomOption> findAllByRoom(Room findRoom);

}
