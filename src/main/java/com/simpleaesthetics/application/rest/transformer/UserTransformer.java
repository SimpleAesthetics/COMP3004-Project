package com.simpleaesthetics.application.rest.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.User;

@Component
public class UserTransformer {

	public User transform(String nickname, ArrayList<String> userInfo) {
		String[] ansArrStrs = userInfo.get(4).split(",");
		Integer[] ansArrInt = new Integer[ansArrStrs.length];
		
		for (int i = 0; i < ansArrStrs.length; ++i) {
			ansArrInt[i] = Integer.parseInt(ansArrStrs[i]);
		}
		
		return new User(nickname, new ArrayList<Integer>(Arrays.asList(ansArrInt)));
	}
	
	public User transform(ArrayList<String> userInfo) {
		String[] ansArrStrs = userInfo.get(4).split(",");
		Integer[] ansArrInt = new Integer[ansArrStrs.length];
		
		for (int i = 0; i < ansArrStrs.length; ++i) {
			ansArrInt[i] = Integer.parseInt(ansArrStrs[i]);
		}
		
		return new User("", new ArrayList<Integer>(Arrays.asList(ansArrInt)));
	}
	
	public ArrayList<User> transformUsers(ArrayList<ArrayList<String>> users) {
		ArrayList<User> transformedUsers = new ArrayList<>();
		for (ArrayList<String> userInfo : users) {
			transformedUsers.add(transform(userInfo));
		}
		
		return transformedUsers;
	}
	
	public Set<User> transformCsvToUserHashSet(String csv) {
		StringTokenizer st = new StringTokenizer(csv, ",");
		Set<User> userSet = new HashSet<>();
		while(st.hasMoreTokens()) {
			userSet.add(new User(st.nextToken()));
		}
		
		return userSet;
	}
	
	public List<User> transformCsvToUserArrayList(String csv) {
		StringTokenizer st = new StringTokenizer(csv, ",");
		List<User> userList = new ArrayList<>();
		while(st.hasMoreTokens()) {
			userList.add(new User(st.nextToken()));
		}
		
		return userList;
	}
	
	
}
