package com.simpleaesthetics.application.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simpleaesthetics.application.model.UserInformation;

public class UserInfoResource {

	@RequestMapping(value="/users", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<UserInformation>> getSpecificUsers(
			@RequestParam(value="firstName", required=false) String userNameQuery) {
		
		System.out.println("User Name Query: " + userNameQuery);
		
		UserInformation userInfo = new UserInformation("TestFirst", "TestLast", "TestNickname", "111-111-1111");
		List<UserInformation> userInfoList = new ArrayList<UserInformation>();
		userInfoList.add(userInfo);
		
		return new ResponseEntity<List<UserInformation>>(
				userInfoList, 
				HttpStatus.OK);
	}
	
	@RequestMapping(value="/users/{userName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<UserInformation> getUsers(
			@PathVariable("userName") String userName) {
		
		if ("TestName".equals(userName)) {
			return new ResponseEntity<UserInformation>(
					new UserInformation(userName, "TestLast", "TestNickname", "111-111-11111"), 
					HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
}
