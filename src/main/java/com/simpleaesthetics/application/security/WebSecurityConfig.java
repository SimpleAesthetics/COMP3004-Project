package com.simpleaesthetics.application.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.simpleaesthetics.application.rest.timer.Deadline;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
	static Logger logger = Logger.getLogger(Deadline.class.getName());
	
	@Autowired
	private GrouperAuthProvider authProvider;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("Configuring Web Security");
        http.authorizeRequests()
        		.anyRequest().authenticated()
	    			.and().httpBasic()
	    			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    			.and().csrf().disable();
    }
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("POST", "/usersInfo");
	}

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(authProvider);
    }
}
