package models;

import java.util.*;

public class Instructor extends User {

    private ArrayList<String> assignedCourses;
    
    public Instructor(String id, String name, String password, String role, String department) {
        super(id, name, password, role, department);
        this.assignedCourses = new ArrayList<>();
    }

    public void assignCourses(String courseCode){
        assignedCourses.add(courseCode);
    }
    public ArrayList<String> getAssignedcourses(){
        return assignedCourses;
    }
    
}

