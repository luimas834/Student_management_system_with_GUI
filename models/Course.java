package models;

public class Course {
    private String courseCode;
    private String title;
    private double credits;
    private String instructorId;

    public Course(String courseCode,String title,double credits,String instructorId){
        this.courseCode=courseCode;
        this.title=title;
        this.credits=credits;
        this.instructorId=instructorId;
    }

     public String getCourseCode() { 
        return courseCode;
    }
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode; 
    }

    public String getTitle() { 
        return title;
    }
    public void setTitle(String title) {
        this.title = title; 
    }

    public double getCredits() { 
        return credits; 
    }
    public void setCredits(double credits) { 
        this.credits = credits; 
    }

    public String getInstructorId() { 
        return instructorId; 
    }
    public void setInstructorId(String instructorId) { 
        this.instructorId = instructorId; 
    }
}

