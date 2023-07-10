package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.ConsumerUser;

public interface ConsumerUserRepository extends JpaRepository<ConsumerUser, Long> {
	
	ConsumerUser findByConsumerId(Long consumerId);
	
	ConsumerUser save(ConsumerUser consumerUser);
	
}
