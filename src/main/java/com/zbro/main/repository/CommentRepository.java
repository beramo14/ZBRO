package com.zbro.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Comment;
import com.zbro.model.Community;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	List<Comment> findAllByPost(Community post);

	void deleteAllByPost(Community thisPost);

}
