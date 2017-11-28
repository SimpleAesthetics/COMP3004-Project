package com.simpleaesthetics.application.rest.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.Course;
import com.simpleaesthetics.application.model.Environment;
import com.simpleaesthetics.application.model.Group;
import com.simpleaesthetics.application.model.University;
import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.model.UserInformation;
import com.simpleaesthetics.application.rest.transformer.CourseTransformer;
import com.simpleaesthetics.application.rest.transformer.EnvironmentTransformer;
import com.simpleaesthetics.application.rest.transformer.GroupTransformer;
import com.simpleaesthetics.application.rest.transformer.QuestionnaireTransformer;
import com.simpleaesthetics.application.rest.transformer.UniversityTransformer;
import com.simpleaesthetics.application.rest.transformer.UserTransformer;
import com.simpleaesthetics.application.utility.Transformer;


@Component
public class DatabaseHelper {
	
	static Logger logger = Logger.getLogger(DatabaseHelper.class.getName());
	
	@Autowired
	private GrouperDB db;
	
	@Autowired
	private Transformer utilTransformer;
	
	@Autowired
	private QuestionnaireTransformer questionnaireTransformer;
	
	@Autowired
	private UserTransformer userTransformer;
	
	@Autowired
	private UniversityTransformer uniTransformer;
	
	@Autowired
	private EnvironmentTransformer envTransformer;
	
	@Autowired
	private GroupTransformer groupTransformer;
	
	@Autowired
	private CourseTransformer courseTransformer;
	
	public boolean createTestData() {
		boolean testWorked = true;
		
		String[] userNames = new String[] {
			"Brad",
			"Vish",
			"Jon",
			"Osama"
			};
		
		String[] uniNames = new String[] {
			"Carleton University",
			"University of Ottawa",
			"University of Waterloo",
			"York University"
			};
		
		String[] courseNames = new String[] {
			"Economics",
			"Neural Networks",
			"Introduction to Computer Science",
			"Classic American Literature"
			};
		
		String[] envNames = new String[] {
			"Study Group",
			"Project Group",
			"Tutorial Group"
			};
		
		Environment env = new Environment("", true, "password", 5);
		env.setOwner("Jon");
		env.addQuestionAndAnswers("Are you happy?", "yes", "no", "maybe");
		env.addQuestionAndAnswers("Is the sky blue?", "yes", "no", "maybe");
		env.setDeadline("23", "12", "2017");
		
		int userId = 123456785;
		for (String name: userNames) {
//			UserInformation user = 
//					new UserInformation(userId++,name,"lastName",name,name +".gmail.com");
//			addUser(user);
		}
		
		for (String universityName : uniNames) {
			testWorked = testWorked && addUniversity(universityName);
			for (String courseName : courseNames) {
				testWorked = testWorked && addCourse(courseName, universityName);
				for (String envName : envNames) {
					env.setName(envName);
					addEnvironment(
							env, 
							courseName, 
							universityName);
					
					Set<String> questions = env.getQuestionnaire().keySet();
					HashMap<String, String> answers = new HashMap<String, String>();
					
					Random rand = new Random();
					for (String question : questions) {
						answers.put(question, 
								Arrays.toString(new Integer[] {
									rand.nextInt(4) + 1,
									rand.nextInt(4) + 1,
									rand.nextInt(4) + 1,
									rand.nextInt(4) + 1}));
					}
					
					for (String userName : userNames) {
						setQuestionnaireAnswers(
								userName, 
								envName, 
								env.getOwner(), 
								answers);
					}
				}
			}
		}
		
		return testWorked;
	}
	
	public void addUser(UserInformation userInfo) {
		this.assertUserDoesNotExists(userInfo.getUsername());
		int userId = db.insertUser(
				userInfo.getStudentNumber(), 
				userInfo.getFirstName(), 
				userInfo.getLastName(), 
				userInfo.getUsername(),
				userInfo.getEmail(),
				userInfo.getPassword());
		
		if (userId == -1) {
			logger.error("Failed to insert user ["+ userInfo.toString() +"]");
//			throw new DatabaseException("Failed to insert user ["+ userInfo.toString() +"]");
		}
	}
	
	public String getUserNickname(int userId) {
		return db.getUserNickname(userId);
	}
	
	public int getUserId(String nickname) {
		return db.getUserID(nickname);
	}
	
	public User getUser(String nickname) {	
		return userTransformer.transform(db.queryUser(nickname));
	}
	
