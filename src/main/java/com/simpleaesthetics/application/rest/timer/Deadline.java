package com.simpleaesthetics.application.rest.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.Environment;
import com.simpleaesthetics.application.model.Group;
import com.simpleaesthetics.application.model.University;
import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.rest.Grouper;
import com.simpleaesthetics.application.rest.db.DatabaseHelper;
import com.simpleaesthetics.application.rest.transformer.UniversityTransformer;

import ch.qos.logback.classic.db.DBHelper;

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
		Date currDate = new Date();
		logger.info("RUNNING SCHEDULED TASK ["+ currDate.toString() +"]; Check and execute deadlines");
		
		List<University> universities = helper.getUniversities();
		logger.info("Found "+ universities.size() +" universities to use for sorting");
		
		for (University university : universities) {
			logger.info("Attempting to sort university ["+ university.getName() +"]");
			
			for (String courseName : university.getCoursesList()) {	
				String trimmedCourse = courseName.trim();
				if (trimmedCourse.isEmpty()) continue;
				
				logger.info("Attempting to sort course ["+ courseName +"]");
				List<Environment> envList = helper.getEnvironments(university.getName(), courseName);
				
				for (Environment env : envList) {
					logger.info("Sorting environment ["+ env.getName() +"]");
					
					if (env.afterDate(currDate)) {
						Set<Group> groupSet = grouper.findAllGroups(env.getUsers(), env.getMaxGroupSize());
						helper.addGroupsToEnvironment(groupSet, env.getName(), courseName, university.getName());
					}
				}
			}	
		}
	}
	
}
