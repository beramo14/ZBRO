package com.zbro.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zbro.admin.repository.AdminRoomRepository;
import com.zbro.model.Room;

@Service
public class AdminRoomService {
	
	@Autowired
	private AdminRoomRepository roomRepo;
	
    public Page<Room> findAllOrderedByRoomId(Pageable pageable) {
        return roomRepo.findAll(pageable);
    }

    public Page<Room> searchRoomsBySellerOrBuildingNameOrAddress(String searchType, List<String> types, String searchKeyword, Pageable pageable) {
        List<Room> filteredRooms = new ArrayList<>();
        Page<Room> rooms = findAllOrderedByRoomId(pageable);

        for (Room room : rooms.getContent()) {
            if (isMatchedWithSearchCriteria(room, types, searchType, searchKeyword)) {
                filteredRooms.add(room);
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredRooms.size());
        return new PageImpl<>(filteredRooms.subList(start, end), pageable, filteredRooms.size());
    }

    private boolean isMatchedWithSearchCriteria(Room room, List<String> types,String searchType, String searchKeyword) {
        if (types != null && !types.isEmpty() && !types.contains(room.getType().toString())) {
            return false;
        }       
    	if (searchType != null && searchKeyword != null) {
            if (searchType.equals("seller")) {
                return room.getSeller().getName().contains(searchKeyword);
            } else if (searchType.equals("broker")) {
                return room.getSeller().getBrokerName().contains(searchKeyword);
            } else if (searchType.equals("buildingName")) {
                return room.getBuildingName().contains(searchKeyword);
            } else if (searchType.equals("address")) {
                return room.getAddress().contains(searchKeyword);
            }
        }

        return true;
    }
}