	public UserInformation getUserInformation(String nickname) {	
		return userTransformer.transformToUserDetails(nickname, db.queryUser(nickname), db.getPassword(nickname));
	}
	
	public ArrayList<User> getUsers() {	
		return userTransformer.transformUsers(db.queryAllUsers());
	}
	
	public University getSpecificUniversity(String universityName) {
		return uniTransformer.transformToUniversity(
				this.transferCourseIdsToNames(this.getUniversityInfo(universityName)));
	}
	
	private ArrayList<String> transferCourseIdsToNames(ArrayList<String> uniInfo) {
		String courseNames = "";
		for (String courseId : utilTransformer.transformCsvToStringList(uniInfo.get(2))) {
			try {
				courseNames += db.queryCourse(Integer.valueOf(courseId.trim())).get(1) +", ";
				
			} catch (NumberFormatException e) {
				logger.error("Could not parse course Id ["+ courseId +"]: "+ e.getMessage());
				
			} catch (IndexOutOfBoundsException e) {
				logger.error("Could not retrieve course using Id ["+ courseId +"]: "+ e.getMessage());
			}
		}
		
		uniInfo.set(2, courseNames);			
		return uniInfo;
	}
	
	public List<University> getUniversities() {
		List<University> universities = uniTransformer.transformToUniversities(db.queryAllUniversities());
		
		for (University uni : universities) {
			List<String> courses = uni.getCoursesList();
			for (int i = 0; i < courses.size(); i++) {
				String trimmedCourse = courses.get(i).trim();
				if (!trimmedCourse.isEmpty()) {
					courses.set(i, this.getCourseName(trimmedCourse));
				}
			}
		}
		
		return universities;
	}
	
	public boolean addUniversity(String name) {
		this.assertUniversityDoesNotExists(name);
		
		if (db.insertUniversity(name) == -1) {
			logger.warn("Could not insert [" + name +"]");
			return false;
			
		} else {
			logger.info("Successfully inserted ["+ name +"]");
		}
		
		return true;
	}
	
	private int getUniversityId(String universityName) {
		int universityId = db.getUniversityID(universityName);
		
		if (universityId == -1) {
			logger.warn("Failed to get university id for ["+ universityName +"]");
			throw new DatabaseException("Failed to get university id for ["+ universityName +"]");
		}
		
		return universityId;
	}
	
	private ArrayList<String> getUniversityInfo(String universityName) {
		ArrayList<String> universityInfo = db.queryUniversity(this.getUniversityId(universityName));
		
		System.out.println("info: " + universityInfo);
		
		if (universityInfo.size() == 0) {
			logger.warn("No university was returned for ["+ universityName +"]");
		}
		
		return universityInfo;
	}
	
	public boolean addCourse(String courseName, String universityName) {
		this.assertCourseDoesNotExists(courseName, universityName);
		return (db.insertCourse(courseName, -1, this.getUniversityId(universityName)) == -1) 
				? false 
				: true;
	}
	
	private String getCourseName(String courseId) {
		try {
			return db.queryCourse(Integer.valueOf(courseId)).get(1);
			
		} catch (NumberFormatException e) {
			logger.error("Failed to parse course Id: ["+ e.getMessage() +"]");
			
		} catch (IndexOutOfBoundsException e) {
			logger.error("Failed to get course Id: ["+ e.getMessage() +"]");
		}
		
		return "";
	}
	
	public Course getSpecificCourse(String courseName, String universityName) {
		return courseTransformer.transformToCourse(
				this.transferEnvIdsToNames(this.getCourseInfo(courseName, universityName)));
	}
	
	private ArrayList<String> transferEnvIdsToNames(ArrayList<String> envInfo) {
		String envNames = "";
		for (String envId : utilTransformer.transformCsvToStringList(envInfo.get(4))) {
			try {
				envNames += db.queryEnvironment(Integer.valueOf(envId.trim())).get(1) +", ";
				
			} catch (NumberFormatException e) {
				logger.error("Could not parse environment Id ["+ envId.trim() +"]: "+ e.getMessage());
				
			} catch (IndexOutOfBoundsException e) {
				logger.error("Could not retrieve environment using Id ["+ envId.trim() +"]");
			}
		}
		
		envInfo.set(4, envNames);			
		return envInfo;
	}
	
	public List<Course> getCourses(String universityName) {
		return courseTransformer.transformToCourses(
				this.transferEnvsIdsToNames(
						db.getCourses(this.getUniversityId(universityName))));
	}
	
