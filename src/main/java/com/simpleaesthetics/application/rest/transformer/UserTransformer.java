package com.simpleaesthetics.application.rest.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.model.UserInformation;

@Component
public class UserTransformer {

	private final Pattern containsInts = Pattern.compile("^([0-9])+$");
	
	public User transform(String nickname, ArrayList<String> userInfo) {
		List<Integer> ansList = new ArrayList<Integer>();
		System.out.println(userInfo);
		StringTokenizer st = new StringTokenizer(userInfo.get(4), ",");
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if (containsInts.matcher(token).find()) {
				ansList.add(Integer.valueOf(token));
			}
		}
		
		return new User(nickname, ansList);
	}
	
	public UserInformation transformToUserDetails(String nickname, ArrayList<String> userInfo, String password) {
		System.out.println(userInfo);
		return new UserInformation(
				Integer.valueOf(userInfo.get(0)), 
				userInfo.get(1),
				userInfo.get(2),
				nickname,
				userInfo.get(3),
				password);
	}
	
	public User transform(ArrayList<String> userInfo) {
		List<Integer> ansList = new ArrayList<Integer>();
		System.out.println(userInfo);
		StringTokenizer st = new StringTokenizer(userInfo.get(4), ",");
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if (containsInts.matcher(token).find()) {
				ansList.add(Integer.valueOf(token));
			}
		}
		
		return new User(userInfo.get(3), ansList);
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
			userSet.add(new User(st.nextToken().trim()));
		}
		
		return userSet;
	}
	
	public List<User> transformCsvToUserArrayList(String csv) {
		StringTokenizer st = new StringTokenizer(csv, ",");
		List<User> userList = new ArrayList<>();
		while(st.hasMoreTokens()) {
			userList.add(new User(st.nextToken().trim()));
		}
		
		return userList;
	}
	
	
}
