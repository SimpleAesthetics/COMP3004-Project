package com.simpleaesthetics.application.rest.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.simpleaesthetics.application.model.University;

@Component
public class UniversityTransformer {
	
	public List<University> transform(
			ArrayList<ArrayList<String>> toTransform) {
		
		List<University> transformed = new ArrayList<>(); 
		
		for (ArrayList<String> universityInfo : toTransform) {
			transformed.add(new University(
					universityInfo.get(1), 
					new ArrayList<String>(Arrays.asList(universityInfo.get(2).split(",")))));
		}
		
		return transformed;
	}
	
}
