package com.simpleaesthetics.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.simpleaesthetics.application.model.UserInformation;
import com.simpleaesthetics.application.rest.db.DatabaseHelper;

@Service
public class GrouperAuthUserService implements UserDetailsService {
	
	@Autowired
	DatabaseHelper dbHelper;
	
	public UserInformation loadUserByUsername(String username) throws UsernameNotFoundException {
        return dbHelper.getUserInformation(username);
    }

}
