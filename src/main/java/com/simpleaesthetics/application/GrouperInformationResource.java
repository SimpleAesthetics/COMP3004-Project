 package com.simpleaesthetics.application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jetty.http.HttpHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simpleaesthetics.application.model.Environment;
import com.simpleaesthetics.application.model.Group;
import com.simpleaesthetics.application.model.UniClass;
import com.simpleaesthetics.application.model.University;
import com.simpleaesthetics.application.model.User;
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
	public @ResponseBody ResponseEntity<Environment> getSpecificEnvironment(
			@RequestHeader(value="sort", defaultValue="false") boolean toSort,
			@PathVariable("uniName") String uniName,
			@PathVariable("className") String className,
			@PathVariable("environmentName") String environmentName) {
		
		Environment env = new Environment("TestEnv", true, "blastoise", new ArrayList<Group>());
		
		if (toSort) {
			Set<User> users = createTestUserSet();
			User currUser = users.iterator().next();
			users.remove(currUser);
			
			env.getGroups().add(
					new Group(findSingleMatch(currUser, users, 4)));
			
		}
		
		return new ResponseEntity<Environment>(
				env,
				HttpStatus.OK);
	}
	
	private Set<User> findSingleMatch(User toMatch, Set<User> users, Integer maxMatches) {
		Map<Integer, Queue<User>> sortingMap = new HashMap<>();
		Set<User> matches = new HashSet<>();
		Iterator<User> userIter = users.iterator();
		List<Integer> currAns = toMatch.getQuestionAnswers();
		matches.add(toMatch);
		
		System.out.println(toMatch.toString());
		
		while (userIter.hasNext()) {
			User currUser = userIter.next();
			Integer score = calculateSimilarityOfAns(
					currAns,
					currUser.getQuestionAnswers());
			
			if (!sortingMap.containsKey(score)) {
				sortingMap.put(score, createQueueAndAddUser(currUser));
			} else {
				sortingMap.get(score).add(currUser);
			}
			
			System.out.println("\n--------------");
			System.out.println(sortingMap.toString());
			
		}
		
		Integer i = currAns.size();
		while (i >= 0) {
			if (sortingMap.containsKey(i)) {
				Queue<User> currMatches = sortingMap.get(i);
				while (currMatches.size() != 0 && matches.size() != maxMatches) {
					matches.add(currMatches.poll());
				}
				
				if (matches.size() == maxMatches) {
					break;
				}
			}
			
			i--;
		}
		
		return matches;
	}
	
	private Integer calculateSimilarityOfAns(List<Integer> ansUser, List<Integer> ansOtherUser) {
		
		Integer score = 0;
		
		for (int j = 0; j < ansUser.size(); ++j) {
			if (ansUser.get(j) == ansOtherUser.get(j)) {
				score++;
			} 
		}
		
		return score;
	}
	
	private Queue<User> createQueueAndAddUser(User user) {
		Queue<User> tempQueue = new ArrayDeque<>();
		tempQueue.add(user);
		return tempQueue;
	}
	
	
	private Set<User> createTestUserSet() {
		Set<User> users = new HashSet<>();
		users.add(createUser("Jeb", 1, 2, 3, 4));
		users.add(createUser("Simba", 4, 3, 2, 1));
		users.add(createUser("Java", 1, 2, 4, 3));
		users.add(createUser("Caltrone", 2, 1, 4, 3));
		users.add(createUser("Father", 1, 2, 3, 4));
		users.add(createUser("Mother", 3, 2, 4, 4));
		
		return users;
	}
	
	private User createUser(String userNickname, Integer...answers) {
		return new User(userNickname, createAnsList(answers));
	}
	
	private List<Integer> createAnsList(Integer[] answers) {
		List<Integer> ansList = new ArrayList<>();
		
		for (Integer answer : answers) {
			ansList.add(answer);
		}
		
		return ansList;
	}
	
}
