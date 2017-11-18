package reboot.grouper.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Course {
    @SerializedName("name")
    @Expose
	String name;

    @SerializedName("courseCode")
    @Expose
	String courseCode;

    @SerializedName("university")
    @Expose
	String university;

    @SerializedName("envList")
    @Expose
	List<String> envList;
	
	public Course() {
		this.name = null;
		this.courseCode = null;
		this.envList = null;
	}
	
	public Course(String name, String university, List<String> envList) {
		this.name = name;
		this.university = university;
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

	public List<String> getEnvList() {
		return envList;
	}

	public void setEnvList(ArrayList<String> envList) {
		this.envList = envList;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courseCode == null) ? 0 : courseCode.hashCode());
		result = prime * result + ((envList == null) ? 0 : envList.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((university == null) ? 0 : university.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (university == null) {
			if (other.university != null)
				return false;
		} else if (!university.equals(other.university))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Course [name=" + name + ", courseCode=" + courseCode + ", university=" + university + ", envList="
				+ envList + "]";
	}
	
	
}