	private ArrayList<ArrayList<String>> transferEnvsIdsToNames(ArrayList<ArrayList<String>> envsInfo) {
		for (ArrayList<String> envInfo : envsInfo) {
			String envNames = "";
			for (String envId : utilTransformer.transformCsvToStringList(envInfo.get(4))) {
				try {
					envNames += db.queryEnvironment(Integer.valueOf(envId.trim())).get(1) +", ";
					
				} catch (NumberFormatException e) {
					logger.error("Could not parse environment Id ["+ envId.trim() +"]: "+ e.getMessage());
					
				} catch (IndexOutOfBoundsException e) {
					logger.error("Could not retrieve environment using Id ["+ envId.trim() +"]: "+ e.getMessage());
				}
			}
			
			envInfo.set(4, envNames);
		}
		
		return envsInfo;
	}
	
	public int getCourseId(String courseName, String universityName) {
		int courseId = db.getCourseID(courseName, this.getUniversityId(universityName));
		
		if (courseId == -1) {
			logger.warn("Failed to get course id for ["+ courseName +"]");
			throw new DatabaseException("Failed to get course id for ["+ courseName +"]");
		}
		
		return courseId;
	}
	
	public ArrayList<String> getCourseInfo(String courseName, String unviersityName) {
		ArrayList<String> courseInfo = db.queryCourse(getCourseId(courseName, unviersityName));
		
		if (courseInfo.size() == 0) {
			logger.warn("No courses were returned for ["+ courseName +"]");
		}
		
		return courseInfo;
	}
	
	public ArrayList<String> getCourseInfo(int courseName) {
		ArrayList<String> courseInfo = db.queryCourse(courseName);
		
		if (courseInfo.size() == 0) {
			logger.warn("No courses were returned for ["+ courseName +"]");
		}
		
		return courseInfo;
	}
	
	public void addEnvironment(Environment env, String courseName, String uniName) {
		this.assertEnvironmentDoesNotExists(env.getName(), courseName, uniName);
		int insertedEnvId = db.insertEnvironment(
				env.getName(),
				db.getUserID(env.getOwner().toLowerCase()),
				getCourseId(courseName,uniName), 
				env.isPrivateEnv(), 
				env.getPassword(),
				env.getMaxGroupSize().intValue(), 
				env.getDeadlineStr(), 
				"", 
				"",
				-1);
		
		if (insertedEnvId == -1) {
			throw new DatabaseException("Could not add new environment ["+ env.getName() +"]");
			
		} else {
			int insertedQuestionnnaire = 
					db.insertQuestionnaire(
						insertedEnvId, 
						questionnaireTransformer.transformForDbArray(env.getQuestionnaire()));
			
			if (insertedQuestionnnaire == -1) {
				throw new DatabaseException("Could not add new questionnaire to env ["+ env.getName() +"]");
				
			} else {
				boolean successfulUpdate = db.changeQuestionnaire(
						insertedEnvId, 
						insertedQuestionnnaire);
				
				if (!successfulUpdate) {
					throw new DatabaseException("Could not update questionnaire id for env ["+ env.getName() +"]");
				}
			}
		}
	}
	
	public void updateEnvironment(Environment env, String courseName, String uniName) {
		this.assertEnvironmentExists(env.getName(), courseName, uniName);
		
		Set<User> userset = env.getUsers();
		String userlist = "";
		for(User x : userset) {
			userlist += db.getUserID(x.getNickname()) + ", ";
		}
		userlist=userlist.substring(0,userlist.length()-2);
		Set<Group> groupset = env.getGroups();
		String grouplist = "";
		for(Group x : groupset) {
			grouplist += db.getGroupID(this.getEnvironmentId(env.getName(),courseName,uniName),x.getName())	+ ", ";
		}
		grouplist=grouplist.substring(0,grouplist.length()-2);
		int qid = -1;
		if(db.getQuestionnaire(this.getEnvironmentId(env.getName(),courseName,uniName)) > -1) {
			qid = this.getEnvironmentId(env.getName(),courseName,uniName);
		}
		boolean updated = db.updateEnvironment(
				this.getEnvironmentId(env.getName(),courseName,uniName),
				env.getName(),
				db.getUserID(env.getOwner().toLowerCase()),
				getCourseId(courseName,uniName), 
				env.isPrivateEnv(), 
				env.getPassword(),
				env.getMaxGroupSize().intValue(), 
				env.getDeadline().toString(), 
				userlist, 
				grouplist,
				qid);
		
		if (updated == false) {
			throw new DatabaseException("Could not update environment ["+ env.getName() +"]");
			
		} 
	}
	
