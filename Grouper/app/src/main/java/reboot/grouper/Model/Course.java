package reboot.grouper.Model;

import java.util.ArrayList;

public class Course {

	String name;
	String courseCode;
	String instructor;
	ArrayList<String> envList;
	
	public Course() {
		this.name = null;
		this.courseCode = null;
		this.instructor = null;
		this.envList = null;
	}
	
	public Course(String name, String courseCode, String instructor, ArrayList<String> envList) {
		this.name = name;
		this.courseCode = courseCode;
		this.instructor = instructor;
		this.envList = envList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public ArrayList<String> getEnvList() {
		return envList;
	}

	public void setEnvList(ArrayList<String> envList) {
		this.envList = envList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courseCode == null) ? 0 : courseCode.hashCode());
		result = prime * result + ((envList == null) ? 0 : envList.hashCode());
		result = prime * result + ((instructor == null) ? 0 : instructor.hashCode());
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
		Course other = (Course) obj;
		if (courseCode == null) {
			if (other.courseCode != null)
				return false;
		} else if (!courseCode.equals(other.courseCode))
			return false;
		if (envList == null) {
			if (other.envList != null)
				return false;
		} else if (!envList.equals(other.envList))
			return false;
		if (instructor == null) {
			if (other.instructor != null)
				return false;
		} else if (!instructor.equals(other.instructor))
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
		return "{\"name\":\"" + name + "\", \"courseCode\":\"" + courseCode + "\", \"instructor\":\"" + instructor +"\",\"envList\":[]}";
	}
	
	
}
