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
import com.simpleaesthetics.application.rest.transformer.UniversityTransformer;
import com.simpleaesthetics.application.rest.transformer.UserTransformer;
import com.simpleaesthetics.application.utility.Transformer;
import com.simpleaesthetics.application.model.Course;
import com.simpleaesthetics.application.model.University;
import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.rest.db.DatabaseException;
import com.simpleaesthetics.application.rest.db.DatabaseHelper;

@Controller
@RequestMapping
public class GrouperInfoResource {
	
	static Logger logger = Logger.getLogger(GrouperInfoResource.class.getName());
	
	@Autowired
	private Grouper grouper;
	
	@Autowired
	private GrouperDB db;
	
	@Autowired
	private Transformer utilsTransformer;
	
	@Autowired
	private EnvironmentTransformer envTransformer;
	
	@Autowired
	private QuestionnaireTransformer questionnaireTransformer;
	
	@Autowired
	private UniversityTransformer uniTransformer;
	
	@Autowired
	private UserTransformer userTransformer;
	
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
		List<University> universities = uniTransformer.transform(dbHelper.getUniversities());
		
		for (University uni : universities) {
			List<String> courses = uni.getCoursesList();
			System.out.println(courses);
			for (int i = 0; i < courses.size(); i++) {
				String trimmedCourse = courses.get(i).trim();
				if (!trimmedCourse.isEmpty()) {
					courses.set(i, dbHelper.getCourseName(trimmedCourse));
				}
			}
		}
		
		if (universities.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
			logger.info("No universities were returned");
		}
		
		return new ResponseEntity<List<University>>(
				universities,
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<ArrayList<String>> getSpecificUniversity(
			@PathVariable(value="uniName",required=true) String uniName) {
		
		HttpStatus status = HttpStatus.OK;
		ArrayList<String> universityInfo = dbHelper.getUniversityInfo(uniName);
		
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
		ArrayList<ArrayList<String>> courses = dbHelper.getCourses(uniName);
		
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
		List<String> course = dbHelper.getCourseInfo(courseName, uniName);
		
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
		
		HttpStatus status = HttpStatus.OK;
		boolean isCourseInserted = dbHelper.addCourse(course.getName(), uniName);
		
		if (!isCourseInserted) {
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
		ArrayList<ArrayList<String>> envList = dbHelper.getEnvironments(uniName, courseName);
		
		if (envList.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return new ResponseEntity<ArrayList<ArrayList<String>>>(
				envList,
				status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments/{envName}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<ArrayList<String>> getSpecificEnvironment(
			@RequestHeader(value="sortGroups", defaultValue="false") boolean toSort,
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@PathVariable(value="envName",required=true) String envName) {
		
		ArrayList<String> envInfo = dbHelper.getEnvironmentInfo(envName, courseName, uniName);
//		System.out.println("envInfo: "+ envInfo);
//		Set<User> userSet = userTransformer.transformCsvToUserHashSet(envInfo.get(7));
//		
//		for (User user : userSet) {
//			System.out.println(user);
//			List<Integer> answersList = new ArrayList<Integer>();
//			HashMap<String, List<String>> questionnaire = 
//					dbHelper.getUserQuestionnaireAns(
//						envName, 
//						envInfo.get(2), 
//						Integer.valueOf(user.getNickname()));
//				
//			user.setNickname(dbHelper.getUserNickname(Integer.valueOf(user.getNickname())));
//			
//			for (List<String> answers : questionnaire.values()) {
//				answersList.addAll(utilsTransformer.tranformToIntegerList(answers));
//			}
//			
//			user.setQuestionAnswers(answersList);
//		}
//		
//		System.out.println(userSet);
//		
//		Environment env = envTransformer.transform(
//				envInfo,
//				dbHelper.getQuestionnaire(envName, courseName, uniName),
//				userSet);
//		
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
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addEnvironment(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@RequestBody(required=true) Environment env) {
		
		HttpStatus status = HttpStatus.OK;
		boolean isEnvInserted = dbHelper.addEnvironment(env, courseName, uniName);
		
		if (!isEnvInserted) {
			logger.error("Could not add new environment ["+ env.getName() +"]");
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<String>(status);
	}
	
	@RequestMapping(value="/universities/{uniName}/courses/{courseName}/environments/{envName}/groups", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addGroupToEnv(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@PathVariable(value="envName",required=true) String envName,
			@RequestBody(required=true) Group group) {
		
		HttpStatus status = HttpStatus.OK;
		int envId = dbHelper.getEnvironmentId(envName, courseName, uniName);
		
		int insertedGroup = db.insertGroup(
				envId, 
				0, 
				Integer.valueOf(db.queryUser(group.getTaName()).get(0)),
				dbHelper.getCsvFromUserSet(group.getGroupMembers()));
		
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
	public @ResponseBody ResponseEntity<String> addUserToEnv(
			@PathVariable(value="uniName",required=true) String uniName,
			@PathVariable(value="courseName",required=true) String courseName,
			@PathVariable(value="envName",required=true) String envName,
			@RequestBody(required=true) User user) {
		
		HttpStatus status = HttpStatus.OK;
		int envId = dbHelper.getEnvironmentId(envName, courseName, uniName);
		
		boolean isSuccessful = db.updateEnvironment(
				envId,
				user.getNickname(),
				"");
		
		if (!isSuccessful) {
			logger.error("Could not update user for environment ["+ envName +"]");
			status = HttpStatus.BAD_REQUEST;
			
		} else {
			Set<String> questions = dbHelper.getQuestionnaire(envName, courseName, uniName).keySet();
			List<String> answersStringList = utilsTransformer.tranformToStringList(user.getQuestionAnswers());
			String[] ansStringArray = answersStringList.toArray(new String[answersStringList.size()]);
			HashMap<String, String> transformedAns = new HashMap<String, String>();
			String envOwner = dbHelper.getEnvironmentInfo(envName, courseName, uniName).get(2);
			
			int i = 0;
			for (String question : questions) {
				transformedAns.put(question, ansStringArray[i]);
			}
			
			System.out.println(user);
			System.out.println(transformedAns);
			System.out.println(envOwner);
			
			isSuccessful = dbHelper.setQuestionnaireAnswers(
					user.getNickname(), 
					envName,
					envOwner,
					transformedAns);
			
			if (!isSuccessful) {
				logger.error("Could not update questionnaire for ["+ user.getNickname() +"]");
				status = HttpStatus.BAD_REQUEST;
			}
		}
		
		return new ResponseEntity<>(status);
	}

	
} // End of class
