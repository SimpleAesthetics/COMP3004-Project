package com.simpleaesthetics.application.rest.transformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
	
	@Autowired
	UserTransformer userTransformer;
	
	public Environment transformToEnvironment(
			ArrayList<String> envInfo,
			Map<String, String[]> questionnaire,
			Set<User> users
			) {
		
		Environment env = new Environment(
				envInfo.get(1),
				Boolean.valueOf(envInfo.get(3)),
				envInfo.get(4),
				Integer.valueOf(envInfo.get(5)));
		
		env.setOwner(envInfo.get(2));
		env.setDeadlineStr(envInfo.get(6));
		env.setUsers(users);
//		env.setGroups(getHashSetFromCSV(envInfo));
		env.setQuestionnaire(
				questTransformer.transformForModel(questionnaire));
		
		return env;
	}
	
}
	
	
//	private HashSet<String> getGroupHashSetFromCSV(ArrayList<String> envInfo) {
//		StringTokenizer st = new StringTokenizer(envInfo.get(7), ",");
//		HashSet<Group> groupSet = new HashSet<>();
//		while(st.hasMoreTokens()) {
//			groupSet.add(st.nextToken());
//		}
//		
//		return set;
//	}
	
