package com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model;

public class UserInformation {
	private int studentNumber;
	private String firstName;
	private String lastName;
	private String nickname;
	private String email;
	
	public UserInformation() {
		this.studentNumber = -1;
		this.firstName = null;
		this.lastName = null;
		this.nickname = null;
		this.email = null;
	}
	
	public UserInformation(int studentNumber, 
			String firstName, 
			String lastName, 
			String nickname,  
			String email) {
		
		this.studentNumber = studentNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickname = nickname;
		this.email = email;
	}

	public int getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + studentNumber;
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
		UserInformation other = (UserInformation) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (studentNumber != other.studentNumber)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserInformation [studentNumber=" + studentNumber + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", nickname=" + nickname + ", email=" + email + "]";
	}
	
	
}
