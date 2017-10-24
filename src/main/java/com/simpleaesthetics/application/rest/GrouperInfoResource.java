 package com.simpleaesthetics.application.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simpleaesthetics.application.model.Environment;
import com.simpleaesthetics.application.model.Group;
import com.simpleaesthetics.application.rest.db.GrouperDB;
import com.simpleaesthetics.application.model.Course;
import com.simpleaesthetics.application.model.University;
import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.rest.db.DatabaseException;

@Controller
@RequestMapping
public class GrouperInfoResource {
	
	static Logger logger = Logger.getLogger(GrouperInfoResource.class.getName());
	
	@Autowired
	private Grouper grouper;
	
	@Autowired
	private GrouperDB db;
	
	private Environment env = new Environment(
			"TestEnv", true, "blastoise", new HashSet<Group>(), createTestUserSet());
	
	@RequestMapping(value="/universities", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<ArrayList<ArrayList<String>>> getUniversities() {
		
		HttpStatus status = HttpStatus.OK;
		ArrayList<ArrayList<String>> universities = db.queryAllUniversities();
		
		if (universities.size() == 0) {
			status = HttpStatus.NO_CONTENT;
			logger.info("No universities were returned");
		}
		
		return new ResponseEntity<ArrayList<ArrayList<String>>>(
				universities,
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<ArrayList<String>> getSpecificUniversity(
			@PathVariable(value="uniName",required=true) String uniName) {
		
		HttpStatus status = HttpStatus.OK;
		ArrayList<String> university = null;

		university = getUniversityInfo(uniName);

		return new ResponseEntity<ArrayList<String>>(
				university,
				status);
	}
	
	@RequestMapping(value="/universities", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addUniversity(
			@RequestBody(required=true) String name) {
		
		HttpStatus status = HttpStatus.NO_CONTENT;
		
		if (db.insertUniversity(name) == -1) {
			logger.warn("Could not insert [" + name +"]");
			status = HttpStatus.BAD_REQUEST;
			
		} else {
			logger.info("Successfully inserted ["+ name +"]");
		}
		
		return new ResponseEntity<String>(
				name, 
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<String>> getCourses(
			@PathVariable(value="uniName",required=true) String uniName) {
		
		// TODO add a query for classes
		// TODO get all the classes
		
		return null;
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<String>> getSpecificCourse(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName) {
		
		HttpStatus status = HttpStatus.OK;
		List<String> course = db.getCourses(db.getCourseID(courseName, db.getUniversityID(uniName)));
		
		if (course.size() == 0) {
			logger.info("A course was not returned");
			status = HttpStatus.NO_CONTENT;
		}
		
		return new ResponseEntity<List<String>>(
				course,
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addCourse(
			@PathVariable(value="uniName",required=true) String uniName,
			@RequestBody(required=true) Course course) {
		
		HttpStatus status = HttpStatus.OK;
		
		if (-1 == db.insertCourse(
					course.getName(), 
					Integer.parseInt(db.queryUser(course.getInstructor()).get(0)), 
					db.getUniversityID(uniName))) {
			
			logger.error("Could not add new course ["+ course +"]");
			status = HttpStatus.BAD_REQUEST;
			
		} else {
			logger.info("Successfully added new course ["+ course +"]");
		}
		
		return new ResponseEntity<String>(status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<Environment>> getEnvironments(
			@PathVariable("uniName") String uniName,
			@PathVariable("courseName") String courseName) {
		
		HttpStatus status = HttpStatus.OK;
		List<Environment> envList = null;
		
		return new ResponseEntity<List<Environment>>(
				envList,
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}/class/{className}/environments/{environmentName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<Environment> getSpecificEnvironment(
			@RequestHeader(value="sort", defaultValue="false", required=true) boolean toSort,
			@PathVariable("uniName") String uniName,
			@PathVariable("className") String className,
			@PathVariable("environmentName") String environmentName) {
		
		if (toSort) {
			env.setGroups(grouper.findAllGroups(env.getUsers(), 4));
		}
		
		return new ResponseEntity<Environment>(
				env,
				HttpStatus.OK);
	}
	
	@RequestMapping(value="/universities/{uniName}/class/{className}/environments", 
					method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Environment> addEnvironment(
			@PathVariable("uniName") String uniName,
			@PathVariable("className") String className,
			@PathVariable("environmentName") String environmentName) {
		
		return null;
	}
	
	private ArrayList<String> getUniversityInfo(String uniName) {
		System.out.println(uniName);
		
		int uniId = db.getUniversityID(uniName);
		ArrayList<String> university = null;
		
		logger.info(uniId);
		
		if (uniId != -1) {
			university = db.queryUniversity(uniId);
			
			if (university != null && university.size() == 0) {
				logger.info("A university was not returned");
				
			} else if (university == null) {
				throw new DatabaseException("Something went wrong when trying to find a university");
			}
			
		} else {
			logger.warn("Could not find university id for ["+ uniName + "]");
		}
		
		return university;
	}
	
	private Set<University> createTestUniList() {
		Set<University> uniList = new HashSet<>();
		uniList.add(new University("Carleton University"));
		return uniList;
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
