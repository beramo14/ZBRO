package com.zbro.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.MypageRepository;
import com.zbro.main.repository.RoomOptionRepository;
import com.zbro.main.repository.RoomPhotoRepository;
import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomPhoto;

@Service
public class MypageService {
	
	@Autowired
	private MypageRepository mypageRepository;
	
	@Autowired
	private RoomOptionRepository roomOptionRepository;
	
	@Autowired
	private RoomPhotoRepository roomPhotoRepository;
	
	
    public List<Favorite> getFavoritesByUserConsumerId() {
        Long userId = getLoggedInUserId();
        return mypageRepository.findByUserConsumerId(userId);
    }
    
    private Long getLoggedInUserId() {
        return 1L;
    }
    
    public List<RoomOption> getRoomOptionsByRoomId(Long roomId) {
        return roomOptionRepository.findByRoomRoomId(roomId);
    }
    
    public void saveMemo(Long favoriteId, String memo) {
        Favorite favorite = mypageRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid favoriteId"));
        favorite.setMemo(memo);
        mypageRepository.save(favorite);
    }
    
    public List<RoomPhoto> getRoomPhotosByRoomId(Long roomId) {
        return roomPhotoRepository.findByRoomRoomId(roomId);
    }
    
    public void deleteFavorite(Long favoriteId) {
        mypageRepository.deleteById(favoriteId);
    }
    
}
