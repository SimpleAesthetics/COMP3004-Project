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
	
//	private Environment env = new Environment(
//			"TestEnv", true, "blastoise", new HashSet<Group>(), createTestUserSet());
	
	@RequestMapping(value="/universities", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<ArrayList<ArrayList<String>>> getUniversities() {
		
		HttpStatus status = HttpStatus.OK;
		ArrayList<ArrayList<String>> universities = db.queryAllUniversities();
		
		if (universities.isEmpty()) {
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
		ArrayList<String> universityInfo = getUniversityInfo(uniName);
		
		if (universityInfo.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}

		return new ResponseEntity<ArrayList<String>>(
				universityInfo,
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
		
		// TODO get all the classes
		// TODO implement a get all courses function
		
		return null;
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<String>> getSpecificCourse(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName) {
		
		HttpStatus status = HttpStatus.OK;
		List<String> course = getCourseInfo(courseName, uniName);
		
		if (course.isEmpty()) {
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
		
		logger.info(course.getInstructor());
		
		HttpStatus status = HttpStatus.OK;
		int insertedCourse = db.insertCourse(
								course.getName(), 
								Integer.parseInt(db.queryUser(course.getInstructor()).get(0)), 
								getUniversityId(uniName));
		
		if (-1 == insertedCourse) {
			logger.error("Could not add new course ["+ course +"]");
			status = HttpStatus.BAD_REQUEST;
			
		}
		
		return new ResponseEntity<String>(status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<ArrayList<ArrayList<String>>> getEnvironments(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName) {
		
		HttpStatus status = HttpStatus.OK;
		ArrayList<ArrayList<String>> envList = getEnvironmentsHelper(uniName, courseName);
		
		if (envList.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return new ResponseEntity<ArrayList<ArrayList<String>>>(
				envList,
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}/course/{courseName}/environments/{environmentName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<ArrayList<String>> getSpecificEnvironment(
			@RequestHeader(value="sort", defaultValue="false") boolean toSort,
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@PathVariable(value="environmentName",required=true) String environmentName) {
		
		ArrayList<String> envInfo = getEnvironmentInfoHelper(environmentName, courseName, uniName);
		System.out.println(envInfo);
		
//		if (toSort) {
//			env.setGroups(grouper.findAllGroups(env.getUsers(), 4));
//			
//		} else {
//			
//		}
		
		return new ResponseEntity<ArrayList<String>>(
				envInfo,
				HttpStatus.OK);
	}
	
	@RequestMapping(value="/universities/{uniName}/class/{courseName}/environments", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Environment> addEnvironment(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@RequestBody(required=true) Environment env) {
		
//		int[] userIds = 
//		
//		db.insertEnvironment(
//				env.getName(), 
//				getCourseId(courseName,uniName), 
//				env.isPrivateEnv(), 
//				env.getPassword(),
//				env.getMaxGroupSize().intValue(), 
//				env.getDeadline().toString(), 
//				getUserIdsFromNicknames(env.getUsers()), 
//				env.getGroups());
		
		return null;
	}
	
	
	/*
	 *  THE HELPER FUNTIONS
	 */
	
	private int getUniversityId(String universityName) {
		int universityId = db.getUniversityID(universityName);
		
		if (universityId == -1) {
			logger.warn("Failed to get university id for ["+ universityName +"]");
			throw new DatabaseException("Failed to get university id for ["+ universityName +"]");
		}
		
		return universityId;
	}
	
	private ArrayList<String> getUniversityInfo(String universityName) {
		ArrayList<String> universityInfo = db.queryUniversity(getUniversityId(universityName));
		
		if (universityInfo.size() == 0) {
			logger.warn("No university was returned for ["+ universityName +"]");
		}
		
		return universityInfo;
	}
	
	private int getCourseId(String courseName, String universityName) {
		int courseId = db.getCourseID(courseName, getUniversityId(universityName));
		
		if (courseId == -1) {
			logger.warn("Failed to get course id for ["+ courseName +"]");
			throw new DatabaseException("Failed to get course id for ["+ courseName +"]");
		}
		
		return courseId;
	}
	
	private ArrayList<String> getCourseInfo(String courseName, String unviersityName) {
		ArrayList<String> courseInfo = db.queryCourse(getCourseId(courseName, unviersityName));
		
		if (courseInfo.size() == 0) {
			logger.warn("No courses were returned for ["+ courseName +"]");
		}
		
		return courseInfo;
	}
	
	private ArrayList<ArrayList<String>> getEnvironmentsHelper(String universityName, String courseName) {
		ArrayList<ArrayList<String>> envs = db.getEnvironments(getCourseId(courseName, universityName));
		
		if (envs.size() == 0) {
			logger.warn("No environments were returned for ["+ courseName +"]");
		}
		
		return envs;
	}
	
	private int getEnvironmentId(
			String environmentName, 
			String courseName, 
			String universityName) {
		
		int envId = db.getEnvironmentID(environmentName, getCourseId(courseName, universityName));
		
		if (envId == -1) {
			logger.warn("Failed to get environment id for ["+ environmentName +"]");
			throw new DatabaseException("Failed to get environment id for ["+ environmentName +"]");
		}
		
		return envId;
	}
	
	private ArrayList<String> getEnvironmentInfoHelper( 
			String environmentName,
			String courseName,
			String universityName) {
		
		int envId = getEnvironmentId(environmentName, courseName, universityName);
		ArrayList<String> envInfo = db.queryEnvironment(envId);
		
		if (envInfo.isEmpty()) {
			logger.warn("No environments were returned for ["+ environmentName +"]");
		}
		
		return envInfo;
	}
	
	private Integer[] getUserIdsFromNicknames(Set<User> users) {
		ArrayList<Integer> idList = new ArrayList<>();
		for (User user : users) {
			idList.add(Integer.valueOf(
						db.queryUser(user.getNickname()).get(0)));
		}
		
		return idList.toArray(new Integer[idList.size()]);
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
