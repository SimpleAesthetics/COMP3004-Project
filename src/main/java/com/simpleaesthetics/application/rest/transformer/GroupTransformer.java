package com.simpleaesthetics.application.rest.transformer;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.Group;

@Component
public class GroupTransformer {
	
	public Map<String, Group> transformCsvToGroupMap(String csv) {
		Map<String, Group> groupMap = new HashMap<>();		
		StringTokenizer st = new StringTokenizer(csv, ",");
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			groupMap.put(token, new Group(token));
		}
		
		return groupMap;
	}
}
