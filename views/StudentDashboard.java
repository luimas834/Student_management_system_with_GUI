package views;

import javax.swing.*;
import java.awt.*;
import utils.FileHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StudentDashboard extends JFrame {
    private JPanel mainPanel;
    private JButton viewProfileBtn;
    private JButton viewCoursesBtn;
    private JButton viewTranscriptBtn;

    private final String currentStudentId;

    public StudentDashboard(String studentId) {
        this.currentStudentId= studentId;

        setTitle("Student Dashboard -ID: "+ currentStudentId);
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        initializeComponents();
        addComponents();
        addListeners();

        add(mainPanel);
    }

    private void initializeComponents() {
        viewProfileBtn = new JButton("View Profile");
        viewCoursesBtn = new JButton("View Enrolled Courses");
        viewTranscriptBtn = new JButton("View Transcript");
    }

    private void addComponents() {
        mainPanel.add(viewProfileBtn);
        mainPanel.add(viewCoursesBtn);
        mainPanel.add(viewTranscriptBtn);
    }

    private void addListeners() {
        viewProfileBtn.addActionListener(e -> showProfile());
        viewCoursesBtn.addActionListener(e -> showEnrolledCourses());
        viewTranscriptBtn.addActionListener(e -> showTranscript());
    }


    private void showProfile() {
       List<String> students = FileHandler.readFromFile("students.txt");
        String foundLine = null;
        for (String line : students) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] p = line.split(",", -1);
            if (p.length < 1) continue;
            if (p[0].trim().equals(currentStudentId)) {
                foundLine = line;
                break;
            }
        }
        if (foundLine == null) {
            JOptionPane.showMessageDialog(this,
                    "No profile record found for ID: " + currentStudentId,
                    "Profile",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] parts = foundLine.split(",", -1);
        String name = parts.length >= 2 ? parts[1].trim() : "(unknown)";
        String dept = parts.length >= 4 ? parts[3].trim() : "(n/a)";

        StringBuilder sb = new StringBuilder();
        sb.append("Student ID : ").append(currentStudentId).append("\n");
        sb.append("Name       : ").append(name).append("\n");
        sb.append("Department : ").append(dept).append("\n");

        JOptionPane.showMessageDialog(this, sb.toString(), "Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEnrolledCourses() {
         List<String> enrollments = FileHandler.readFromFile("enrollments.txt");
        if (enrollments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No enrollments found.", "Enrollments", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Collect lines for this student
        List<String> my = new ArrayList<>();
        for (String line : enrollments) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] p = line.split(",", -1);
            if (p.length < 2) continue;
            if (p[0].trim().equals(currentStudentId)) {
                my.add(line);
            }
        }

        if (my.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "You are not enrolled in any courses yet.",
                    "Enrollments",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Enrollments for ID ").append(currentStudentId).append(":\n\n");
        for (String line : my) {
            String[] p = line.split(",", -1);
            String course = p.length >= 2 ? p[1].trim() : "(unknown)";
            String semester = "";
            String grade = "";
            if (p.length == 2) {
                // id,course
            } else if (p.length == 3) {
                // id,course,semester
                semester = p[2].trim();
            } else if (p.length >= 4) {
                // id,course,semester(or blank),grade
                semester = p[2].trim();
                grade = p[3].trim();
            }
            sb.append("Course: ").append(course);
            if (!semester.isEmpty()) sb.append(" | Semester: ").append(semester);
            if (!grade.isEmpty()) sb.append(" | Grade: ").append(grade);
            sb.append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Enrolled Courses", JOptionPane.INFORMATION_MESSAGE);
    }


    private void showTranscript() {
       List<String> enrollments = FileHandler.readFromFile("enrollments.txt");
        Map<String, String> courseToGrade = new LinkedHashMap<>();

        for (String line : enrollments) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] p = line.split(",", -1);
            if (p.length < 2) continue;
            if (!p[0].trim().equals(currentStudentId)) continue;

            String course = p[1].trim();
            String grade = "";
            if (p.length >= 4) {
                grade = p[3].trim();
            } else if (p.length == 3) {
                // no grade yet
            }
            if (!grade.isEmpty()) {
                courseToGrade.put(course, grade);
            }
        }

        if (courseToGrade.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No graded courses yet. GPA cannot be calculated.",
                    "Transcript",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double totalPoints = 0.0;
        int gradedCount = 0;
        StringBuilder sb = new StringBuilder("Transcript for ID ").append(currentStudentId).append(":\n\n");

        for (Map.Entry<String, String> e : courseToGrade.entrySet()) {
            String course = e.getKey();
            String grade = e.getValue();
            double pts = letterToPoints(grade);
            if (pts >= 0) {
                totalPoints += pts;
                gradedCount++;
            }
            sb.append(course).append(" : ").append(grade);
            if (pts >= 0) sb.append(" (").append(pts).append(" pts)");
            sb.append("\n");
        }

        double gpa = gradedCount == 0 ? 0.0 : totalPoints / gradedCount;
        sb.append("\nGPA: ").append(String.format(Locale.US,"%.2f", gpa));

        JOptionPane.showMessageDialog(this, sb.toString(), "Transcript", JOptionPane.INFORMATION_MESSAGE);
    }


     private double letterToPoints(String g) {
        if (g == null) return -1;
        g = g.trim().toUpperCase();
        return switch (g) {
            case "A+" -> 4.0;
            case "A"  -> 4.0;
            case "A-" -> 3.7;
            case "B+" -> 3.3;
            case "B"  -> 3.0;
            case "B-" -> 2.7;
            case "C+" -> 2.3;
            case "C"  -> 2.0;
            case "C-" -> 1.7;
            case "D+" -> 1.3;
            case "D"  -> 1.0;
            case "F"  -> 0.0;
            default   -> -1; // unknown or blank
        };
    }
}

