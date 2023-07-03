package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.RoomPhoto;

public interface RoomPhotoRepository extends JpaRepository<RoomPhoto, String> {
    
    List<RoomPhoto> findByRoomRoomId(Long roomId);
}