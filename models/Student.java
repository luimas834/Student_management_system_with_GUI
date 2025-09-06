package models;

import java.util.*;

public class Student extends User{
    private ArrayList<String> enrolledCourses;
    private HashMap<String,Double> grades;

    public Student(String id,String name, String password,String department){
        super(id,name,password,"STUDENT",department);
        this.enrolledCourses=new ArrayList<>();
        this.grades=new HashMap<>();
    }

    public void enrollCourse(String courseCode){
        enrolledCourses.add(courseCode);
    }
    public void setGrade(String courseCode, Double grade){
        grades.put(courseCode,grade);
    }
    public Double getGrade(String courseCode){
        return grades.get(courseCode);
    }
    public ArrayList<String> getEnrolledCourses(){
        return enrolledCourses;
    }
    public double calculateGPA(){
        if(grades.isEmpty()){
            return 0.0;
        }

        double totalGpa=0;
        int totalCourses=0;
        for(Double grade: grades.values()){
            totalGpa +=grade;
            totalCourses++;
        }
        return totalGpa/totalCourses;
    }
}

