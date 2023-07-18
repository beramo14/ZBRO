package com.zbro.main.service.login;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.ConsumerUserRepository;
import com.zbro.main.repository.SellerUserRepository;
import com.zbro.model.SellerUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //생성자 주입(final필드 의존성 주입)
public class SellerUserLoginService implements UserDetailsService {

	private final SellerUserRepository sellerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		SellerUser seller = sellerRepository.findByEmail(email).orElseThrow( () -> new UsernameNotFoundException("Seller not found with email: " + email) );
		
        return new org.springframework.security.core.userdetails.User(seller.getEmail(), seller.getPassword(), getAuthorities(seller));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(SellerUser user) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_SELLER"));
    }

}
