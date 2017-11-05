package com.simpleaesthetics.application.rest.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class QuestionnaireTransformer {

	public HashMap<String, String> transformForDbString(Map<String, List<String>> questionnaire) {
		
		HashMap<String, String> transQuestionnaire = new HashMap<>();
		for (String question : questionnaire.keySet()) {
			List<String> answers = questionnaire.get(question);
			transQuestionnaire.put(
					question, 
					answers.toArray(new String[answers.size()]).toString());
		}
		
		return transQuestionnaire;
	}
	
public HashMap<String, String[]> transformForDbArray(Map<String, List<String>> questionnaire) {
		
		HashMap<String, String[]> transQuestionnaire = new HashMap<>();
		for (String question : questionnaire.keySet()) {
			List<String> answers = questionnaire.get(question);
			transQuestionnaire.put(
					question, 
					answers.toArray(new String[answers.size()]));
		}
		
		return transQuestionnaire;
	}
	
	public HashMap<String, List<String>> transformForModel(Map<String, String[]> questionnaire) {
		
		HashMap<String, List<String>> transQuestionnaire = new HashMap<>();
		for (String question : questionnaire.keySet()) {
			transQuestionnaire.put(
					question, 
					new ArrayList<String>(Arrays.asList(questionnaire.get(question))));
		}
		
		return transQuestionnaire;
	}
}
