package views;

import javax.swing.*;
import java.awt.*;
import utils.FileHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InstructorDashboard extends JFrame {
    private JPanel mainPanel;
    private JButton viewCoursesBtn;
    private JButton viewStudentsBtn;
    private JButton assignGradesBtn;

    public InstructorDashboard() {
        setTitle("Instructor Dashboard");
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
        viewCoursesBtn = new JButton("View My Courses");
        viewStudentsBtn = new JButton("View Enrolled Students");
        assignGradesBtn = new JButton("Assign Grades");
    }
    private void addComponents() {
        mainPanel.add(viewCoursesBtn);
        mainPanel.add(viewStudentsBtn);
        mainPanel.add(assignGradesBtn);
    }
    private void addListeners() {
        viewCoursesBtn.addActionListener(e -> showCourses());
        viewStudentsBtn.addActionListener(e -> showEnrolledStudents());
        assignGradesBtn.addActionListener(e -> showAssignGradesDialog());
    }
 
    private Object showAssignGradesDialog() {
       JDialog dialog = new JDialog(this,"Assign Grades",true);
        dialog.setLayout(new GridLayout(6,2,5,5));

        // Collect existing course codes & student IDs from enrollments
        List<String> enrollments = FileHandler.readFromFile("enrollments.txt");
        Set<String> courseCodes = new HashSet<>();
        Set<String> studentIds = new HashSet<>();
        for(String line : enrollments){
            String[] p = line.split(",");
            if(p.length >= 2){
                studentIds.add(p[0]);
                courseCodes.add(p[1]);
            }
        }
        JComboBox<String> courseBox = new JComboBox<>(courseCodes.toArray(new String[0]));
        JComboBox<String> studentBox = new JComboBox<>(studentIds.toArray(new String[0]));
        JTextField gradeField = new JTextField();

        dialog.add(new JLabel("Course Code:"));
        dialog.add(courseBox);
        dialog.add(new JLabel("Student ID:"));
        dialog.add(studentBox);
        dialog.add(new JLabel("Grade:"));
        dialog.add(gradeField);

        JButton saveBtn = new JButton("Save Grade");
        saveBtn.addActionListener(e -> {
            if(courseBox.getItemCount()==0 || studentBox.getItemCount()==0){
                JOptionPane.showMessageDialog(dialog,"Need at least one enrollment.");
                return;
            }
            String targetCourse = (String) courseBox.getSelectedItem();
            String targetStudent = (String) studentBox.getSelectedItem();
            String grade = gradeField.getText().trim();
            if(grade.isEmpty()){
                JOptionPane.showMessageDialog(dialog,"Grade cannot be empty.");
                return;
            }

            List<String> updated = new ArrayList<>();
            for(String line : enrollments){
                String[] p = line.split(",");
                if(p.length >= 2 &&
                   p[0].equals(targetStudent) &&
                   p[1].equals(targetCourse)){
                    // Cases:
                    // id,course
                    // id,course,semester
                    // id,course,semester,grade
                    if(p.length == 2){
                        updated.add(String.format("%s,%s,,%s", p[0], p[1], grade));
                    } else if(p.length == 3){
                        updated.add(String.format("%s,%s,%s,%s", p[0], p[1], p[2], grade));
                    } else {
                        // replace last with new grade
                        if(p.length >= 4){
                            p[3] = grade;
                            updated.add(String.join(",", p[0],p[1],p[2],p[3]));
                        } else {
                            updated.add(line); // fallback
                        }
                    }
                } else {
                    updated.add(line);
                }
            }
            FileHandler.updateFile("enrollments.txt", updated);
            JOptionPane.showMessageDialog(dialog,"Grade saved.");
            dialog.dispose();
        });

        dialog.add(saveBtn);

        dialog.setSize(380,240);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        return null;
    }


    private Object showEnrolledStudents() {
      List<String> enrollments = FileHandler.readFromFile("enrollments.txt");
        if(enrollments.isEmpty()){
            JOptionPane.showMessageDialog(this,"No enrollments found.");
            return null;
        }
        StringBuilder sb = new StringBuilder("Enrollments (All):\n\n");
        for(String line : enrollments){
            String[] p = line.split(",");
            if(p.length >= 2){
                sb.append("Student: ").append(p[0])
                  .append(" | Course: ").append(p[1]);
                if(p.length >= 4 && !p[3].trim().isEmpty()){
                    sb.append(" | Grade: ").append(p[3]);
                }
                sb.append("\n");
            }
        }
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JOptionPane.showMessageDialog(this,new JScrollPane(area),"Enrolled Students",JOptionPane.INFORMATION_MESSAGE);
        return null;
    }


    private void showCourses() {
        List<String> courses = FileHandler.readFromFile("courses.txt");
        StringBuilder courseList = new StringBuilder("Your Courses:\n\n");
        
        for (String course : courses) {
            String[] parts = course.split(",");
            courseList.append(parts[0]).append(" - ").append(parts[1]).append("\n");
        }
        
        JTextArea textArea = new JTextArea(courseList.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JDialog dialog = new JDialog(this, "My Courses", true);
        dialog.add(scrollPane);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}

