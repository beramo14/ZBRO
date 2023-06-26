package com.zbro.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Comment;

import com.zbro.type.ImageType;

import lombok.Data;

@Data
@Entity
public class RoomPhoto {

	@Id
	@Comment("파일_이름")
	private String fileName;
	
	@Comment("매물_id")
	@ManyToOne
	@JoinColumn(name="room_id", nullable = false)
	private Room room;
	
	@Comment("이미지_유형")
	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private ImageType imageType;
	
}
