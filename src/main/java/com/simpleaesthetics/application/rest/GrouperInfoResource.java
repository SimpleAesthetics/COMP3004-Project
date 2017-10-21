 package com.simpleaesthetics.application.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simpleaesthetics.application.model.Environment;
import com.simpleaesthetics.application.model.Group;
import com.simpleaesthetics.application.model.Course;
import com.simpleaesthetics.application.model.University;
import com.simpleaesthetics.application.model.User;

@Controller
@RequestMapping
public class GrouperInfoResource {
	
	@Autowired
	private Grouper grouper;
	
	private Environment env = new Environment(
			"TestEnv", true, "blastoise", new HashSet<Group>(), createTestUserSet());
	
	private Set<University> universities = createTestUniList();
	
	@RequestMapping(value="/universities", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<University>> queryUniversity(
			@RequestParam(value="name",required=false) String uniName) {
		
		List<University> uniList = new ArrayList<>();
		University toSearch = new University(uniName);
		
		if (uniName!=null && universities.contains(toSearch)) {
			// TODO Add searching here
			//envList.add(universities.iterator()(toSearch));
		
		} else {
			uniList.addAll(this.universities);
		}
		
		return new ResponseEntity<List<University>>(
				uniList, 
				HttpStatus.FOUND);
	}
	
	@RequestMapping(value="/universities/{uniName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<University> getSpecificUniversity(
			@PathVariable(value="uniName",required=true) String uniName) {
		
		if ("Carleton University".equals(uniName)) {
			return new ResponseEntity<University>(
					new University("Carleton University"),
					HttpStatus.OK);
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/universities/", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addUniversity(
			@RequestBody(required=true) String uniName) {
		
		universities.add(new University(uniName));
		return new ResponseEntity<String>(
				"Successfully added: " + uniName, 
				HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="/universities/{uniName}/classes", method=RequestMethod.GET)
	public @ResponseBody List<Course> getClasses(
			@PathVariable(value="uniName",required=true) String uniName) {
		
		List<University> universitiesList = new ArrayList<>();
		universitiesList.add(new University("Carleton University"));
		
		return null;
	}
	
	@RequestMapping(value="/universities/{uniName}/classes/{className}", method=RequestMethod.GET)
	public @ResponseBody Course getSpecificClass(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable("className") String className) {
		
		return null;
	}
	
	@RequestMapping(value="/universities/{uniName}/courseCode", method=RequestMethod.POST)
	public @ResponseBody Course querySpecificClass(
			@PathVariable(value="uniName",required=true) String uniName,
			@RequestParam(value="courseCode",required=false) String courseCode) {
		
		return null;
	}
	
	@RequestMapping(value="/universities/{uniName}/class/{className}/environments", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<Environment>> getEnvironments(
			@RequestParam(value="name",required=false) String envName,
			@PathVariable("uniName") String uniName,
			@PathVariable("className") String className) {
		
		List<Environment> envList = new ArrayList<>();
		HttpStatus status = null;
		
		if (envName!=null) {
			status = HttpStatus.FOUND;
			envList.add(new Environment(
					envName, false, "squirtle", new HashSet<Group>(), createTestUserSet()));
			
		} else {
			status = HttpStatus.OK;
			envList.add(new Environment(
					"TestEnv", true, "blastoise", new HashSet<Group>(), createTestUserSet()));
		}
		
		return new ResponseEntity<List<Environment>>(
				envList,
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}/class/{className}/environments/{environmentName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<Environment> getSpecificEnvironment(
			@RequestHeader(value="sort", defaultValue="false") boolean toSort,
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
	
	@RequestMapping(value="/universities/{uniName}/class/{className}/environments/{environmentName}", 
					method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Environment> querySpecificEnvironment(
			@PathVariable("uniName") String uniName,
			@PathVariable("className") String className,
			@PathVariable("environmentName") String environmentName) {
		
		return null;
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
