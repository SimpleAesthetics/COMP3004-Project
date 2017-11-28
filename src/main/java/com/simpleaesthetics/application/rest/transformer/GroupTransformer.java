package com.simpleaesthetics.application.rest.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.Group;
import com.simpleaesthetics.application.model.User;

@Component
public class GroupTransformer {
	
	@Autowired
	UserTransformer userTransformer;
	
	public Map<String, Group> transformCsvToGroupMap(String csv) {
		Map<String, Group> groupMap = new HashMap<>();		
		StringTokenizer st = new StringTokenizer(csv, ",");
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			groupMap.put(token, new Group(token));
		}
		
		return groupMap;
	}
	
//	public Set<Group> transformToGroupsSet(ArrayList<ArrayList<String>> groupsInfo) {
//		for (ArrayList<String> groupInfo : groupsInfo) {
//			this.transformToSpecificGroup(groupInfo, group);
//		}
//	}
//	
//	public Group transformToSpecificGroup(ArrayList<String> groupInfo, Set<User> groupMembers) {		
//		return new Group(groupInfo.get(1), null, groupMembers);
//	}
}
