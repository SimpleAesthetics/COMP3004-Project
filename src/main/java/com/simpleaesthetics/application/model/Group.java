package com.simpleaesthetics.application.model;

import java.util.Set;

public class Group {
	
	private String name;
	private String meetingLocation;
	private String taName;
	private Set<User> groupMembers;
	private Set<String> groupNames;
	
	public Group() {
		this.name = null;
		this.meetingLocation = null;
		this.groupMembers = null;
	}
	
	public Group(String name, String meetingLocation, Set<User> groupMembers) {
		this.name = name;
		this.meetingLocation = meetingLocation;
		this.groupMembers = groupMembers;
	}
	
	public Group(Set<User> groupMembers) {
		this.name = null;
		this.meetingLocation = null;
		this.groupMembers = null;
		this.groupMembers = groupMembers;
	}
	
	public Group(String name) {
		this.name = name;
		this.meetingLocation = null;
		this.groupMembers = null;
		this.groupMembers = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeetingLocation() {
		return meetingLocation;
	}

	public void setMeetingLocation(String meetingLocation) {
		this.meetingLocation = meetingLocation;
	}

	public Set<User> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(Set<User> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}
	
	public Set<String> getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(Set<String> groupNames) {
		this.groupNames = groupNames;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupMembers == null) ? 0 : groupMembers.hashCode());
		result = prime * result + ((meetingLocation == null) ? 0 : meetingLocation.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Group other = (Group) obj;
		if (groupMembers == null) {
			if (other.groupMembers != null)
				return false;
		} else if (!groupMembers.equals(other.groupMembers))
			return false;
		if (meetingLocation == null) {
			if (other.meetingLocation != null)
				return false;
		} else if (!meetingLocation.equals(other.meetingLocation))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Group [name=" + name + ", meetingLocation=" + meetingLocation + ", groupMembers=" + groupMembers + "]";
	}

	
}
