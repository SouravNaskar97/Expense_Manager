 package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig{
	
	@Bean
	public UserDetailsService getUserDetailService() {
		
		
		return new UserDetailsServiceImpl();
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		
		
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
		
	}
	
	
	
	@Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	   
	    
	    http.authenticationProvider(authenticationProvider());

	    
	 //   http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN")
	 //    .requestMatchers("/user/**").hasRole("USER")
	 //   http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN")
	  //  .requestMatchers("/user/**").hasRole("USER")
	 //   .requestMatchers("/**").permitAll()
	 //   .and().formLogin().and().csrf(AbstractHttpConfigurer::disable);
	    
	    http.authorizeHttpRequests(auth-> auth.requestMatchers("/admin/**").hasRole("ADMIN")
	    	    .requestMatchers("/user/**").hasRole("USER")
	    	    .requestMatchers("/**").permitAll())
	    .formLogin(formLogin -> formLogin.loginPage("/signin")
	    		.loginProcessingUrl("/dologin")
	    		.defaultSuccessUrl("/user/index")
	    		.permitAll()).csrf(AbstractHttpConfigurer::disable);
	     
	     
		
	   
	    return http.build();
	}
	
	
//	@Bean
//	  public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
//	     http.authorizeRequests().requestMatchers("/admin/**").hasRole("ADMIN")
//	     .requestMatchers("/user/**").hasRole("USER")
//	     .requestMatchers("/**").permitAll().and().formLogin().and().csrf().disable();
//		
//		return http.build();
//		
//	}
	
	

}
