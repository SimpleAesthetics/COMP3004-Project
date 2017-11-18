package reboot.grouper.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class University {


	@SerializedName("name")
	@Expose
	private String name;
	// TODO Make a values for this var

	@SerializedName("coursesList")
	@Expose
	private List<String> coursesList;
	
	public University() {
		this.name = null;
		this.coursesList = new ArrayList<>();
	}
	
	public University(String name) {
		this.name = name;
		this.coursesList = null;
	}
	
	public University(String name, List<String> courses) {
		this.name = name;
		this.coursesList = courses;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCoursesList() {
		return coursesList;
	}

	public void setCoursesList(List<String> coursesList) {
		this.coursesList = coursesList;
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
