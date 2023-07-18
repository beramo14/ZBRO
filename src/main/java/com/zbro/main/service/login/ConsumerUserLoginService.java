package com.zbro.main.service.login;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.ConsumerUserRepository;
import com.zbro.model.ConsumerUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //생성자 주입(final필드 의존성 주입)
public class ConsumerUserLoginService implements UserDetailsService{
	
	private final ConsumerUserRepository consumerRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		ConsumerUser user = consumerRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("ConsumerUser not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(ConsumerUser user) {
        // 구매자의 권한 정보를 가져와서 Spring Security의 GrantedAuthority 객체로 변환하는 로직을 작성합니다.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CONSUMER"));
    }
	
}
