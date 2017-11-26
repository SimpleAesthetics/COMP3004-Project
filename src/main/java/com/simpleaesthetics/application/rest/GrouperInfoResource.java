 package com.simpleaesthetics.application.rest;

import java.util.ArrayList;
import java.util.List;

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
import com.simpleaesthetics.application.model.Course;
import com.simpleaesthetics.application.model.University;
import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.rest.db.DatabaseHelper;

@Controller
@RequestMapping
public class GrouperInfoResource {
	
	static Logger logger = Logger.getLogger(GrouperInfoResource.class.getName());
	
	@Autowired
	private Grouper grouper;
	
	@Autowired
	private DatabaseHelper dbHelper;
	
	Boolean createdTestData = false;
	
	@RequestMapping(value="/createTestData", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> createTestData() {
		if (!createdTestData && dbHelper.createTestData()) {
			createdTestData = true;
			return new ResponseEntity<String>("Test data SUCCESSFULLY created", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("Test data FAILED to create", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value="/universities", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<University>> getUniversities() {
		
		HttpStatus status = HttpStatus.OK;
		List<University> universities = dbHelper.getUniversities();
		
		if (universities.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
			logger.info("No universities were returned");
		}
		
		return new ResponseEntity<List<University>>(
				universities,
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<University> getSpecificUniversity(
			@PathVariable(value="uniName",required=true) String uniName) {
		
		University university = dbHelper.getSpecificUniversity(uniName);
		return new ResponseEntity<University>(
				university,
				HttpStatus.OK);
	}
	
	@RequestMapping(value="/universities", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addUniversity(
			@RequestBody(required=true) University university) {
		
		dbHelper.addUniversity(university.getName());
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<Course>> getCourses(
			@PathVariable(value="uniName",required=true) String uniName) {
		
		HttpStatus status = HttpStatus.OK;
		List<Course> courses = dbHelper.getCourses(uniName);
		
		if (courses.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
			logger.info("No courses were returned for ["+ uniName +"]");
		}
		
		return new ResponseEntity<List<Course>>(
				courses,
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<Course> getSpecificCourse(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName) {
		
		Course course = dbHelper.getSpecificCourse(courseName, uniName);
		return new ResponseEntity<Course>(
				course,
				HttpStatus.OK);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addCourse(
			@PathVariable(value="uniName",required=true) String uniName,
			@RequestBody(required=true) Course course) {
		
		HttpStatus status = HttpStatus.OK;
		boolean isCourseInserted = dbHelper.addCourse(course.getName(), uniName);
		
		if (!isCourseInserted) {
			logger.error("Could not add new course ["+ course.getName() +"]");
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<String>(status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<Environment>> getEnvironments(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName) {
		
		HttpStatus status = HttpStatus.OK;
//		ArrayList<ArrayList<String>> envList = dbHelper.getEnvironments(uniName, courseName);
		List<Environment> envList = dbHelper.getEnvironments(uniName, courseName);
		
		if (envList.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return new ResponseEntity<List<Environment>>(
				envList,
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments/{envName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<Environment> getSpecificEnvironment(
			@RequestHeader(value="sortGroups", defaultValue="false") boolean toSort,
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@PathVariable(value="envName",required=true) String envName) {
		
		Environment environment = dbHelper.getSpecificEnvironment(envName, courseName, uniName);

		if (toSort) {
			System.out.println("Running sorting");
			environment.setGroups(grouper.findAllGroups(environment.getUsers(), 4));
			
		}
		
		return new ResponseEntity<Environment>(
				environment,
				HttpStatus.OK);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addEnvironment(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@RequestBody(required=true) Environment env) {
		
		dbHelper.addEnvironment(env, courseName, uniName);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments/{envName}/groups", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addSpecificGroupToEnv(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@PathVariable(value="envName",required=true) String envName,
			@RequestBody(required=true) Group group) {
		
		dbHelper.addSpecificGroupToEnv(group, envName, courseName, uniName);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments/{envName}/users", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addSpecificUserToEnv(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@PathVariable(value="envName",required=true) String envName,
			@RequestBody(required=true) User user) {
		
		dbHelper.addSpecificUserToEnv(user, envName, courseName, uniName);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	
} // End of class
