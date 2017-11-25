package com.simpleaesthetics.application.security;

import org.springframework.security.core.GrantedAuthority;
 
public class Role implements GrantedAuthority{
    
	private static final long serialVersionUID = 5530285550569998115L;
	
	private String name;
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getAuthority() {
        return this.name;
    }
}
