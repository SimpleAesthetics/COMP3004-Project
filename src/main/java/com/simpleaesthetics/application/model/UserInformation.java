package com.simpleaesthetics.application.model;

public class UserInformation {
	private String firstName;
	private String lastName;
	private String nickname;
	private String number;
	
	
	public UserInformation(String firstName, String lastName, String nickname, String number) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickname = nickname;
		this.number = number;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
}
