package com.zbro.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.MypageRepository;
import com.zbro.model.Favorite;

@Service
public class MypageService {
	
	@Autowired
	private MypageRepository mypageRepository;
	
	
    public List<Favorite> getFavoritesLogInUserId() {
        // 현재 로그인한 사용자의 ID를 가져와서 해당 사용자의 찜 목록을 가져옴
        Long userId = getLogInUserId();
        return mypageRepository.findByUserConsumerId(userId);
    }
    
    private Long getLogInUserId() {
        return 1L;
    }
}
