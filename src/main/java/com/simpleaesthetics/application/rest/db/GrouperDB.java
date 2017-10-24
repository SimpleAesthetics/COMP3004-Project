package com.simpleaesthetics.application.rest.db;
/* Grouper database connector class
 * Used to create an interface between the Grouper application and the underlying database
 * At present, Grouper uses SQLite as its DB engine
 * Written by Brad Hunter, part of the SimpleAesthetics dev team
 * Includes "sqlite-jdbc" external library, written by Taro L. Saito:
 * https://bitbucket.org/xerial/sqlite-jdbc/downloads/
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class GrouperDB {
	
	static Logger logger = Logger.getLogger(GrouperDB.class.getName());
	
	//Declaration of stuff related to DB
	private String dburl;
	private Connection dbconn;
	private boolean opened;
	
	@PostConstruct
	public void postConstruct() {
		logger.info("Creating DB instance");
		if (!openDB()) {
			throw new DatabaseException("Failed to OPEN the database");
		}
	}
	
	@PreDestroy
	public void preDestroy() {
		logger.info("Destroying DB instance");
		if (!closeDB()) {
			throw new DatabaseException("Failed to CLOSE the database");
		}
	}
	
	//Base constructor, takes no input, assumes DB located where the application is
	public GrouperDB() {
		dburl = "jdbc:sqlite:grouperdb.db";
		dbconn = null;
		opened = false;
	}
	
	//Constructor, takes in a path to the database
	public GrouperDB(String url) {
		dburl = "jdbc:sqlite:" + url;
		dbconn = null;
		opened = false;
	}
	
	//Function for changing the path to the database
	public void changeURL(String url) {
		dburl = "jdbc:sqlite:" + url;
	}
	
	//Function for getting the open state of the database
	public boolean getState() {
		return opened;
	}
	
	//Function for opening the database
	public boolean openDB() {
		try {
			//Open a connection
			dbconn = DriverManager.getConnection(dburl);
			//If we get to here, it is safe to assume the database has been opened
			opened = true;
			return true;
		}
		catch(SQLException e) {
			//Database has not been opened
			return false;
		}
	}
	
	//Function for closing the database
	public boolean closeDB() {
		if(opened == true && dbconn != null) {
			try {
				//Attempt to close the database
				dbconn.close();
				//Safe to assume it has been closed at this point
				opened = false;
				return true;
			}
			catch(SQLException e) {
				//Did not close DB
				return false;
			}
		}
		//Database was never opened
		return true;
	}
	
	//Function for inserting a new university
	//Input: university name
	//Output: ID of the inserted university
	public int insertUniversity(String name) {
		return this.insertUniversity(name,new int[0]);
	}
	
	//Function for inserting a new university
	//OVERLOAD: same as above, but user specifies the list of courses
	public int insertUniversity(String name, int[] courses) {
		//Combine all courses into a string
		String courselist = Arrays.toString(courses);
		//Remove brackets from the string
		courselist = courselist.replace("[","");
		courselist = courselist.replace("]","");
		
		if(opened != false && dbconn != null) {
			//Write SQL statement
			String sql = "INSERT INTO Universities(name,courses) VALUES (?,?)";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setString(1,name);
				psql.setString(2,courselist);
				//Execute statement
				psql.executeUpdate();
				
			}
			catch(SQLException e) {
				//Some kind of error, assume university was not added
				logger.error("Failed to insert a University", e);
				return -1;
			}
		}
		//Get the ID of the recently added university
		return this.getUniversityID(name);
	}
	
	//Function for modifying the list of courses in a university
	public boolean updateUniversity(int id, String courses) {
		if(opened == false || dbconn == null) {
			//DB isn't open
			return false;
		}
		else {
			//Get the data for the university
			ArrayList<String> university = this.queryUniversity(id);
			//Get list of courses already added
			String existingcourses = university.get(1);
			//Add the new list of courses
			if(existingcourses != "") {
				existingcourses += ", ";
			}
			existingcourses += courses;
			//Write SQL statement
			String sql = "UPDATE Universities SET courses = ? where id = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Set variables
				psql.setString(1,existingcourses);
				psql.setInt(2,id);
				//Execute statement
				psql.executeUpdate();
				//No way to check if succeeded, so assume it did
				return true;
			}
			catch(SQLException e) {
				//Did not succeed
				return false;
			}
		}
	}
	
	//Function for updating course list in university
	//OVERLOAD: takes an array of integers and converts it to a string, then feeds it to above function
	public boolean updateUniversity(int id, int[] courses) {
		//Combine all courses into a string
		String courselist = Arrays.toString(courses);
		//Remove brackets from the string
		courselist = courselist.replace("[","");
		courselist = courselist.replace("]","");
		//Return result of add using function above
		return this.updateUniversity(id,courselist);
	}
	
	//Function for inserting a single course into a university (uses above function)
	public boolean addCourse(int id, int course) {
		int[] courses = {course};
		return this.updateUniversity(id,courses);
	}
	
	//Function for querying a university
	//Input: ID of university to query
	//Output: list of parameters for that university
	public ArrayList<String> queryUniversity(int id) {
		//Make list that stores data
		ArrayList<String> courses = new ArrayList<String>();
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT Name,Courses FROM Universities WHERE id = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setInt(1,id);
				//Execute statement
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					//Add results to course list
					courses.add(results.getString("Name"));
					courses.add(results.getString("Courses"));
					//Exit, since we only want the first result; assume first result is the one we want
					break;
				}
			}
			catch(SQLException e) {
				//Didn't work
				return courses;
			}
		}
		return courses;
	}
	
	/**
	 * Alternate version of the queryUniversity from above.
	 * This is very similar to the function below. 
	 */
	public ArrayList<ArrayList<String>> queryUniversity(String name) {
		//Make list that stores data
		ArrayList<ArrayList<String>> courses = new ArrayList<ArrayList<String>>();
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT Name,Courses FROM Universities WHERE Name = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setString(1, name);
				//Execute statement
				ResultSet result = psql.executeQuery();
				
				// Obtain the matching query
				// Assumes that University names are unique
				ArrayList<String> currCourse = new ArrayList<String>(); 
				currCourse.add(result.getString("Name"));
				currCourse.add(result.getString("Courses"));
				
				// Add the course to the courses list for consistency
				courses.add(currCourse);
				
			}
			catch(SQLException e) {
				//Didn't work
				return courses;
			}
		}
		return courses;
	}
	
	//Helper function for getting the university ID
	//This is only useful for obtaining the last added university
	public int getUniversityID(String name) {
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT id FROM Universities WHERE name = ? ORDER BY id DESC LIMIT 1";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setString(1,name);
				//Execute statement
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					//Since the one we want is the only thing here, just return it
					return(results.getInt("id"));
				}
				//If we get here, it wasn't found
				return -1;
			}
			catch(SQLException e) {
				//Didn't work
				return -1;
			}
		}
		//DB not opened
		return -1;
	}
	
	//Function for getting all universities
	//Outputs a list of universities, whose data is all in its own list
	public ArrayList<ArrayList<String>> queryAllUniversities() {
		//Make list for storage
		ArrayList<ArrayList<String>> courses = new ArrayList<ArrayList<String>>();
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT ID,Name,Courses FROM Universities";
			try {
				//Create statement
				Statement query = dbconn.createStatement();
				//Execute query
				ResultSet results = query.executeQuery(sql);
				ArrayList<String> result;
				
				while(results.next()) {
					//Get the results
					result = new ArrayList<String>();
					result.add(new Integer(results.getInt("ID")).toString());
					result.add(results.getString("Name"));
					result.add(results.getString("Courses"));
					//Add the results to the list
					courses.add(result);
				}
			}
			catch(SQLException e) {
				//Some kind of error
				return courses;
			}
		}
		return courses;
	}
	
	//Function for removing a university
	public boolean deleteUniversity(int id) {
		if(opened == false || dbconn == null || this.queryUniversity(id).isEmpty() == true) {
			//DB isn't open or university doesn't exist
			return false;
		}
		else {
			//Write SQL
			String sql = "DELETE FROM Universities WHERE id = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setInt(1,id);
				//Execute statement
				psql.executeUpdate();
				//Make sure it was deleted
				return this.queryUniversity(id).isEmpty();
			}
			catch(SQLException e) {
				//Some error occurred
				return false;
			}
		}
	}
	
	//Function for inserting a user
	//Input: first name, last name, email address
	//Output: ID of added user
	public int insertUser(int id, String firstName, String lastName, String email) {
		//For now, assume the nickname is the email address
		String nickname = email;
		//Convert to all lowercase letters
		nickname = nickname.toLowerCase();
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "INSERT INTO Users(id,firstname,lastname,nickname,email) VALUES (?,?,?,?,?)";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setInt(1, id);
				psql.setString(2,firstName);
				psql.setString(3,lastName);
				psql.setString(4,nickname);
				psql.setString(5,email);
				//Execute statement
				psql.executeUpdate();
				
			}
			catch(SQLException e) {
				//Some kind of error, assume user was not added
				return -1;
			}
			//Return the ID of the newly-added user
			return this.getUserID(nickname);
		}
		//DB isn't open
		return -1;
	}
	
	//Function for updating a user's email address
	public boolean updateUser(int id, String email) {
		if(opened == false || dbconn == null) {
			//DB isn't open
			return false;
		}
		else {
			//Write SQL
			String sql = "UPDATE Users SET email = ? where id = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Set variables
				psql.setString(1,email);
				psql.setInt(2,id);
				//Execute statement
				psql.executeUpdate();
				//No way to check for success, so assume it did
				return true;
			}
			catch(SQLException e) {
				//Some kind of error, assume it didn't succeed
				return false;
			}
		}
	}
	
	//Function for getting a user's details based on their nickname
	public ArrayList<String> queryUser(String nickname) {
		//Make list
		ArrayList<String> users = new ArrayList<String>();
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT id,firstname,lastname,email FROM Users WHERE nickname = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setString(1,nickname);
				//Execute statement
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					//Add details
					users.add(new Integer(results.getInt("id")).toString());
					users.add(results.getString("firstname"));
					users.add(results.getString("lastname"));
					users.add(results.getString("email"));
					//Assume the first value is the only value and exit
					break;
				}
			}
			catch(SQLException e) {
				//Error occurred
				return users;
			}
		}
		//DB isn't open
		return users;
	}
	
	//Function for querying for user details based on the user's ID
	public ArrayList<String> queryUser(int id) {
		//Make list
		ArrayList<String> users = new ArrayList<String>();
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT firstname,lastname,nickname,email FROM Users WHERE id = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setInt(1,id);
				//Execute statement
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					//Add details
					users.add(results.getString("firstname"));
					users.add(results.getString("lastname"));
					users.add(results.getString("nickname"));
					users.add(results.getString("email"));
					//Assume the first value is the only value and exit
					break;
				}
			}
			catch(SQLException e) {
				//Error occurred
				return users;
			}
		}
		//DB isn't open
		return users;
	}
	
	//Helper function for obtaining the ID of the most recently added user
	public int getUserID(String name) {
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT ID FROM Users WHERE nickname = ? ORDER BY id DESC LIMIT 1";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setString(1,name);
				//Execute statement
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					//Since there is only one result, it is safe to return it
					return results.getInt("id");
				}
			}
			catch(SQLException e) {
				//Error occurred
				return -1;
			}
		}
		//DB not open
		return -1;
	}
	
	//Function for getting all users
	public ArrayList<ArrayList<String>> queryAllUsers() {
		//Make list
		ArrayList<ArrayList<String>> users = new ArrayList<ArrayList<String>>();
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT id,firstname,lastname,nickname,email FROM Users";
			try {
				//Create statement
				Statement query = dbconn.createStatement();
				//Execute statement
				ResultSet results = query.executeQuery(sql);
				ArrayList<String> result;
				
				while(results.next()) {
					//Create list of results
					result = new ArrayList<String>();
					result.add(new Integer(results.getInt("ID")).toString());
					result.add(results.getString("firstname"));
					result.add(results.getString("lastname"));
					result.add(results.getString("nickname"));
					//Add results to list of users
					result.add(results.getString("email"));
					users.add(result);
				}
			}
			catch(SQLException e) {
				//Error occurred
				return users;
			}
		}
		//Return list of users
		return users;
	}
	
	//Function for removing a user from the system
	public boolean deleteUser(int id) {
		if(opened == false || dbconn == null) {
			//DB isn't open
			return false;
		}
		else {
			//Write SQL
			String sql = "DELETE FROM Users WHERE id = ?";
			try {
				//prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setInt(1,id);
				//Execute statement
				psql.executeUpdate();
				//Verify the user was actually deleted
				return this.queryUser(id).isEmpty();
			}
			catch(SQLException e) {
				//Error occurred
				return false;
			}
		}
	}
	
	//Function for inserting a course into the database
	//OVERLOAD: does not provide a list of environments
	public int insertCourse(String name,int instructor,int university) {
		return this.insertCourse(name,instructor,university,new int[0]);
	}
	
	//Function for inserting a course into the database
	//Input: name, instructor ID, university ID, list of enclosed environments
	//Output: ID of the created course
	public int insertCourse(String name, int instructor, int university, int[] environmentlist) {
		//Combine all courses into a string
		String environments = Arrays.toString(environmentlist);
		int id = -1;
		//Remove brackets from the string
		environments = environments.replace("[","");
		environments = environments.replace("]","");
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "INSERT INTO Courses(name,instructor,university,environments) VALUES (?,?,?,?)";
			try {
				//Prepare statements
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setString(1,name);
				psql.setInt(2,instructor);
				psql.setInt(3,university);
				psql.setString(4,environments);
				//Execute statement
				psql.executeUpdate();
				//Get the course ID
				id = this.getCourseID(name,university);
				//Get the containing university
				ArrayList<String> universities = this.queryUniversity(university);
				//Get existing course list from university
				String clist = universities.get(1);
				//Add new course ID to course list
				if(clist != "") {
					clist += ", ";
				}
				clist += new Integer(id).toString();
				//Update the university (if it fails, we don't care)
				this.updateUniversity(this.getUniversityID(universities.get(0)),clist);
			}
			catch(SQLException e) {
				//Some error occurred
				return -1;
			}
		}
		//Return the created ID
		return id;
	}
	
	//Function for adding to the environment list of a course
	public boolean updateCourse(int id, String environmentlist) {
		if(opened == false || dbconn == null) {
			//DB isn't open
			return false;
		}
		else {
			//Get existing environment list
			ArrayList<String> course = this.queryCourse(id);
			String elist = course.get(3);
			//Add new environment list to existing list
			if(elist != "") {
				elist += ", ";
			}
			elist += environmentlist;
			//Write SQL
			String sql = "UPDATE Courses SET environments = ? where id = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setString(1,elist);
				psql.setInt(2,id);
				//Execute statement
				psql.executeUpdate();
				//No way to check if update took place, so assume it did
				return true;
			}
			catch(SQLException e) {
				//Some error occurred
				return false;
			}
		}
	}
	
	//Function for updating list of environments in a course
	//OVERLOAD: simply converts list of environments to a string representation
	public boolean updateCourse(int id, int[] environmentlist) {
		//Combine all courses into a string
		String environments = Arrays.toString(environmentlist);
		//Remove brackets from the string
		environments = environments.replace("[","");
		environments = environments.replace("]","");
		return this.updateCourse(id,environments);
	}
	
	//Function for changing the instructor of a course
	public boolean updateCourse(int id, int instructor) {
		if(opened == false || dbconn == null || this.queryUser(instructor).isEmpty() == true) {
			//DB not opened or invalid instructor ID supplied
			return false;
		}
		else {
			//Write SQL
			String sql = "UPDATE Courses SET instructor = ? where id = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setInt(1,instructor);
				psql.setInt(2,id);
				//Execute statement
				psql.executeUpdate();
				//No way to check if update worked, so assume it did
				return true;
			}
			catch(SQLException e) {
				//Some error occurred
				return false;
			}
		}
	}
	
	//Function for getting list of courses associated with a university
	public ArrayList<String> getCourses(int uid) {
		//Make list
		ArrayList<String> courses = new ArrayList<String>();
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT ID,Name,Instructor,Environments FROM Courses WHERE university = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setInt(1,uid);
				//Execute statement
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					//Add results to course
					courses.add(new Integer(results.getInt("ID")).toString());
					courses.add(results.getString("Name"));
					courses.add(new Integer(results.getInt("Instructor")).toString());
					courses.add(results.getString("Environments"));
					//Since that should be the only result, it is safe to exit
					break;
				}
			}
			catch(SQLException e) {
				//Some error occurred
				return courses;
			}
		}
		//Return course information
		return courses;
	}
	
	//Function for getting course information
	public ArrayList<String> queryCourse(int id) {
		//Make list
		ArrayList<String> courses = new ArrayList<String>();
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT Name,Instructor,University,Environments FROM Courses WHERE id = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setInt(1,id);
				//Execute statement
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					//Add results to course
					courses.add(results.getString("Name"));
					courses.add(new Integer(results.getInt("Instructor")).toString());
					courses.add(new Integer(results.getInt("University")).toString());
					courses.add(results.getString("Environments"));
					//Since that should be the only result, it is safe to exit
					break;
				}
			}
			catch(SQLException e) {
				//Some error occurred
				return courses;
			}
		}
		//Return course information
		return courses;
	}
	
	//Helper function for getting the ID of the most recently added course
	public int getCourseID(String name, int uid) {
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT ID FROM Courses WHERE name = ? AND university = ? ORDER BY id DESC LIMIT 1";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setString(1,name);
				psql.setInt(2,uid);
				//Execute statement
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					//Since there should only be one, it is safe to simply return it
					return (results.getInt("ID"));
				}
			}
			catch(SQLException e) {
				//Error occurred
				return -1;
			}
		}
		//DB isn't open
		return -1;
	}
	
	//Function for getting information for all courses
	public ArrayList<ArrayList<String>> queryAllCourses() {
		//Make list
		ArrayList<ArrayList<String>> courses = new ArrayList<ArrayList<String>>();
		if(opened != false && dbconn != null) {
			//Write SQL
			String sql = "SELECT ID,Name,Instructor,University,Environments FROM Courses";
			try {
				//Create statement
				Statement query = dbconn.createStatement();
				//Execute statement
				ResultSet results = query.executeQuery(sql);
				ArrayList<String> result;
				
				while(results.next()) {
					//Make list of results
					result = new ArrayList<String>();
					result.add(new Integer(results.getInt("ID")).toString());
					result.add(results.getString("Name"));
					result.add(new Integer(results.getInt("Instructor")).toString());
					result.add(new Integer(results.getInt("University")).toString());
					result.add(results.getString("Environments"));
					//Add results to list
					courses.add(result);
				}
			}
			catch(SQLException e) {
				//Some error occurred
				return courses;
			}
		}
		//Return the list of courses
		return courses;
	}
	
	//Function for removing a course
	public boolean deleteCourse(int id) {
		//Get list of all universities
		ArrayList<ArrayList<String>> universities = this.queryAllUniversities();
		for(ArrayList<String> x : universities) {
			//Get list of courses from university
			ArrayList<String> y = new ArrayList<String>(Arrays.asList(x.get(2).split(", ")));
			//Remove the course that is being deleted
			y.remove(new Integer(id).toString());
			//Update list of courses
			String z = y.toArray().toString();
			z = z.replace("[","");
			z = z.replace("]","");
			this.updateUniversity(Integer.parseInt(x.get(0)),z);
		}
		if(opened == false || dbconn == null) {
			//DB isn't open
			return false;
		}
		else {
			//Write SQL
			String sql = "DELETE FROM Courses WHERE id = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setInt(1,id);
				//Execute statement
				psql.executeUpdate();
				//Verify that the course was deleted
				return this.queryCourse(id).isEmpty();
			}
			catch(SQLException e) {
				//Some error occurred
				return false;
			}
		}
	}
	
	//Function to insert environment to the database
	//OVERLOAD, contains no student list, group list, or questionnaire
	public int insertEnvironment(String name, int cid, boolean priv, String password, int mgs, String close) {
		return this.insertEnvironment(name,cid,priv,password,mgs,close,new int[0],new int[0],-1);
	}
	//Function to insert environment to the database
	//OVERLOAD, converts integer arrays to strings
	public int insertEnvironment(String name, int cid, boolean priv, String password, int mgs, String close, int[] students, int[] groups, int questionnaire) {
		//Combine all students into a string
		String s = Arrays.toString(students);
		//Remove brackets from the string
		s = s.replace("[","");
		s = s.replace("]","");
		//Combine all students into a string
		String g = Arrays.toString(groups);
		//Remove brackets from the string
		g = g.replace("[","");
		g = g.replace("]","");
		return this.insertEnvironment(name,cid,priv,password,mgs,close,s,g,questionnaire);
	}

	//Function to insert environment to the database
	/*Input: name, course ID, instructor ID, private?, password (if applicable),
	 * maximum group size, closing date, list of students, list of groups
	 * reference to associated questionnaire */
	//Output: the ID of the created environment
	public int insertEnvironment(String name, int cid, boolean priv, String password, int mgs, String close, String students, String groups, int questionnaire) {
		//Basic sanity check
		if(this.queryCourse(cid).isEmpty() || name == "" || (priv == true && password == "") || mgs < 2 || close == "") {
			return -1;
		}
		int pvt = 0;
		if(priv == true) {
			pvt = 1;
		}
		int id = -1;
		if(opened != false && dbconn != null) {
			//Database is opened, can insert into DB now
			String sql = "INSERT INTO Environments(name,course,private,password,maxgroupsize,closedate,students,groups,questionnaire) VALUES (?,?,?,?,?,?,?,?,?)";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setString(1,name);
				psql.setInt(2,cid);
				psql.setInt(3,pvt);
				psql.setString(4,password);
				psql.setInt(5,mgs);
				psql.setString(6,close);
				psql.setString(7,students);
				psql.setString(8,groups);
				psql.setInt(9,questionnaire);
				psql.executeUpdate();
				id = this.getEnvironmentID(name,cid);
				ArrayList<String> courses = this.queryCourse(cid);
				if(courses.get(3) != "") {
					courses.set(3,courses.get(3) + ", ");
				}
				courses.set(3,courses.get(3) + new Integer(id).toString());
				this.updateCourse(cid,courses.get(3));
				
			}
			catch(SQLException e) {
				return -1;
			}
		}
		return id;
	}
	
	//Helper function for retrieving the ID of an environment
	//Input: env name and ID of course it belongs to
	//Output: environment ID
	public int getEnvironmentID(String name, int courseID) {
		ArrayList<Integer> envs = new ArrayList<Integer>();
		if(opened != false && dbconn != null) {
			String sql = "SELECT ID FROM Environments WHERE name = ? AND course = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setString(1,name);
				psql.setInt(2, courseID);
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					envs.add(new Integer(results.getInt("ID")));
				}
			}
			catch(SQLException e) {
				return -1;
			}
		}
		if(envs.isEmpty() == true) {
			return -1;
		}
		else {
			return envs.get(0);
		}
	}
	//Function to update environment details
	//Input: env ID, closing date
	//Output: true on success, false otherwise
	public boolean updateEnvironment(int id, String close) {
		if(opened == false || dbconn == null || close == "") {
			return false;
		}
		else {
			String sql = "UPDATE Environments SET closedate = ? where id = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setString(1,close);
				psql.setInt(2,id);
				psql.executeUpdate();
				return true;
			}
			catch(SQLException e) {
				return false;
			}
		}
	}
	//Function to update environment details
	//Input: env ID, list of students, list of groups
	//Output: true on success, false otherwise
	public boolean updateEnvironment(int id, String students, String groups) {
		ArrayList<String> env = this.queryEnvironment(id);
		if(env.get(6).length() > 0) {
			students = ", " + students;
		}
		if(env.get(7).length() > 0) {
			groups = ", " + groups;
		}
		env.set(6,env.get(6) + students);
		env.set(7,env.get(7) + groups);
		if(opened == false || dbconn == null) {
			return false;
		}
		else {
			String sql = "UPDATE Environments SET students = ? and groups = ? where id = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setString(1,env.get(6));
				psql.setString(2,env.get(7));
				psql.setInt(3,id);
				psql.executeUpdate();
				return true;
			}
			catch(SQLException e) {
				return false;
			}
		}
	}
	//Function to update environment details
	//Input: env ID, private flag, password
	//Output: true on success, false otherwise
	public boolean updateEnvironment(int id, boolean priv, String password) {
		int prv = 0;
		if(priv == true) {
			prv = 1;
		}
		if(opened == false || dbconn == null || password == "") {
			return false;
		}
		else {
			String sql = "UPDATE Environments SET private = ? and password = ? where id = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setInt(1,prv);
				psql.setString(2,password);
				psql.setInt(3,id);
				psql.executeUpdate();
				return true;
			}
			catch(SQLException e) {
				return false;
			}
		}
	}
	//Function to update environment details
	//Input: env ID, maximum group size
	//Output: true on success, false otherwise
	public boolean updateEnvironment(int id, int mgs) {
		if(opened == false || dbconn == null || mgs < 2) {
			return false;
		}
		else {
			String sql = "UPDATE Environments SET maxgroupsize = ? where id = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setInt(1,mgs);
				psql.setInt(2,id);
				psql.executeUpdate();
				return true;
			}
			catch(SQLException e) {
				return false;
			}
		}
	}
	//Function to update environment details
	//Input: env ID, maximum group size
	//Output: true on success, false otherwise
	public boolean changeEnvName(int id, String name) {
		if(opened == false || dbconn == null || name == "") {
			return false;
		}
		else {
			String sql = "UPDATE Environments SET name = ? where id = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setString(1,name);
				psql.setInt(2,id);
				psql.executeUpdate();
				return true;
			}
			catch(SQLException e) {
				return false;
			}
		}
	}
	//Function to update environment details
	//Input: env ID, ID of the questionnaire
	//Output: true on success, false otherwise
	public boolean changeQuestionnaire(int id, int qid) {
		if(opened == false || dbconn == null || this.queryQuestionnaire(qid) == false) {
			return false;
		}
		else {
			String sql = "UPDATE Environments SET questionnaire = ? where id = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setInt(1,qid);
				psql.setInt(2,id);
				psql.executeUpdate();
				return true;
			}
			catch(SQLException e) {
				return false;
			}
		}
	}
	//Function to get all environments associated with a course
	//Input: course ID
	//Output: list of environments, each in the form of another list
	public ArrayList<ArrayList<String>> getEnvironments(int cid) {
		ArrayList<ArrayList<String>> envs = new ArrayList<ArrayList<String>>();
		if(opened != false && dbconn != null) {
			String sql = "SELECT id,name,private,password,maxgroupsize,closedate,students,groups,questionnaire FROM Environments WHERE course = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setInt(1,cid);
				ResultSet results = psql.executeQuery();
				ArrayList<String> result;
				
				while(results.next()) {
					result = new ArrayList<String>();
					result.add(new Integer(results.getInt("id")).toString());
					result.add(results.getString("name"));
					result.add(new Integer(results.getInt("private")).toString());
					result.add(results.getString("password"));
					result.add(new Integer(results.getInt("maxgroupsize")).toString());
					result.add(results.getString("closedate"));
					result.add(results.getString("students"));
					result.add(results.getString("groups"));
					result.add(new Integer(results.getInt("questionnaire")).toString());
					envs.add(result);
				}
			}
			catch(SQLException e) {
				envs.add(new ArrayList<String>(9));
			}
		}
		return envs;
	}
	//Function for querying an environment
	//Input: environment ID
	//Output: list of all details the database contains about that environment
	public ArrayList<String> queryEnvironment(int id) {
		ArrayList<ArrayList<String>> envs = new ArrayList<ArrayList<String>>();
		if(opened != false && dbconn != null) {
			String sql = "SELECT name,course,private,password,maxgroupsize,closedate,students,groups,questionnaire FROM Environments WHERE id = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setInt(1,id);
				ResultSet results = psql.executeQuery();
				ArrayList<String> result;
				
				while(results.next()) {
					result = new ArrayList<String>();
					result.add(results.getString("name"));
					result.add(new Integer(results.getInt("course")).toString());
					result.add(new Integer(results.getInt("private")).toString());
					result.add(results.getString("password"));
					result.add(new Integer(results.getInt("maxgroupsize")).toString());
					result.add(results.getString("closedate"));
					result.add(results.getString("students"));
					result.add(results.getString("groups"));
					result.add(new Integer(results.getInt("questionnaire")).toString());
					envs.add(result);
				}
			}
			catch(SQLException e) {
				ArrayList<String> errorlist = new ArrayList<String>();
				for(int i = 0; i < 9; i++) {
					errorlist.add("");
				}
				envs.add(errorlist);
			}
		}
		if(envs.isEmpty() == true) {
			return new ArrayList<String>();
		}
		else {
			return envs.get(0);
		}
	}
	
	public ArrayList<ArrayList<String>> queryAllEnvironments() {
		ArrayList<ArrayList<String>> envs = new ArrayList<ArrayList<String>>();
		if(opened != false && dbconn != null) {
			String sql = "SELECT id,name,course,private,password,maxgroupsize,closedate,students,groups,questionnaire FROM Environments";
			try {
				Statement query = dbconn.createStatement();
				ResultSet results = query.executeQuery(sql);
				ArrayList<String> result;
				
				while(results.next()) {
					result = new ArrayList<String>();
					result.add(new Integer(results.getInt("id")).toString());
					result.add(results.getString("name"));
					result.add(new Integer(results.getInt("course")).toString());
					result.add(new Integer(results.getInt("private")).toString());
					result.add(results.getString("password"));
					result.add(new Integer(results.getInt("maxgroupsize")).toString());
					result.add(results.getString("closedate"));
					result.add(results.getString("students"));
					result.add(results.getString("groups"));
					result.add(new Integer(results.getInt("questionnaire")).toString());
					envs.add(result);
				}
			}
			catch(SQLException e) {
				ArrayList<String> errorlist = new ArrayList<String>();
				for(int i = 0; i < 9; i++) {
					errorlist.add("");
				}
				envs.add(errorlist);
			}
		}
		return envs;
	}
	
	public boolean deleteEnvironment(int id) {
		//Get list of all courses
		ArrayList<ArrayList<String>> courses = this.queryAllCourses();
		for(ArrayList<String> x : courses) {
			//Get list of environments from course
			ArrayList<String> y = new ArrayList<String>(Arrays.asList(x.get(4).split(", ")));
			//Remove the course that is being deleted
			y.remove(new Integer(id).toString());
			//Update list of courses
			String z = y.toArray().toString();
			z = z.replace("[","");
			z = z.replace("]","");
			this.updateCourse(Integer.parseInt(x.get(0)),z);
		}
		if(opened == false || dbconn == null) {
			//DB isn't open
			return false;
		}
		else {
			//Write SQL
			String sql = "DELETE FROM Environments WHERE id = ?";
			try {
				//Prepare statement
				PreparedStatement psql = dbconn.prepareStatement(sql);
				//Assign variables
				psql.setInt(1,id);
				//Execute statement
				psql.executeUpdate();
				//Verify that the course was deleted
				return this.queryEnvironment(id).isEmpty();
			}
			catch(SQLException e) {
				//Some error occurred
				return false;
			}
		}
	}
	
	public int insertQuestionnaire(int eid, HashMap<String,String[]> questions) {
		//Basic sanity check
		if(this.queryEnvironment(eid).isEmpty() || questions.isEmpty()) {
			return -1;
		}
		String q = "";
		String a = "";
		q = Arrays.toString(questions.keySet().toArray());
		q = q.replace("[","");
		q = q.replace("]","");
		ArrayList<String[]> answers = new ArrayList<String[]>();
		answers.addAll(questions.values());
		for(String[] x : answers) {
			String y = Arrays.toString(x);
			y = y.replace("[","");
			y = y.replace("]","");
			y = y.replace(", ","|");
			a += y + ", ";
		}
		a = a.substring(0,a.lastIndexOf(", "));
		int id = -1;
		if(opened != false && dbconn != null) {
			//Database is opened, can insert into DB now
			String sql = "INSERT INTO Questionnaires(environment,questions,answers) VALUES (?,?,?)";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setInt(1,eid);
				psql.setString(2,q);
				psql.setString(3,a);
				psql.executeUpdate();
				id = this.getQuestionnaire(eid);
				this.changeQuestionnaire(eid,id);
				
			}
			catch(SQLException e) {
				return -1;
			}
		}
		return id;
	}
	
	public int getQuestionnaire(int eid) {
		ArrayList<Integer> qs = new ArrayList<Integer>();
		if(opened != false && dbconn != null) {
			String sql = "SELECT ID FROM Questionnaires WHERE environment = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setInt(1, eid);
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					qs.add(new Integer(results.getInt("ID")));
				}
			}
			catch(SQLException e) {
				return -1;
			}
		}
		if(qs.isEmpty() == true) {
			return -1;
		}
		else {
			return qs.get(0);
		}
	}
	
	public boolean queryQuestionnaire(int qid) {
		ArrayList<Integer> qs = new ArrayList<Integer>();
		if(opened != false && dbconn != null) {
			String sql = "SELECT environment FROM Questionnaires WHERE id = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setInt(1, qid);
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					qs.add(new Integer(results.getInt("environment")));
				}
			}
			catch(SQLException e) {
				return false;
			}
		}
		if(qs.isEmpty() == true) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public HashMap<String,String[]> getQuestions(int id) {
		HashMap<String,String[]> qamap = new HashMap<String,String[]>();
		ArrayList<String> qset = new ArrayList<String>();
		ArrayList<String[]> aset = new ArrayList<String[]>();
		String qs = "";
		String as = "";
		if(opened != false && dbconn != null) {
			String sql = "SELECT questions,answers FROM Questionnaires WHERE id = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setInt(1, id);
				ResultSet results = psql.executeQuery();
				
				while(results.next()) {
					qs += results.getString("questions");
					as += results.getString("answers");
				}
			}
			catch(SQLException e) {
				return qamap;
			}
			
			qs = qs.replace("[","");
			qs = qs.replace("]","");
			as = as.replace("[","");
			as = as.replace("]","");
			
			String[] qs2 = qs.split(", ");
			for(String x : qs2) {
				qset.add(x);
			}
			String[] as2 = as.split(", ");
			for(String x : as2) {
				aset.add(x.split("|"));
			}
			
			for(int i = 0; i < Math.min(qset.size(),aset.size()); i++) {
				qamap.put(qset.get(i),aset.get(i));
			}
		}
		return qamap;
	}
	
	public boolean deleteQuestionnaire(int id) {
		ArrayList<ArrayList<String>> envs = new ArrayList<ArrayList<String>>();
		envs = this.queryAllEnvironments();
		for(ArrayList<String> x : envs) {
			this.updateCourse(Integer.parseInt(x.get(0)),-1);
		}
		if(opened == false || dbconn == null) {
			return false;
		}
		else {
			String sql = "DELETE FROM Questionnaires WHERE id = ?";
			try {
				PreparedStatement psql = dbconn.prepareStatement(sql);
				psql.setInt(1,id);
				psql.executeUpdate();
				return true;
			}
			catch(SQLException e) {
				return false;
			}
		}
	}
	
	public int insertGroup() {
		return -1;
	}
	
	public static void deleteCrap(GrouperDB db, boolean oc, int qid, int eid, int cid, int uid, int uid2, int uid3) {
		System.out.println("DELETING DATA");
		oc = db.deleteQuestionnaire(qid);
		if(oc == true) {
			System.out.println("Successfully deleted questionnaire from the database");
		}
		else {
			System.out.println("Failed to delete questionnaire from the database");
		}
		oc = db.deleteEnvironment(eid);
		if(oc == true) {
			System.out.println("Successfully deleted environment from the database");
		}
		else {
			System.out.println("Failed to delete environment from the database");
		}
		oc = db.deleteCourse(cid);
		if(oc == true) {
			System.out.println("Successfully deleted course from the database");
		}
		else {
			System.out.println("Failed to delete course from the database");
		}
		oc = db.deleteUniversity(uid);
		if(oc == true) {
			System.out.println("Successfully deleted university from the database");
		}
		else {
			System.out.println("Failed to delete university from the database");
		}
		oc = db.deleteUser(uid2);
		if(oc == true) {
			System.out.println("Successfully deleted user from the database");
		}
		else {
			System.out.println("Failed to delete user from the database");
		}
		oc = db.deleteUser(uid3);
		if(oc == true) {
			System.out.println("Successfully deleted user from the database");
		}
		else {
			System.out.println("Failed to delete user from the database");
		}
		System.out.println("");
		oc = db.closeDB();
		if(oc == true) {
			System.out.println("Successfully closed the database");
		}
		else {
			System.out.println("Failed to close the database");
		}
	}

	public static void main(String[] args) {
		GrouperDB db = new GrouperDB("grouperdb.db");
		boolean oc = false;
		int uid = -1;
		int uid2 = -1;
		int uid3 = -1;
		int cid = -1;
		int eid = -1;
		int qid = -1;
		
		oc = db.openDB();
		if(oc == true) {
			System.out.println("Successfully opened the database");
		}
		else {
			System.out.println("Failed to open the database");
		}
		if(db.getState() == true) {
			System.out.println("");
			System.out.println("ADDING DATA");
			
			uid = db.insertUniversity("Carleton",new int[0]);
			if(uid > -1) {
				System.out.println("Successfully added university to the database");
			}
			else {
				System.out.println("Failed to add university to the database");
			}
			
			uid2 = db.insertUser(123456789,"Test","User","test@test.com");
			if(uid2 > -1) {
				System.out.println("Successfully added user to the database");
			}
			else {
				System.out.println("Failed to add user to the database");
			}
			uid3 = db.insertUser(133713371,"LOL","User","java-sucks@LOL.com");
			if(uid3 > -1) {
				System.out.println("Successfully added user to the database");
			}
			else {
				System.out.println("Failed to add user to the database");
			}
			
			oc = db.updateUser(uid2,"test2@test.com");
			if(oc == true) {
				System.out.println("Successfully updated user in the database");
			}
			else {
				System.out.println("Failed to update user in the database");
			}
			
			cid = db.insertCourse("LOLcourse",uid2,uid);
			if(cid > -1) {
				System.out.println("Successfully added course to the database");
			}
			else {
				System.out.println("Failed to add course to the database");
			}
			
			oc = db.updateCourse(cid,uid3);
			if(oc == true) {
				System.out.println("Successfully updated course in the database");
			}
			else {
				System.out.println("Failed to update course in the database");
			}
			
			int[] c = new int[1];
			c[0]=cid;
			oc = db.updateUniversity(uid,c);
			if(oc == true) {
				System.out.println("Successfully updated university in the database");
			}
			else {
				System.out.println("Failed to update university in the database");
			}
			
			eid = db.insertEnvironment("Test Environment",cid,false,"",3,"2/25/2069");
			if(eid > -1) {
				System.out.println("Successfully added environment to the database");
			}
			else {
				System.out.println("Failed to add environment to the database");
			}
			
			HashMap<String,String[]> qset = new HashMap<String,String[]>();
			String[] a1 = {"a","b","c"};
			String[] a2 = {"d","e"};
			qset.put("Test question 1",a1);
			qset.put("Test question 2",a2);
			
			qid = db.insertQuestionnaire(eid,qset);
			if(qid > -1) {
				System.out.println("Successfully added questionnaire to the database");
			}
			else {
				System.out.println("Failed to add questionnaire to the database");
			}
			
			/*System.out.println("");
			System.out.println("DISPLAYING DATA");			
			ArrayList<String[]> universities = db.queryAllUniversities();
			for(String[] x : universities) {
				System.out.println(Arrays.toString(x));
			}
			universities = db.queryUniversity(uid);
			for(String[] x : universities) {
				System.out.println(Arrays.toString(x));
			}
			ArrayList<String[]> users = db.queryAllUsers();
			for(String[] x : users) {
				System.out.println(Arrays.toString(x));
			}
			users = db.queryUser(uid2);
			for(String[] x : users) {
				System.out.println(Arrays.toString(x));
			}
			ArrayList<String[]> courses = db.queryAllCourses();
			for(String[] x : courses) {
				System.out.println(Arrays.toString(x));
			}
			courses = db.queryCourse(cid);
			for(String[] x : courses) {
				System.out.println(Arrays.toString(x));
			}
			ArrayList<ArrayList<String>> envs = db.queryAllEnvironments();
			for(ArrayList<String> x : envs) {
				System.out.println(Arrays.toString(x.toArray()));
			}
			qset = db.getQuestions(qid);
			System.out.println(qset.toString());
			System.out.println("");*/
			
			//deleteCrap(db,oc,qid,eid,cid,uid,uid2,uid3);
		}
	}

}