package com.simpleaesthetics.application.rest;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simpleaesthetics.application.model.UserInformation;
import com.simpleaesthetics.application.rest.db.DatabaseException;
import com.simpleaesthetics.application.rest.db.GrouperDB;

@Controller
@RequestMapping
public class UserInfoResource {

	static Logger logger = Logger.getLogger(GrouperInfoResource.class.getName());
	
	@Autowired
	GrouperDB db;
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<ArrayList<ArrayList<String>>> getUsers(
			@RequestParam(value="firstName", required=false) String userNameQuery) {
		
		ArrayList<ArrayList<String>> users = db.queryAllUsers();
		
		return new ResponseEntity<ArrayList<ArrayList<String>>>(
				users, 
				HttpStatus.OK);
	}
	
	@RequestMapping(value="/users/{userNickname}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<ArrayList<String>> getSpecificUser(
			@PathVariable("userNickname") String userNickname) {
		
		ArrayList<String> user = db.queryUser(userNickname);
		return new ResponseEntity<ArrayList<String>>(user,HttpStatus.OK);
	}
	
	@RequestMapping(value="/users", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<UserInformation> getUsers(
			@RequestBody(required=true) UserInformation userInfo) {
		
		int userId = db.insertUser(
				userInfo.getStudentNumber(), 
				userInfo.getFirstName(), 
				userInfo.getLastName(), 
				userInfo.getEmail());
		
		if (userId == -1) {
			logger.error("Failed to insert user ["+ userInfo.toString() +"]");
			throw new DatabaseException("Failed to insert user ["+ userInfo.toString() +"]");
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
}
