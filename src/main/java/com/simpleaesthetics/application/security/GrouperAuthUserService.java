package com.simpleaesthetics.application.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.simpleaesthetics.application.model.UserInformation;
import com.simpleaesthetics.application.rest.GrouperInfoResource;
import com.simpleaesthetics.application.rest.db.DatabaseHelper;

@Service
public class GrouperAuthUserService implements UserDetailsService {
	
	static Logger logger = Logger.getLogger(GrouperAuthUserService.class.getName());
	
	@Autowired
	DatabaseHelper dbHelper;
	
	public UserInformation loadUserByUsername(String username) throws UsernameNotFoundException {
        
		try {
			return dbHelper.getUserInformation(username);
			
		} catch (Exception e) {
			throw new UsernameNotFoundException("Failed to authorize username or password");
		}
    }

}
