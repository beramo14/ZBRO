package com.zbro.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zbro.admin.service.AdminRoomService;
import com.zbro.model.Room;





@Controller
public class AdminController {

	
	
	@Autowired
	private AdminRoomService roomService;
	
    @GetMapping("/admin/room/list")
    public String roomList(Model model,
                           @RequestParam(value = "searchType", required = false) String searchType,
                           @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                           @RequestParam(value = "type", required = false) List<String> types,
                           @RequestParam(defaultValue = "0") int pageNo,
                           @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Room> rooms;
        if ((searchType != null && searchKeyword != null) || (types != null && !types.isEmpty())) {
            rooms = roomService.searchRoomsBySellerOrBuildingNameOrAddress(searchType,types, searchKeyword, pageable);
        } else {
            rooms = roomService.findAllOrderedByRoomId(pageable);
        }

        model.addAttribute("rooms", rooms);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("types", types);

        return "admin/AroomList";
    }

}
