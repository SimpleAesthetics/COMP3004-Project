package com.simpleaesthetics.application.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Transformer {
	
	public List<String> transformCsvToStringList(String csv) {
		return new ArrayList<String>(Arrays.asList(csv.split(",")));
	}
	
	public List<String> tranformToStringList(List<Integer> listToTransform) {
		List<String> transformedList = new ArrayList<String>();
		for (Integer item : listToTransform) {
			transformedList.add(String.valueOf(item));
		}
		
		return transformedList;
	}
	
	public List<Integer> tranformToIntegerList(List<String> listToTransform) {
		List<Integer> transformedList = new ArrayList<Integer>();
		for (String item : listToTransform) {
			transformedList.add(Integer.valueOf(item));
		}
		
		return transformedList;
	}
	
}
