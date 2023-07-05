package com.zbro.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zbro.dto.PageInfo;
import com.zbro.main.service.CommunityService;
import com.zbro.model.Community;
import com.zbro.type.PostType;

@Controller
public class CommunityController {
	
	@Autowired
	private CommunityService commuService;
	
	@RequestMapping("/post_list")
	public String listView(Model model,
							@RequestParam(defaultValue = "0") int page,
							@RequestParam(defaultValue = "10") int pageSize,
							@RequestParam(defaultValue = "자취") String categoryType,
							@RequestParam(defaultValue = "title") String searchType,
							@RequestParam(defaultValue = "") String searchWord,
							@RequestParam(defaultValue = "꿀팁") String type) {
		PostType postType = PostType.valueOf(type);
		
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("postId").descending());
		Page<Community> pageResult = commuService.getPosts(pageable, searchType, searchWord, postType, categoryType);
		
		List<Community> postList = pageResult.getContent();
		
		// 페이징 확인
		for(Community post:postList) {
			System.out.println(post.toString());
		}
		
		// 쿼리실행 후 총 게시물 수
		int resultTotalCnt = pageResult.getNumberOfElements();
		System.out.println("쿼리실행 후 총 게시물 수 : " + resultTotalCnt);
		
		// 페이징처리
		PageInfo pageInfo = new PageInfo((int)pageResult.getTotalElements(), page, pageSize, searchType, searchWord, categoryType);
		
		
		model.addAttribute("pageable", pageable);		// 쿼리문 실행 전 페이징 정보
		model.addAttribute("pageInfo", pageInfo);		// 쿼리문 실행 후 페이징 정보
		model.addAttribute("pageResult", pageResult);	// 페이징 처리된 데이터
		
		model.addAttribute("pg", pageInfo);
		model.addAttribute("bp", pageInfo.getBeginPage());
		model.addAttribute("ep", pageInfo.getEndPage());
		model.addAttribute("ns", pageInfo.getNaviSize());
		model.addAttribute("ps", pageSize);
		model.addAttribute("tp", pageInfo.getTotalPage());
		model.addAttribute("st", searchType);
		model.addAttribute("sw", searchWord);
		model.addAttribute("ct", categoryType);
		model.addAttribute("type", type);
		
		
		return "/main/community/post_list";
	}
	
	

}
