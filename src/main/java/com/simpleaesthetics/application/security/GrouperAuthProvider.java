package com.simpleaesthetics.application.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.UserInformation;

@Component
public class GrouperAuthProvider implements AuthenticationProvider {
	
	private final String ERROR_STRING = "Incorrect user name or password"; 
	
	@Autowired
    private GrouperAuthUserService userService;
    
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	
    	String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        
        UserInformation user = userService.loadUserByUsername(username);;
    	
        if (user == null || !user.getUsername().equalsIgnoreCase(username)) {
            throw new BadCredentialsException(ERROR_STRING);
        }
 
        if (!password.equals("password")) {
            throw new BadCredentialsException(ERROR_STRING);
        }
        
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
 
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }
 
    public boolean supports(Class<?> arg0) {
        return true;
    }
	
}
