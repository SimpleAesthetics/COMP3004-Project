package com.simpleaesthetics.application.rest.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.Course;
import com.simpleaesthetics.application.utility.Transformer;

@Component
public class CourseTransformer {
	
	@Autowired
	Transformer utilsTransformer;

	public Course transformToCourse(ArrayList<String> courseInfo) {
		return new Course(
				courseInfo.get(1),
				courseInfo.get(3),
				utilsTransformer.transformCsvToStringList(courseInfo.get(4)));
	}
	
	public List<Course> transformToCourses(ArrayList<ArrayList<String>> coursesInfo) {
		List<Course> courses = new ArrayList<Course>();
		System.out.println(coursesInfo.size());
		for (ArrayList<String> courseInfo : coursesInfo) {
			System.out.println(courseInfo);
			courses.add(this.transformToCourse(courseInfo));
		}
		
		return courses;
	}
	
}
