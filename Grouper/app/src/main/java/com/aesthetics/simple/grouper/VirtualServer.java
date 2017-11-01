package com.aesthetics.simple.grouper;

import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.Course;
import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.Environment;
import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.Group;
import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.University;
import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by visha on 2017-10-31.
 */

public class VirtualServer {
    public List<University> Universities;
    public List<String> UserList;
    public VirtualServer(){
        UserList=new ArrayList<>();
        UserList.add("grouper@test.com:boguspass");

        User a = new User("Vishahan",null);
        User b = new User("Jonathan",null);
        User c = new User("Brad",null);
        User d = new User("Osama",null);
        Set<User> grUsers = new HashSet<>();
        grUsers.add(a);
        grUsers.add(b);
        grUsers.add(c);
        grUsers.add(d);
        Group gr = new Group("Simple Aesthetics","45.382165:-75.6995581:?q=Herzberg+Laboratories+Carleton+University",grUsers);

        Universities = new ArrayList<>();
        Universities.add(new University("Carleton University"));
        Universities.add(new University("University of Ottawa"));
        ArrayList<Environment> e1 = new ArrayList<>();

        e1.add(new Environment("Fall Project",false,"",64));
        e1.get(0).setDate("14","09","2017");

        e1.get(0).getUsers().add(a);
        e1.get(0).getUsers().add(b);
        e1.get(0).getUsers().add(c);
        e1.get(0).getUsers().add(d);
        e1.get(0).getGroups().add(gr);

        Set<User> fluff = new HashSet<>();


        e1.add(new Environment("Winter Project",false,"",64));
        for(int i = 0; i < 30;i++){
            e1.get(1).getUsers().add(new User(i+"",null));
        }
        e1.get(1).setDate("14","01","2018");
        Universities.get(0).coursesList.add(new Course("Object Oriented Programming","COMP3004","Dr. Olga Baysal", e1));
        Universities.get(0).coursesList.add(new Course("Simple other course","SIMP1000","Prof. Someone", new ArrayList<Environment>()));
    }
}
