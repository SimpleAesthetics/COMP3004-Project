 package com.simpleaesthetics.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simpleaesthetics.application.model.Environment;
import com.simpleaesthetics.application.model.UniClass;
import com.simpleaesthetics.application.model.University;
import com.simpleaesthetics.application.model.UserInformation;

@Controller
@RequestMapping
public class GrouperInformationResource {
	
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
	
	@RequestMapping(value="/universities", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<University>> getUniversities() {
		List<University> universitiesList = new ArrayList<>();
		universitiesList.add(new University("Carleton University"));
		
		return new ResponseEntity<List<University>>(
				universitiesList, 
				HttpStatus.OK);
	}
	
	@RequestMapping(value="/universities/{uniName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<University> getSpecificUniversity(@PathVariable("uniName") String uniName) {
		
		if ("Carleton University".equals(uniName)) {
			return new ResponseEntity<University>(
					new University("Carleton University"),
					HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/universities/{uniName}/classes", method=RequestMethod.GET)
	public @ResponseBody List<UniClass> getClasses(@PathVariable("uniName") String uniName) {
		
		List<University> universitiesList = new ArrayList<>();
		universitiesList.add(new University("Carleton University"));
		
		return null;
	}
	
	@RequestMapping(value="/universities/{uniName}/classes/{className}", method=RequestMethod.GET)
	public @ResponseBody UniClass getSpecificClass(
			@PathVariable("uniName") String uniName,
			@PathVariable("className") String className) {
		
		return null;
	}
	
	@RequestMapping(value="/universities/{uniName}/class/{className}/environments", method=RequestMethod.GET)
	public @ResponseBody List<Environment> getEnvironments(
			@PathVariable("uniName") String uniName,
			@PathVariable("className") String className) {
		
		return null;
	}
	
	@RequestMapping(value="/universities/{uniName}/class/{className}/environments/{environmentName}", method=RequestMethod.GET)
	public @ResponseBody Environment getSpecificEnvironment(
			@PathVariable("uniName") String uniName,
			@PathVariable("className") String className,
			@PathVariable("environmentName") String environmentName) {
		
		return null;
	}
	
}
