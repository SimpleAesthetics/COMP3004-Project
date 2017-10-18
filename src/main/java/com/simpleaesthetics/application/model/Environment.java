package com.simpleaesthetics.application.model;

import java.util.List;
import java.util.Map;

public class Environment {
	
	private User owner;
	private String name;
	private Boolean privateEnv;
	private String password;
	private List<Group> groups;
	private List<User> users;
	private Map<String, List<String>> questionnaire = null;
	
	public Environment() {
		this.name = null;
		this.privateEnv = null;
		this.password = null;
		this.groups = null;
		this.users = null;
	}
	
	public Environment(String name, boolean privateEnv, String password, List<Group> groups) {
		this.name = name;
		this.privateEnv = privateEnv;
		this.password = password;
		this.groups = groups;
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
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + (privateEnv ? 1231 : 1237);
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
		if (groups == null) {
			if (other.groups != null)
				return false;
		} else if (!groups.equals(other.groups))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (privateEnv != other.privateEnv)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Environment [name=" + name + ", privateEnv=" + privateEnv + ", groups="
				+ groups + "]";
	}
	
	
	
}