	public List<Environment> getEnvironments(String universityName, String courseName) {
		ArrayList<ArrayList<String>> envInfos = db.getEnvironments(this.getCourseId(courseName, universityName));
		List<Environment> envs = new ArrayList<Environment>();
		for(ArrayList<String> info : envInfos) {
			if(info.size() >= 2) {
				String envName = info.get(1);
				Environment env = this.getSpecificEnvironment(envName,courseName,universityName);
				envs.add(env);
			}
		}
		return envs;
		/*ArrayList<ArrayList<String>> envInfos = 
				db.getEnvironments(
						this.getCourseId(courseName, universityName));
		
		List<Environment> envs = new ArrayList<>();
		
		for (ArrayList<String> envInfo : envInfos) {
			Set<User> userSet = userTransformer.transformCsvToUserHashSet(envInfo.get(7));
			userSet = this.transferAnswers(envInfo.get(0), envInfo.get(2), userSet);
			Map<String, String[]> questionnaire = this.getQuestionnaire(envInfo.get(1), courseName, universityName);
			
			Environment env = 
					envTransformer.transformToEnvironment(
						envInfo, 
						questionnaire, 
						userSet);
			
			envs.add(env);
		}
		
		if (envs.size() == 0) {
			logger.warn("No environments were returned for ["+ courseName +"]");
		}
//		
		return envs;*/
	}
	
	public Environment getSpecificEnvironment(String envName, String courseName, String universityName) {
		ArrayList<String> envInfo = this.getEnvironmentInfo(envName, courseName, universityName);
		Set<User> userSet = userTransformer.transformCsvToUserHashSet(envInfo.get(7));
		Set<Group> groupSet = this.getAllGroupsForEnv(envName,courseName,universityName);
		
		for (User user : userSet) {
			List<Integer> answersList = new ArrayList<Integer>();
			HashMap<String, List<String>> questionnaire = 
					this.getUserQuestionnaireAns(
						envName, 
						envInfo.get(2), 
						user.getNickname());
			
			for (List<String> answers : questionnaire.values()) {
				answersList.addAll(utilTransformer.tranformToIntegerList(answers));
			}
			
			user.setQuestionAnswers(answersList);
		}
		
//		ArrayList<ArrayList<String>> groupsInfo = 
//				db.getGroups(db.getGroupID(this.getEnvironmentId(envName, courseName, universityName)));
//		
//		for (ArrayList<String> groupInfo : groupsInfo) {
//			groupMap.get(groupInfo.get(1));
//		}
		
		Environment env = envTransformer.transformToEnvironment(
				envInfo,
				this.getQuestionnaire(envName, courseName, universityName),
				userSet,
				groupSet);
		
		return env;
	}
	
	public void deleteSpecificEnvironment(String envName, String courseName, String universityName) {
		boolean isDeleted = db.deleteEnvironment(this.getEnvironmentId(envName, courseName, universityName));
		if (!isDeleted) {
			throw new DatabaseException("Could not delete environment ["+ envName +"]");
		}
	}
	
	private Set<User> transferAnswers(String envName, String owner, Set<User> userSet) {
		for (User user : userSet) {
			List<Integer> answersList = new ArrayList<Integer>();
			HashMap<String, List<String>> questionnaire = 
					this.getUserQuestionnaireAns(
						envName, 
						owner, 
						user.getNickname());
			
			for (List<String> answers : questionnaire.values()) {
				answersList.addAll(utilTransformer.tranformToIntegerList(answers));
			}
			
			user.setQuestionAnswers(answersList);
		}
		return userSet;
	}
	
	public int getEnvironmentId(String environmentName, String courseName, String universityName) {
		
		int envId = db.getEnvironmentID(environmentName, getCourseId(courseName, universityName));
		
		if (envId == -1) {
			logger.warn("Failed to get environment id for ["+ environmentName +"]");
			throw new DatabaseException("Failed to get environment id for ["+ environmentName +"]");
		}
		
		return envId;
	}
	
