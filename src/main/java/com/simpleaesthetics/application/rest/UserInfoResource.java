package com.simpleaesthetics.application.rest;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.BadPayloadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.model.UserInformation;
import com.simpleaesthetics.application.rest.db.DatabaseException;
import com.simpleaesthetics.application.rest.db.DatabaseHelper;
import com.simpleaesthetics.application.rest.db.GrouperDB;

@Controller
@RequestMapping
public class UserInfoResource {

	static Logger logger = Logger.getLogger(GrouperInfoResource.class.getName());
	
	@Autowired
	private GrouperDB db;
	
	@Autowired
	private DatabaseHelper dbHelper;
	
//	@RequestMapping(value="/users", method=RequestMethod.GET)
//	public @ResponseBody ResponseEntity<ArrayList<User>> getUsers(
//			@RequestParam(value="firstName", required=false) String userNameQuery) {
//		
//		ArrayList<User> users = dbHelper.getUsers();
//		return new ResponseEntity<ArrayList<User>>(
//				users, 
//				HttpStatus.OK);
//	}
	
	@RequestMapping(value="/usersInfo/{username}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<UserInformation> getUserInformation(
				Principal principal,
				@PathVariable("username") String username) {
		
		this.verifyUserIntegrity(principal, username);
		
		UserInformation user = dbHelper.getUserInformation(username);
		return new ResponseEntity<UserInformation>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value="/usersInfo", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<UserInformation> getUsers(
			@RequestBody(required=true) UserInformation userInfo) {
		
		this.verifyUserDetails(userInfo);
		
		int userId = db.insertUser(
				userInfo.getStudentNumber(), 
				userInfo.getFirstName(), 
				userInfo.getLastName(),
				userInfo.getUsername(),
				userInfo.getEmail().replaceFirst("\\[at\\]", "@"),
				userInfo.getPassword());
		
		if (userId == -1) {
			logger.error("Failed to insert user ["+ userInfo.toString() +"]");
			throw new DatabaseException("Failed to insert user ["+ userInfo.toString() +"]");
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/users/{username}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<User> getSpecificUser(
				Principal principal,
				@PathVariable("username") String username) {
		
		this.verifyUserIntegrity(principal, username);
		
		User user = dbHelper.getUser(username);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	
	/** Helper Functions **/
	
	private void verifyUserDetails(UserInformation userInfo) {
		if (userInfo.getStudentNumber() == -1) {
			throw new BadCredentialsException("Student number is not set");
			
		} else if (userInfo.getFirstName() == null) {
			throw new BadCredentialsException("Student first name is not set");
			
		} else if (userInfo.getLastName() == null) {
			throw new BadCredentialsException("Student last name is not set");
			
		} else if (userInfo.getUsername() == null) {
			throw new BadCredentialsException("Student username is not set");
			
		} else if (userInfo.getEmail() == null) {
			throw new BadCredentialsException("Student email is not set");
			
		}  else if (userInfo.getPassword() == null) {
			throw new BadCredentialsException("Student password is not set");
			
		}
	}
	
	private void verifyUserIntegrity(Principal principal, String requestedUsername) {
		if (!requestedUsername.equals(principal.getName())) {
			throw new BadCredentialsException("User is attempting to access another users information");
		}
	}
	
}
