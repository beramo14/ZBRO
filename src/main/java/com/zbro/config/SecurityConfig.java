package com.zbro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zbro.main.service.login.ConsumerUserLoginService;
import com.zbro.main.service.login.SellerUserLoginService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private ConsumerUserLoginService consumerUserLoginService;
	
	@Autowired
	private SellerUserLoginService sellerUserLoginService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().antMatchers("/").permitAll()
                .antMatchers("/join/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .and()
                .formLogin(login -> login.loginPage("/login/consumer").defaultSuccessUrl("/"))
                .logout(logout -> logout.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID"));
	}

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(consumerUserLoginService).passwordEncoder(passwordEncoder());
        auth.userDetailsService(sellerUserLoginService).passwordEncoder(passwordEncoder());
    }
	

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
}

