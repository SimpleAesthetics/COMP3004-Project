 package com.simpleaesthetics.application.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.simpleaesthetics.application.rest.transformer.EnvironmentTransformer;
import com.simpleaesthetics.application.rest.transformer.QuestionnaireTransformer;
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
	
	@Autowired
	private EnvironmentTransformer envTransformer;
	
	@Autowired
	private QuestionnaireTransformer questionnaireTransformer;
	
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
			@RequestBody(required=true) University university) {
		
		HttpStatus status = HttpStatus.NO_CONTENT;
		String name = university.getName();
		
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
	public @ResponseBody ResponseEntity<ArrayList<ArrayList<String>>> getCourses(
			@PathVariable(value="uniName",required=true) String uniName) {
		
		HttpStatus status = HttpStatus.OK;
		ArrayList<ArrayList<String>> courses = db.getCourses(getUniversityId(uniName));
		
		if (courses.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
			logger.info("No courses were returned for ["+ uniName +"]");
		}
		
		return new ResponseEntity<ArrayList<ArrayList<String>>>(
				courses,
				status);
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
//								Integer.parseInt(db.queryUser(course.getInstructor()).get(0)),
								-1,
								getUniversityId(uniName));
		
		if (-1 == insertedCourse) {
			logger.error("Could not add new course ["+ course.getName() +"]");
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
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments/{envName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<Environment> getSpecificEnvironment(
			@RequestHeader(value="sortGroups", defaultValue="false") boolean toSort,
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@PathVariable(value="envName",required=true) String envName) {
		
		Environment env = envTransformer.transform(
				getEnvironmentInfoHelper(envName, courseName, uniName),
				getQuestionnaireHelper(envName, courseName, uniName));
		
		if (toSort) {
			env.setGroups(grouper.findAllGroups(env.getUsers(), 4));
			
		} else {
			
		}
		
		return new ResponseEntity<Environment>(
				env,
				HttpStatus.OK);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addEnvironment(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@RequestBody(required=true) Environment env) {
		
		HttpStatus status = HttpStatus.OK;
		String errorStr = "";
		
		int insertedEnv = db.insertEnvironment(
				env.getName(), 
				getCourseId(courseName,uniName), 
				env.isPrivateEnv(), 
				env.getPassword(),
				env.getMaxGroupSize().intValue(), 
				env.getDeadline(), 
				new int[0], 
				new int[0],
				-1);
		
		if (insertedEnv == -1) {
			logger.error("Could not add new environment ["+ env.getName() +"]");
			status = HttpStatus.BAD_REQUEST;
			errorStr += "Inserting environment failed";
			
		} else {
			int envId = getEnvironmentId(env.getName(), courseName, uniName);
			int insertedQuestionnnaire = db.insertQuestionnaire(
					envId, 
					questionnaireTransformer.transformForDb(env.getQuestionnaire()));
			
			System.out.println(questionnaireTransformer.transformForDb(env.getQuestionnaire()).toString());
			
			if (insertedQuestionnnaire == -1) {
				logger.error("Could not add new questionnaire to env ["+ env.getName() +"]");
				status = HttpStatus.BAD_REQUEST;
				errorStr += "Inserting questionnaire failed";
				
			} else {
				boolean successfulUpdate = db.changeQuestionnaire(
						envId, 
						insertedQuestionnnaire);
				
				if (!successfulUpdate) {
					logger.error("Could not update questionnaire id for env ["+ env.getName() +"]");
					status = HttpStatus.BAD_REQUEST;
					errorStr += "Updating questionnaire ID to environment failed";
				}
			}
		}
		
		return new ResponseEntity<String>(errorStr, status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments/{envName}/groups", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addGroup(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@PathVariable(value="envName",required=true) String envName,
			@RequestBody(required=true) Group group) {
		
		HttpStatus status = HttpStatus.OK;
		int envId = getEnvironmentId(envName, courseName, uniName);
		
		int insertedGroup = db.insertGroup(
				envId, 
				0, 
				Integer.valueOf(db.queryUser(group.getTaName()).get(0)),
				getCsvFromUserSet(group.getGroupMembers()));
		
		if (insertedGroup == -1) {
			logger.error("Could not add new group ["+ group.getName() +"]");
			status = HttpStatus.BAD_REQUEST;
			
		} else {
			boolean isSuccessfulUpdate = db.updateEnvironment(
					envId, 
					"", 
					group.getName());
			
			if (!isSuccessfulUpdate) {
				logger.error("Could not successfully add group to update environment ["+ envName +"]");
				status = HttpStatus.BAD_REQUEST;
			}
		}
		
		return new ResponseEntity<>(status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments/{envName}/users", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addGroup(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@PathVariable(value="envName",required=true) String envName,
			@RequestBody(required=true) User users) {
		
		HttpStatus status = HttpStatus.OK;
		int envId = getEnvironmentId(envName, courseName, uniName);
		
		boolean isUpdateSuccessful = db.updateEnvironment(
				envId,
				users.getNickname(),
				"");
		
		if (!isUpdateSuccessful) {
			logger.error("Could not update user for environment ["+ envName +"]");
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<>(status);
	}
	
	
	
	/*
	 *  THE HELPER FUNCTIONS (OH YEAH!!!)
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
	
	private int getEnvironmentId(String environmentName, String courseName, String universityName) {
		
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
	
	private int getQuestionnaireIdHelper(
			String envName,
			String courseName,
			String universityName) {
		
		int questId = db.getQuestionnaire(
				getEnvironmentId(envName, courseName, universityName));
		
		if (questId == -1) {
			logger.warn("Failed to get questionnaire id for ["+ envName +"]");
			throw new DatabaseException("Failed to get questionnaire id for ["+ envName +"]");
		}
		
		return questId;
	}
	
	private HashMap<String,String[]> getQuestionnaireHelper(
			String envName,
			String courseName,
			String universityName) {
		
		HashMap<String,String[]> questionnaire = 
				db.getQuestions(
						getQuestionnaireIdHelper(
								envName, 
								courseName, 
								universityName));
		
		System.out.println(Arrays.toString(questionnaire.get("Is this ok?")));
		
		if (questionnaire.isEmpty()) {
			logger.warn("No questionnaire items were returned for ["+ envName +"]");
		}
		
		return questionnaire;
	}
	
	private int[] getUserIdsFromNicknames(Set<User> users) {
		int[] idList = new int[users.size()];
		int i = 0;
		for (User user : users) {
			idList[i] = (Integer.valueOf(db.getUserID(user.getNickname())));
			i++;
		}
		
		return idList;
	}
	
	private String getCsvFromUserSet(Set<User> users) {
		String userStr = "";
		for (User user : users) {
			userStr += (user.getNickname().equals("") ? "" : ",") + user.getNickname();
		}
		
		return userStr;
	}
	
	private String getCsvFromUserList(List<User> users) {
		String userStr = "";
		for (User user : users) {
			userStr += (user.getNickname().equals("") ? "" : ",") + user.getNickname();
		}
		
		return userStr;
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
