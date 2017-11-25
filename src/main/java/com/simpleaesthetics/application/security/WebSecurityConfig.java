package com.simpleaesthetics.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
	private GrouperAuthProvider authProvider;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("CONFIGURING SECURITY");
        http/*.authorizeRequests().antMatchers("POST", "/users").anonymous()
        		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    			.and().csrf().disable()*/
        	.authorizeRequests()
        		.anyRequest().authenticated()
	    			.and().httpBasic()
	    			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    			.and().csrf().disable();
    }
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("POST", "/users");
	}

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(authProvider);
    }
}
