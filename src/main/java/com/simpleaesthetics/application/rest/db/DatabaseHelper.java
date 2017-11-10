package com.simpleaesthetics.application.rest.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.Environment;
import com.simpleaesthetics.application.model.Group;
import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.model.UserInformation;
import com.simpleaesthetics.application.rest.transformer.QuestionnaireTransformer;


@Component
public class DatabaseHelper {
	
	static Logger logger = Logger.getLogger(DatabaseHelper.class.getName());
	
	@Autowired
	private GrouperDB db;
	
	@Autowired
	private QuestionnaireTransformer questionnaireTransformer;
	
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
				"Tutorial Group",
			};
		
		Environment env = new Environment("", true, "password", 5);
		env.setOwner("Jon");
		env.addQuestionAndAnswers("Are you happy?", "yes", "no", "maybe");
		env.addQuestionAndAnswers("Is the sky blue?", "yes", "no", "maybe");
		env.setDeadline("23", "12", "2017");
		
		int userId = 123456785;
		for (String name: userNames) {
			UserInformation user = 
					new UserInformation(userId++,name,"lastName",name,name +".gmail.com");
			addUser(user);
			setQuestionnaireAnswers(
					name.toLowerCase(), 
					questionnaireTransformer.transformForDbString(env.getQuestionnaire()));
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
					
				}
			}
		}
		
		return testWorked;
	}
	
	public void addUser(UserInformation userInfo) {
		int userId = db.insertUser(
				userInfo.getStudentNumber(), 
				userInfo.getFirstName(), 
				userInfo.getLastName(), 
				userInfo.getNickname(),
				userInfo.getEmail());
		
		if (userId == -1) {
			logger.error("Failed to insert user ["+ userInfo.toString() +"]");
//			throw new DatabaseException("Failed to insert user ["+ userInfo.toString() +"]");
		}
	}
	
	public boolean addUniversity(String name) {
		if (db.insertUniversity(name) == -1) {
			logger.warn("Could not insert [" + name +"]");
			return false;
			
		} else {
			logger.info("Successfully inserted ["+ name +"]");
		}
		
		return true;
	}
	
	public ArrayList<ArrayList<String>> getUniversities() {
		return db.queryAllUniversities();
	}
	
	public int getUniversityId(String universityName) {
		int universityId = db.getUniversityID(universityName);
		
		if (universityId == -1) {
			logger.warn("Failed to get university id for ["+ universityName +"]");
//			throw new DatabaseException("Failed to get university id for ["+ universityName +"]");
		}
		
		return universityId;
	}
	
	public ArrayList<String> getUniversityInfo(String universityName) {
		ArrayList<String> universityInfo = db.queryUniversity(getUniversityId(universityName));
		
		if (universityInfo.size() == 0) {
			logger.warn("No university was returned for ["+ universityName +"]");
		}
		
		return universityInfo;
	}
	
	public boolean addCourse(String courseName, String universityName) {
		return (db.insertCourse(courseName, -1,getUniversityId(universityName)) == -1) 
				? false 
				: true;
	}
	
	public String getCourseName(String courseId) {
		return db.queryCourse(Integer.valueOf(courseId)).get(0);
	}
	
	public ArrayList<ArrayList<String>> getCourses(String universityName) {
		return db.getCourses(getUniversityId(universityName));
	}
	
	public int getCourseId(String courseName, String universityName) {
		logger.info("Course Name: "+ courseName);
		
		int courseId = db.getCourseID(courseName, getUniversityId(universityName));
		
		if (courseId == -1) {
			logger.warn("Failed to get course id for ["+ courseName +"]");
//			throw new DatabaseException("Failed to get course id for ["+ courseName +"]");
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
	
	public boolean addEnvironment(Environment env, String courseName, String uniName) {
		logger.info("Attempting to environment [" + env.toString() +"]");
		logger.info("using unversity [" + uniName +"] and course ["+ courseName +"]");
		
		boolean isEnvAdded = true;
		int insertedEnv = db.insertEnvironment(
				env.getName(),
				db.getUserID(env.getOwner().toLowerCase()),
				getCourseId(courseName,uniName), 
				env.isPrivateEnv(), 
				env.getPassword(),
				env.getMaxGroupSize().intValue(), 
				env.getDeadline(), 
				"", 
				"",
				-1);
		
		if (insertedEnv == -1) {
			logger.error("Could not add new environment ["+ env.getName() +"]");
			isEnvAdded = false;
			
		} else {
			int envId = getEnvironmentId(env.getName(), courseName, uniName);
			int insertedQuestionnnaire = db.insertQuestionnaire(
					envId, 
					questionnaireTransformer.transformForDbArray(env.getQuestionnaire()));
			
			System.out.println(questionnaireTransformer.transformForDbArray(env.getQuestionnaire()).toString());
			
			if (insertedQuestionnnaire == -1) {
				logger.error("Could not add new questionnaire to env ["+ env.getName() +"]");
				isEnvAdded = false;
				
			} else {
				boolean successfulUpdate = db.changeQuestionnaire(
						envId, 
						insertedQuestionnnaire);
				
				if (!successfulUpdate) {
					logger.error("Could not update questionnaire id for env ["+ env.getName() +"]");
					isEnvAdded = false;
				}
			}
		}
		
		return isEnvAdded;
	}
	
	public ArrayList<ArrayList<String>> getEnvironments(String universityName, String courseName) {
		ArrayList<ArrayList<String>> envs = db.getEnvironments(getCourseId(courseName, universityName));
		
		if (envs.size() == 0) {
			logger.warn("No environments were returned for ["+ courseName +"]");
		}
		
		return envs;
	}
	
	public int getEnvironmentId(String environmentName, String courseName, String universityName) {
		
		int envId = db.getEnvironmentID(environmentName, getCourseId(courseName, universityName));
		
		if (envId == -1) {
			logger.warn("Failed to get environment id for ["+ environmentName +"]");
//			throw new DatabaseException("Failed to get environment id for ["+ environmentName +"]");
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
	
	public void setGroups(Set<Group> groups, String envName, String courseName, String uniName) {
		for (Group group : groups) {
			int envId = getEnvironmentId(envName, courseName, uniName);
			int insertedGroup = db.insertGroup(
					envId, 
					0, 
					Integer.valueOf(db.queryUser(group.getTaName()).get(0)),
					getCsvFromUserSet(group.getGroupMembers()));
			
			if (insertedGroup == -1) {
				logger.error("Could not add new group ["+ group.getName() +"]");
				
			} else {
				boolean isSuccessfulUpdate = db.updateEnvironment(
						envId, 
						"", 
						group.getName());
				
				if (!isSuccessfulUpdate) {
					logger.error("Could not successfully add group to update environment ["+ envName +"]");
				}
			}
		}
	}
	
	public boolean setQuestionnaireAnswers(String userNickname, HashMap<String,String> ans) {
		try {
			if (db.answerQuestionnaire(Integer.valueOf(db.queryUser(userNickname).get(0)), ans)) {
				return true;
			}
		} catch (IndexOutOfBoundsException e) {
			logger.error(e.getMessage());
		}
		
		return false;
	}
	
	public HashMap<String, String> getUserQuestionnaireAns(
			String userNickname,
			String questions, 
			String answers) {
		
		int ansId = db.getAnswerID(Integer.valueOf(
				db.queryUser(userNickname).get(0)), 
				questions, 
				answers);
		
		System.out.println(db.getAnswers(ansId));
		
		return null;
	}
	
	public int getQuestionnaireId(
			String envName,
			String courseName,
			String universityName) {
		
		int questId = db.getQuestionnaire(
				getEnvironmentId(envName, courseName, universityName));
		
		if (questId == -1) {
			logger.warn("Failed to get questionnaire id for ["+ envName +"]");
//			throw new DatabaseException("Failed to get questionnaire id for ["+ envName +"]");
		}
		
		return questId;
	}
	
	public HashMap<String,String[]> getQuestionnaire(
			String envName,
			String courseName,
			String universityName) {
		
		HashMap<String,String[]> questionnaire = 
				db.getQuestions(
						getQuestionnaireId(
								envName, 
								courseName, 
								universityName));
		
		System.out.println(Arrays.toString(questionnaire.get("Is this ok?")));
		
		if (questionnaire.isEmpty()) {
			logger.warn("No questionnaire items were returned for ["+ envName +"]");
		}
		
		return questionnaire;
	}
	
	public int[] getUserIdsFromNicknames(Set<User> users) {
		int[] idList = new int[users.size()];
		int i = 0;
		for (User user : users) {
			idList[i] = (Integer.valueOf(db.getUserID(user.getNickname())));
			i++;
		}
		
		return idList;
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
