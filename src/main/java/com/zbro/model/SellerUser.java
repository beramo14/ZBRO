package com.zbro.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Comment;

import com.zbro.type.UserStatusType;

import lombok.Data;

@Data
@Entity
public class SellerUser {

	@Comment("판매자_아이디")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;
	
	@Comment("이메일")
	@Column(unique = true, nullable = false, length = 100)
	private String email;
	
	@Comment("이름")
	@Column(nullable = false, length = 50)
	private String name;
	
	@Comment("비밀번호")
	@Column(nullable = false)
	private String password;
	
	@Comment("프로필사진 파일명")
	private String profilePhoto;
	
	@Comment("판매자 승인여부")
	private boolean admission;
	
	@Comment("전화번호_1")
	private int tel1;
	
	@Comment("전화번호_2")
	private int tel2;

	@Comment("전화번호_3")
	private int tel3;
	
	@Comment("판매자 소개")
	private String description;
	
	@Comment("부동산명")
	@Column(length = 30)
	private String brokerName;
	
	@Comment("사업자 여부")
	private boolean isBiz;
	
	@Comment("상태")
	@Enumerated(EnumType.STRING)
	@Column(updatable = false, insertable = false, columnDefinition = "VARCHAR(10) DEFAULT 'ACTIVE'")
	private UserStatusType status;
	
	@Comment("등록일")
	@Column(insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;
	
	
	@OneToMany(mappedBy="seller", cascade = CascadeType.ALL)
	private List<Room> rooms = new ArrayList<>();
}