	public ArrayList<String> getEnvironmentInfo( 
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
	
	public void addSpecificGroupToEnv(Group group, String envName, String courseName, String uniName) {
		ArrayList<String> envinfo = this.getEnvironmentInfo(envName,courseName,uniName);
		int finalized = 0;
		if(envinfo.size() >= 9) {
			int maxsize = Integer.parseInt(envinfo.get(5));
			if(group.getGroupMembers().size() >= maxsize) {
				finalized = 1;
			}
		}
		
		int insertedGroup = 
				db.insertGroup(
					this.getEnvironmentId(envName, courseName, uniName),
					group.getName(),
					finalized, 
					this.getUserId(group.getTaName()),
					this.getCsvFromUserSet(group.getGroupMembers()));
		
		if (insertedGroup == -1) {
			throw new DatabaseException("Could not add new group ["+ group.getName() +"]");
			
		}
	}
	
	public void addGroupsToEnvironment(Set<Group> groups, String envName, String courseName, String uniName) {
		for (Group group : groups) {
			this.addSpecificGroupToEnv(group, envName, courseName, uniName);
		}
	}
	
	public boolean setQuestionnaireAnswers(
			String userNickname,
			String envName, 
			String envOwner,  
			HashMap<String,String> ans) {
		
		try {
			if (db.answerQuestionnaire(
					db.getEnvID(envName,db.getUserID(envOwner)),
					db.getUserID(userNickname),
					ans)) {
				
				return true;
			}
		} catch (IndexOutOfBoundsException e) {
			logger.error(e.getMessage());
		}
		
		return false;
	}
	
	public HashMap<String, List<String>> getUserQuestionnaireAns(
			String envName,
			String envOwner,
			String userNickname) {
		
		try {
			return questionnaireTransformer
						.transformForModelFromAnswerString(
								db.getAnswers(
										db.getEnvID(envName,db.getUserID(envOwner)),
										db.getUserID(userNickname)));
		
		} catch (IndexOutOfBoundsException e) {
			logger.warn("Could not find any users with nickname ["+ userNickname +"]");
			return new HashMap<String, List<String>>();
		}
	}
	
	public int getQuestionnaireId(
			String envName,
			String courseName,
			String universityName) {
		
		int questId = 
				db.getQuestionnaire(
						this.getEnvironmentId(envName, courseName, universityName));
		
		if (questId == -1) {
			logger.warn("Failed to get questionnaire id for ["+ envName +"]");
			throw new DatabaseException("Failed to get questionnaire id for ["+ envName +"]");
		}
		
		return questId;
	}
	
	public Map<String,String[]> getQuestionnaire(
			String envName,
			String courseName,
			String universityName) {
		
		Map<String,String[]> questionnaire = 
				db.getQuestions(
						this.getEnvironmentId(
								envName, 
								courseName, 
								universityName));
		
		if (questionnaire.isEmpty()) {
			logger.warn("No questionnaire items were returned for ["+ envName +"]");
		}
		
		return questionnaire;
	}
	
	public void addSpecificUserToEnv(User user, String envName, String courseName, String uniName) {
		this.assertUserExists(user.getNickname());
		this.assertUserNotInEnv(user, this.getSpecificEnvironment(envName, courseName, uniName));
		
		int envId = this.getEnvironmentId(envName, courseName, uniName);
		boolean isSuccessful = db.updateEnvironment(
				envId,
				user.getNickname(),
				"");
		
		if (!isSuccessful) {
			throw new DatabaseException("Could not update user for environment ["+ envName +"]");
			
		} else {
			Set<String> questions = this.getQuestionnaire(envName, courseName, uniName).keySet();
			List<String> answersStringList = utilTransformer.tranformToStringList(user.getQuestionAnswers());
			String[] ansStringArray = answersStringList.toArray(new String[answersStringList.size()]);
			HashMap<String, String> transformedAns = new HashMap<String, String>();
			String envOwner = this.getEnvironmentInfo(envName, courseName, uniName).get(2);
			
			int i = 0;
			for (String question : questions) {
				transformedAns.put(question, ansStringArray[i]);
				++i;
			}
			
			isSuccessful = this.setQuestionnaireAnswers(
					user.getNickname(), 
					envName,
					envOwner,
					transformedAns);
			
			if (!isSuccessful) {
				throw new DatabaseException("Could not update questionnaire for ["+ user.getNickname() +"]");
			}
		}
	}
	
	public Group getGroup(String groupName, String envName, String courseName, String uniName) {
		int envID = this.getEnvironmentId(envName,courseName,uniName);
		this.assertGroupExists(db.getGroupID(envID,groupName));
		ArrayList<String> groupinfo = db.queryGroup(db.getGroupID(envID,groupName));
		Group group = new Group();
		group.setName(groupName);
		group.setTaName(db.getUserNickname(Integer.parseInt(groupinfo.get(3))));
		group.setGroupMembers(userTransformer.transformCsvToUserHashSet(groupinfo.get(4)));
		return group;
	}
	
	public Set<Group> getAllGroupsForEnv(String envName, String courseName, String uniName) {
		int envID = this.getEnvironmentId(envName,courseName,uniName);
		ArrayList<ArrayList<String>> groupsinfo = db.getGroups(envID);
		Set<Group> groups = new HashSet<Group>();
		for(ArrayList<String> g: groupsinfo) {
			this.assertIsActuallyAGroup(g);
			Group temp = new Group();
			temp.setName(g.get(1));
			temp.setTaName(db.getUserNickname(Integer.parseInt(g.get(3))));
			temp.setGroupMembers(userTransformer.transformCsvToUserHashSet(g.get(4)));
			groups.add(temp);
		}
		return groups;
	}
	
	
	/** Helper Functions **/
	
	
	private void assertUserNotInEnv(User user, Environment env) {
		if (env.getUsers().contains(user)) {
			throw new DatabaseException("Assert failed: User already in Environment");
		}
	}
	
	private void assertGroupExists(int groupID) {
		ArrayList<String> temp = db.queryGroup(groupID);
		if(temp.isEmpty() || temp.size() < 5) {
			throw new DatabaseException("Assert failed: Group does not exist");
		}
	}
	
	private void assertIsActuallyAGroup(ArrayList<String> groupinfo) {
		if(groupinfo.isEmpty() || groupinfo.size() < 5) {
			throw new DatabaseException("Assert failed: Not actually a group");
		}
	}
	
	private void assertUserExists(String username) {
		if (db.queryUser(username).size() < 6) {
			throw new DatabaseException("Assert failed: Could not find expected user");
		}
	}
	
	private void assertUserDoesNotExists(String username) {
		boolean exists = false;
		try {
			exists = !db.queryUser(username).isEmpty();
		} catch (DatabaseException e) {
			// Do nothing
		}
		
		if (exists) {
			throw new DatabaseException("Assert failed: User already exists");
		}
	}
	
	private void assertUniversityDoesNotExists(String universityName) {
		boolean exists = false;
		try {
			exists = !db.queryUniversity(this.getUniversityId(universityName)).isEmpty();
					
		} catch (DatabaseException e) {
			// Do nothing
		}
		
		if (exists) {
			throw new DatabaseException("Assert failed: University already exists");
		}
	}
	
	private void assertCourseDoesNotExists(String courseName, String universityName) {
		boolean exists = false;
		try {
			exists = !db.queryCourse(this.getCourseId(courseName, universityName)).isEmpty();
			
		} catch (DatabaseException e) {
			// Do nothing
		}
		
		if (exists) {
			throw new DatabaseException("Assert failed: Course already exists for this University");
		}
	}
	
	private void assertEnvironmentExists(String environmentName, String courseName, String universityName) {
		boolean exists = false;
		try {
			exists = !db.queryEnvironment(this.getEnvironmentId(environmentName, courseName, universityName)).isEmpty();
			
		} catch (DatabaseException e) {
			// Do nothing
		}
		
		if (!exists) {
			throw new DatabaseException("Assert failed: Environment does not exist");
		}
	}
	
	private void assertEnvironmentDoesNotExists(String environmentName, String courseName, String universityName) {
		boolean exists = false;
		try {
			exists = !db.queryEnvironment(this.getEnvironmentId(environmentName, courseName, universityName)).isEmpty();
			
		} catch (DatabaseException e) {
			// Do nothing
		}
		
		if (exists) {
			throw new DatabaseException("Assert failed: Environment already exists for this Course");
		}
	}
	
	public String getCsvFromUserSet(Set<User> users) {
		String userStr = "";
		for (User user : users) {
			userStr += (user.getNickname().equals("") ? "" : ",") + user.getNickname();
		}
		
		return userStr;
	}
	
	public String getCsvFromUserList(List<User> users) {
		String userStr = "";
		for (User user : users) {
			userStr += (user.getNickname().equals("") ? "" : ",") + user.getNickname();
		}
		
		return userStr;
	}
	
	
}
