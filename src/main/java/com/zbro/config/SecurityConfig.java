package com.zbro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zbro.main.service.login.ConsumerUserLoginService;
import com.zbro.main.service.login.SellerUserLoginService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final PasswordEncoder passwordEncoder;
	private final ConsumerUserLoginService consumerUserLoginService;
    private final SellerUserLoginService sellerUserLoginService;

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.disable())
    		.csrf(csrf -> csrf.disable())
    		.authorizeHttpRequests().antMatchers("/").permitAll()
            .antMatchers("/join/**").permitAll()
            .antMatchers("/login/**").permitAll()
            .and()
            .formLogin(login -> login.loginPage("/login/consumer").defaultSuccessUrl("/"))
            .logout(logout -> logout.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID"));
	}

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(consumerUserLoginService).passwordEncoder(passwordEncoder);
        auth.userDetailsService(sellerUserLoginService).passwordEncoder(passwordEncoder);
    }
	
	
}

