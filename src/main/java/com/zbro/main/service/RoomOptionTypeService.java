package com.zbro.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.RoomOptionTypeRepository;
import com.zbro.model.RoomOptionType;

@Service
public class RoomOptionTypeService {
	
    @Autowired
    private RoomOptionTypeRepository roomOptionTypeRepository;

    public List<RoomOptionType> getAllRoomOptionTypes() {
        return roomOptionTypeRepository.findAll();
    }
}
