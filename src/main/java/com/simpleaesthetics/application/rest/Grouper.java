package com.simpleaesthetics.application.rest;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.Group;
import com.simpleaesthetics.application.model.User;
import com.simpleaesthetics.application.rest.db.DatabaseHelper;

@Component
public class Grouper {
	
	static Logger logger = Logger.getLogger(Grouper.class.getName());
	
	private int totalCount = 0;
	
	public Set<Group> findAllGroups(Set<User> users, Integer maxMatches) {
		totalCount = 0;
		if (maxMatches == 0) return null;
		
		Set<Group> sortedGroups = new HashSet<Group>();
		
		while (users.size() > 0) {
			User currUser = users.iterator().next();
			if (currUser.getQuestionAnswers().isEmpty()) continue;
			
			Group currGroup = new Group(findSingleGroup(users, maxMatches));
			users.removeAll(currGroup.getGroupMembers());
			sortedGroups.add(currGroup);
		}
		
		logger.info("Number of operations completed: "+ totalCount);
		return sortedGroups;
	}
	
	public Set<User> findSingleGroup(Set<User> users, Integer maxMatches) {
		Iterator<User> iterUser = users.iterator();
		Integer bestScore = -1;
		
		Set<User> globalMatches = null;
		
		while (iterUser.hasNext()) {
			User currUser = iterUser.next();
			List<Integer> currAns = currUser.getQuestionAnswers();
			Set<User> matches = new HashSet<>();
			matches.add(currUser);
			Set<User> copy = new HashSet<>(users);
			copy.remove(currUser);
			
			Map<Integer, Queue<User>> sortingMap = this.createSortingMap(currAns, copy.iterator());
			Integer i = currAns.size();
			Integer currScore = 0;
			
			while (i >= 0) {
				if (sortingMap.containsKey(i)) {
					Queue<User> currMatches = sortingMap.get(i);
					while (currMatches.size() != 0 && matches.size() != maxMatches) {
						matches.add(currMatches.poll());
						currScore += i;
						totalCount += 1;
					}
					
					if (matches.size() == maxMatches) {
						break;
					}
				}
				
				--i;
			}
			
			if (currScore > bestScore) {
				globalMatches = matches;
			}
		}
		
		return globalMatches;
	}
	
//	public Set<Group> findAllGroups(Set<User> users, Integer maxMatches) {
//		if (maxMatches == 0) return null;
//		
//		Set<Group> sortedGroups = new HashSet<Group>();
//		
//		while (users.size() > 0) {
//			User currUser = users.iterator().next();
//			if (currUser.getQuestionAnswers().isEmpty()) continue;
//			
//			Group currGroup = new Group(findSingleGroup(currUser, users, maxMatches));
//			users.removeAll(currGroup.getGroupMembers());
//			sortedGroups.add(currGroup);
//		}
//		
//		return sortedGroups;
//	}
//	
//	public Set<User> findSingleGroup(User toMatch, Set<User> users, Integer maxMatches) {
//		if (maxMatches == 0) return null;
//		
//		List<Integer> currAns = toMatch.getQuestionAnswers();
//		Map<Integer, Queue<User>> sortingMap = this.createSortingMap(currAns, users.iterator());
//		
//		Set<User> matches = new HashSet<>();
//		matches.add(toMatch);
//		
//		Integer i = currAns.size();
//		while (i >= 0) {
//			if (sortingMap.containsKey(i)) {
//				Queue<User> currMatches = sortingMap.get(i);
//				while (currMatches.size() != 0 && matches.size() != maxMatches) {
//					matches.add(currMatches.poll());
//				}
//				
//				if (matches.size() == maxMatches) {
//					break;
//				}
//			}
//			
//			--i;
//		}
//		
//		return matches;
//	}
	
	private Map<Integer, Queue<User>> createSortingMap(List<Integer> currAns, Iterator<User> userIter) {
		Map<Integer, Queue<User>> sortingMap = new HashMap<>();
		
		while (userIter.hasNext()) {
			User currUser = userIter.next();
			Integer score = calculateAnsSimilarity(currAns, currUser.getQuestionAnswers());
			
			if (!sortingMap.containsKey(score)) {
				sortingMap.put(score, createQueueAndAddUser(currUser));
			} else {
				sortingMap.get(score).add(currUser);
			}
			
		}
		
		return sortingMap;
	}
	
	private Integer calculateAnsSimilarity(List<Integer> ansUser, List<Integer> ansOtherUser) {
		Integer score = 0;
		
		for (int j = 0; j < ansUser.size(); ++j) {
			if (ansUser.get(j) == ansOtherUser.get(j)) {
				score++;
			}
		}

		return score;
	}
	
	private Queue<User> createQueueAndAddUser(User user) {
		Queue<User> tempQueue = new ArrayDeque<>();
		tempQueue.add(user);
		return tempQueue;
	}
	
}
