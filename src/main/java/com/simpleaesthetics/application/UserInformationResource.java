package com.simpleaesthetics.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class UserInformationResource {

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody UserInformation sayHello(@RequestHeader(value="testValue") String testValue) {
		UserInformation userInfo = new UserInformation("Jon", "Bimm", "jbon", testValue);
		return userInfo;
	}
	
	
	
}
