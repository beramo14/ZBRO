package com.zbro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zbro.main.service.login.ConsumerUserLoginService;
import com.zbro.main.service.login.SellerUserLoginService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Order(2)
	@Configuration
	@RequiredArgsConstructor
	public class ConsumerSecurityConfig extends WebSecurityConfigurerAdapter {
		
		private final PasswordEncoder passwordEncoder;
		private final ConsumerUserLoginService consumerUserLoginService;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
	        http.cors(cors -> cors.disable())
	    		.csrf(csrf -> csrf.disable())
	    		.antMatcher("/**")
	    		.authorizeHttpRequests( requestMatchers -> 
	    			requestMatchers
	    				.antMatchers("/join/**").permitAll()
	    				.antMatchers("/consumer/login").permitAll()
				)
	            .formLogin(login -> login.loginPage("/consumer/login").loginProcessingUrl("/consumer/login").defaultSuccessUrl("/"))
	            .logout(logout -> logout.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID"));
		}

		@Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(consumerUserLoginService).passwordEncoder(passwordEncoder);
	    }
	}
	
	@Order(1)
	@Configuration
	@RequiredArgsConstructor
	public class SellerSecurityConfig extends WebSecurityConfigurerAdapter {
		
		private final PasswordEncoder passwordEncoder;
	    private final SellerUserLoginService sellerUserLoginService;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
	        http.cors(cors -> cors.disable())
	    		.csrf(csrf -> csrf.disable())
	    		.antMatcher("/seller/**")
	    		.authorizeHttpRequests( requestMatchers -> 
		    		requestMatchers
						.antMatchers("/seller/login").permitAll()
						.antMatchers("/seller/**").hasAuthority("ROLE_SELLER")
				)
	    		.formLogin(login -> login.loginPage("/seller/login").loginProcessingUrl("/seller/login").defaultSuccessUrl("/seller"));
		}

		@Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(sellerUserLoginService).passwordEncoder(passwordEncoder);
	    }
	
		
	}

}
