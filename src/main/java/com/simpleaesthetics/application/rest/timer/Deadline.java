package com.simpleaesthetics.application.rest.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.University;
import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.rest.Grouper;
import com.simpleaesthetics.application.rest.db.DatabaseHelper;
import com.simpleaesthetics.application.rest.transformer.UniversityTransformer;

@Component
public class Deadline {
	
	static Logger logger = Logger.getLogger(Deadline.class.getName());
	
	@Autowired
	private Grouper grouper;
	
	@Autowired
	private DatabaseHelper helper;
	
	@Autowired
	private UniversityTransformer uniTransformer;
	
	@Scheduled(fixedDelay=30000)
	public void checkAndExecuteDealines() {
		logger.info("RUNNING SCHEDULED TASK ["+ new Date().toString() +"]; Check and execute deadlines");
		
//		List<University> universities = uniTransformer.transform(helper.getUniversities());
//		logger.info("Found "+ universities.size() +" universities to sort");
//		
//		for (University university : universities) {
//			
//			logger.info("Attempting to sort university ["+ university.getName() +"]");
//			
//			for (String courseName : university.getCoursesList()) {	
//				String trimmedCourse = courseName.trim();
//				
//				if (trimmedCourse.isEmpty()) {
//					continue;
//				}
//				
//				logger.info("Attempting to sort course ["+ courseName +"]");
//				
//				ArrayList<ArrayList<String>> envInfos = 
//						helper.getEnvironments(
//								university.getName(), 
//								helper.getCourseInfo(Integer.valueOf(trimmedCourse)).get(0));
//				
//				for (ArrayList<String> envInfo : envInfos) {
////					helper.setGroups(
////							grouper.findAllGroups(getUserHashSetFromCSV(envInfo.get(6)), 4), 
////							envInfo.get(1), 
////							courseName, 
////							university.getName());
//					logger.info("Returning sorted groups: \n"+
//							grouper.findAllGroups(
//									getUserHashSetFromCSV(envInfo.get(6)), 
//									4));
//				}
//			}	
//		}
	}
	
	private HashSet<User> getUserHashSetFromCSV(String usersCSV) {
		StringTokenizer st = new StringTokenizer(usersCSV, ",");
		HashSet<User> userSet = new HashSet<>();
		while(st.hasMoreTokens()) {
			userSet.add(new User(st.nextToken()));
		}
		
		return userSet;
	}
	
}
