package com.zbro.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.CommentRepository;
import com.zbro.main.repository.CommunityRepository;
import com.zbro.main.repository.ConsumerUserRepository;
import com.zbro.main.repository.MypageRepository;
import com.zbro.main.repository.RoomOptionRepository;
import com.zbro.main.repository.RoomPhotoRepository;
import com.zbro.model.Comment;
import com.zbro.model.Community;
import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomPhoto;
import com.zbro.type.PostType;
import com.zbro.type.UserStatusType;

@Service
public class MypageService {
	
	@Autowired
	private MypageRepository mypageRepository;
	
	@Autowired
	private RoomOptionRepository roomOptionRepository;
	
	@Autowired
	private RoomPhotoRepository roomPhotoRepository;
	
	@Autowired
	private CommunityRepository communityRepository;
	
	@Autowired
	private ConsumerUserRepository consumerUserRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	
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
    
    
	public List<Community> getCommunitiesByUser() {
		Long userId = getLoggedInUserId();
		return communityRepository.findByUserConsumerId(userId);
	}
	
	
	public List<Comment> getCommentsByUser() {
		Long userId = getLoggedInUserId();
		return commentRepository.findByUserConsumerId(userId);
	}
	
	
	public ConsumerUser getConsumerUser() {
	    Long userId = getLoggedInUserId();
	    return consumerUserRepository.findByConsumerId(userId);
	}
	
    public ConsumerUser updateUserInfo(ConsumerUser consumerUser) {       
        return consumerUserRepository.save(consumerUser);
    }
    
    public void memberDelete() {
        Long userId = getLoggedInUserId();
        ConsumerUser consumerUser = consumerUserRepository.findByConsumerId(userId);
        consumerUser.setStatus(UserStatusType.DELETE);
        consumerUserRepository.save(consumerUser);
    }
    
    

    
}
