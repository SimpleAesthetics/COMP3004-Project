package com.aesthetics.simple.grouper;

import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.Course;
import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.Environment;
import com.aesthetics.simple.grouper.com.simpleaesthetics.application.Model.University;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by visha on 2017-10-31.
 */

public class VirtualServer {
    List<University> Universities;
    public VirtualServer(){
        Universities = new ArrayList<>();
        Universities.add(new University("Carleton University"));
        Universities.add(new University("University of Ottawa"));
        ArrayList<Environment> e1 = new ArrayList<>();

        e1.add(new Environment("Fall Project",false,"",64));
        e1.get(0).setDate("14","09","2017");
        e1.add(new Environment("Winter Project",false,"",64));
        e1.get(1).setDate("14","01","2018");
        Universities.get(0).coursesList.add(new Course("Object Oriented Programming","COMP3004","Dr. Olga Baysal", e1));
        Universities.get(0).coursesList.add(new Course("Sinple other course","SIMP1000","Prof. Someone", null));
    }
}
