package com.zbro.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.zbro.model.Room;
import com.zbro.type.CostType;
import com.zbro.type.RoomType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomSearchDTO {
	
	/*### Search Field ###*/
	private String searchWord = "";
	private RoomType type;
	private CostType costType;
	/*### Search Field ###*/
	
	private Long roomId;
	private String address;
	private String intro;
	private int monthCost;
	private int deposit;
	
	
	public RoomSearchDTO(Room room) {
		this.roomId = room.getRoomId();
		this.type = (room.getType() == null)? RoomType.원룸 : room.getType();
		this.costType = (room.getCostType() == null)? CostType.월세 : room.getCostType();
		this.address = (room.getAddress() == null)? "" : room.getAddress();
		this.intro = (room.getIntro() == null)? "" : room.getIntro();
		this.monthCost = room.getMonthCost();
		this.deposit = room.getDeposit();
	}
	
	public static List<RoomSearchDTO> convertToDTO(List<Room> rooms) {
	        return rooms.stream()
	                    .map(RoomSearchDTO::new)
	                    .collect(Collectors.toList());
    }
	
}


