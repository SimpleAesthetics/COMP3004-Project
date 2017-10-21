package com.simpleaesthetics.application.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Environment {
	
	private User owner;
	private String name;
	private Boolean privateEnv;
	private String password;
	private Set<Group> groups;
	private Set<User> users;
	private Map<String, List<String>> questionnaire = null;
	
	public Environment(String name, boolean privateEnv, String password, Set<Group> groups, Set<User> users) {
		this.name = name;
		this.privateEnv = privateEnv;
		this.password = password;
		this.groups = groups;
		this.users = users;
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

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		return true;
	}

	@Override
	public String toString() {
		return "Environment [name=" + name + ", privateEnv=" + privateEnv + ", groups="
				+ groups + "]";
	}
	
	
	
}
