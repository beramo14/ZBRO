package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findByUserConsumerId(Long consumerId);

}
