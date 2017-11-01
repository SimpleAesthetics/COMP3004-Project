package com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model;

import java.util.ArrayList;
import java.util.List;

public class University {
	
	private String name;
	
	// TODO Make a values for this var
	public List<Course> coursesList;
	
	public University(String name) {
		this.name = name;
		this.coursesList = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		University other = (University) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "University [name=" + name + "]";
	}
	
	
}
