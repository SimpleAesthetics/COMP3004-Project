package com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Environment {
	
	private User owner;
	private String name;
	private Boolean privateEnv;
	private String password;
	private Integer maxGroupSize;
	public Date deadline;
	private SimpleDateFormat dateFormat;
	public 	Set<Group> groups;
	public  Set<User> users;
	private Map<String, List<String>> questionnaire;
	public	boolean formed;
	public Environment() {
		this.name = null;
		this.privateEnv = null;
		this.password = null;
		this.maxGroupSize = null;
		this.groups = new HashSet<Group>();
		this.users = new HashSet<User>();
		this.questionnaire = new HashMap<>();
		this.deadline = null;
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public Environment(String name, boolean privateEnv, String password, Integer maxGroupSize) {
		this.name = name;
		this.privateEnv = privateEnv;
		this.password = password;
		this.maxGroupSize = maxGroupSize;
		this.groups = new HashSet<Group>();
		this.users = new HashSet<User>();
		this.questionnaire = new HashMap<>();
		this.deadline = null;
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public Environment(String name, boolean privateEnv, String password, Integer maxGroupSize, Set<Group> groups, Set<User> users) {
		this.name = name;
		this.privateEnv = privateEnv;
		this.password = password;
		this.maxGroupSize = maxGroupSize;
		this.groups = groups;
		this.users = users;
		this.questionnaire = new HashMap<>();
		this.deadline = null;
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isPrivateEnv() {
		return privateEnv;
	}
	public void setPrivateEnv(boolean privateEnv) {
		this.privateEnv = privateEnv;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public boolean addQuestionAndAnswers(String question, String[] answers) {
		if (answers.length < 4) {
			questionnaire.put(question, createQuestionArray(answers));
			return true;
		}
		
		return false;
	}
	
	private ArrayList<String> createQuestionArray(String[] answers) {
		ArrayList<String> ansArray = new ArrayList<>();
		for (String answer : answers) {
			ansArray.add(answer);
		}
		
		return ansArray;
	}
	
	public Map<String, List<String>> getQuestionnaire() {
		return this.questionnaire;
	}
	
	public boolean setDate(String day, String month, String year) {
		try {
			this.deadline = dateFormat.parse(day+"/"+month+"/"+year);
			
		} catch (ParseException e) {
			return false;
		}
		
		return true;
	}
	
	public boolean afterDate(String day, String month, String year) {
		boolean isAfterDate = false;
		try {
			Date currDate = dateFormat.parse(day+"/"+month+"/"+year);
			isAfterDate = this.deadline.after(currDate);
			
		} catch (ParseException e) {
			return false;
		}
		
		return isAfterDate;
	}
	
	public Date getDeadline() {
		return this.deadline;
	}

	public Integer getMaxGroupSize() {
		return maxGroupSize;
	}

	public void setMaxGroupSize(Integer maxGroupSize) {
		this.maxGroupSize = maxGroupSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((privateEnv == null) ? 0 : privateEnv.hashCode());
		result = prime * result + ((questionnaire == null) ? 0 : questionnaire.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Environment other = (Environment) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (privateEnv == null) {
			if (other.privateEnv != null)
				return false;
		} else if (!privateEnv.equals(other.privateEnv))
			return false;
		if (questionnaire == null) {
			if (other.questionnaire != null)
				return false;
		} else if (!questionnaire.equals(other.questionnaire))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Environment [owner=" + owner + ", name=" + name + "]";
	}
	
	
	
}
