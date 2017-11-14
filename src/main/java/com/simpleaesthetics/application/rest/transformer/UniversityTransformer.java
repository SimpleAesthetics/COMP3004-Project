package com.simpleaesthetics.application.rest.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.University;

@Component
public class UniversityTransformer {
	
	public University transformToUniversity(ArrayList<String> universityInfo) {
		System.out.println(universityInfo);
		
		return new University(
				universityInfo.get(1), 
				new ArrayList<String>(Arrays.asList(universityInfo.get(2).split(","))));
	}
	
	public List<University> transformToUniversities(
			ArrayList<ArrayList<String>> toTransform) {
		
		List<University> transformed = new ArrayList<>(); 
		
		for (ArrayList<String> universityInfo : toTransform) {
			transformed.add(transformToUniversity(universityInfo));
		}
		
		return transformed;
	}
	
}
