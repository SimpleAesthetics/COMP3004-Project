package com.simpleaesthetics.application.rest.transformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.Environment;
import com.simpleaesthetics.application.model.Group;
import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.rest.transformer.QuestionnaireTransformer;

@Component
public class EnvironmentTransformer {

	@Autowired
	QuestionnaireTransformer questTransformer;
	
	public Environment transform(
			ArrayList<String> envInfo,
			Map<String, String[]> questionnaire) {
		
		System.out.println(envInfo);
		
		Environment env = new Environment(
				envInfo.get(0),
				Boolean.valueOf(envInfo.get(2)),
				envInfo.get(3),
				Integer.valueOf(envInfo.get(4)));
		
		env.setDeadlineStr(envInfo.get(5));
//		env.setUsers(getHashSetFromCSV(envInfo));
//		env.setGroups(getHashSetFromCSV(envInfo));
		env.setQuestionnaire(
				questTransformer.transformForModel(questionnaire));
		
		return env;
	}
	
	
//	private HashSet<String> getUserHashSetFromCSV(ArrayList<String> envInfo) {
//		StringTokenizer st = new StringTokenizer(envInfo.get(6), ",");
//		HashSet<User> userSet = new HashSet<>();
//		while(st.hasMoreTokens()) {
//			userSet.add(new User(st.nextToken()));
//		}
//		
//		return set;
//	}
//	
//	private HashSet<String> getGroupHashSetFromCSV(ArrayList<String> envInfo) {
//		StringTokenizer st = new StringTokenizer(envInfo.get(7), ",");
//		HashSet<Group> groupSet = new HashSet<>();
//		while(st.hasMoreTokens()) {
//			groupSet.add(st.nextToken());
//		}
//		
//		return set;
//	}
	
}
