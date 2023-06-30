package com.zbro.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.FavoritRepository;
import com.zbro.model.Favorite;

@Service
public class FavoriteService {
	
	@Autowired
	private FavoritRepository favRepository;

	public Favorite favorite(Favorite fav) {
		return favRepository.save(fav);
	}
	
	public void unfavorite(Favorite fav) {
		favRepository.deleteById(fav.getFavoriteId());
	}

}
