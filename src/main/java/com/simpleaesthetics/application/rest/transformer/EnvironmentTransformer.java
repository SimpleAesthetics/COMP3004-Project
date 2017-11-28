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
			Set<User> users,
			Set<Group> groups
			) {
		
		Environment env = new Environment(
				envInfo.get(1),
				Boolean.valueOf(envInfo.get(3)),
				envInfo.get(4),
				Integer.valueOf(envInfo.get(5)));
		
		env.setOwner(envInfo.get(2));
		env.setDeadlineStr(envInfo.get(6));
		env.setUsers(users);
		env.setGroups(groups);
		env.setQuestionnaire(
				questTransformer.transformForModel(questionnaire));
		
		return env;
	}
	
//	private HashSet<Group> getGroupHashSetFromCSV(String csv) {
//		StringTokenizer st = new StringTokenizer(csv, ",");
//		HashSet<Group> groupSet = new HashSet<>();
//		while(st.hasMoreTokens()) {
//			String token = st.nextToken();
//			HashSet<String> group = new HashSet<String>();
//			group.add(token);
//			groupSet.add(new Group(group));
//		}
//		
//		return groupSet;
//	}
	
}
	
