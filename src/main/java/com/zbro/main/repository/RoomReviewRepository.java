package com.zbro.main.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.dto.RoomReviewDTO;
import com.zbro.model.RoomReview;

public interface RoomReviewRepository extends JpaRepository<RoomReview, Long>{

	/* List<RoomReview> findById(); */

	 List<RoomReview> findByRoomRoomId(Long roomId);
	 
	 Page<RoomReview> findByUserConsumerId(Long consumerId, Pageable pageable);
}
