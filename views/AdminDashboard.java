package views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import utils.FileHandler;

public class AdminDashboard extends JFrame {
    private JPanel mainPanel;
    private JButton addStdntBtn;
    private JButton addInsBtn;
    private JButton addCourseBtn;
    private JButton enrollStdntBtn;
    private JButton manageRecordsBtn;

    public AdminDashboard(){
        setTitle("Admin Dashboard");
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3,2,10,10));

        initializeComponents();
        addComponents();
        addListeners(); 

        add(mainPanel);
    }

    private void initializeComponents(){
        addStdntBtn = new JButton("Add Student");
        addInsBtn = new JButton("Add Instructor");
        addCourseBtn =new JButton("Add course");
        enrollStdntBtn =new JButton("Enroll Student");
        manageRecordsBtn = new JButton("Manage Records");
    }

    private void addComponents() {
        mainPanel.add(addStdntBtn);
        mainPanel.add(addInsBtn);
        mainPanel.add(addCourseBtn);
        mainPanel.add(enrollStdntBtn);
        mainPanel.add(manageRecordsBtn);
    }

    private void addListeners(){
        addStdntBtn.addActionListener(e -> showAddStudentDialog());
        addInsBtn.addActionListener(e -> showAddInstructorDialog());
        addCourseBtn.addActionListener(e -> showAddCourseDialog());
        enrollStdntBtn.addActionListener(e -> showEnrollStudentDialog());
        manageRecordsBtn.addActionListener(e -> showManageRecordsDialog());
    }
    private Object showManageRecordsDialog() {
       JDialog dialog = new JDialog(this,"Manage Records",true);
        dialog.setSize(600,400);
        dialog.setLocationRelativeTo(this);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Students", buildManagePanel("students.txt"));
        tabs.add("Instructors", buildManagePanel("instructors.txt"));
        tabs.add("Courses", buildManagePanel("courses.txt"));
        tabs.add("Enrollments", buildManagePanel("enrollments.txt"));

        dialog.add(tabs);
        dialog.setVisible(true);
        return null;
    }

    private JPanel buildManagePanel(String filename){
        JPanel panel = new JPanel(new BorderLayout(5,5));
        List<String> data = FileHandler.readFromFile(filename);
        DefaultListModel<String> model = new DefaultListModel<>();
        for(String line : data){
            model.addElement(line);
        }
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if(idx >= 0){
                model.remove(idx);
                // write back
                List<String> newData = new ArrayList<>();
                for(int i=0;i<model.size();i++){
                    newData.add(model.getElementAt(i));
                }
                FileHandler.updateFile(filename,newData);
            }
        });

        panel.add(new JScrollPane(list),BorderLayout.CENTER);
        panel.add(deleteBtn,BorderLayout.SOUTH);
        return panel;
    }

    private Object showEnrollStudentDialog() {
      JDialog dialog = new JDialog(this,"Enroll Student",true);
        dialog.setLayout(new GridLayout(5,2,5,5));

        // Students
        List<String> students = FileHandler.readFromFile("students.txt");
        Vector<String> studentIds = new Vector<>();
        for(String s: students){
            String[] p = s.split(",");
            if(p.length > 0) studentIds.add(p[0]);
        }
        JComboBox<String> studentBox = new JComboBox<>(studentIds);

        // Courses
        List<String> courses = FileHandler.readFromFile("courses.txt");
        Vector<String> courseCodes = new Vector<>();
        for(String c : courses){
            String[] p = c.split(",");
            if(p.length > 0) courseCodes.add(p[0]);
        }
        JComboBox<String> courseBox = new JComboBox<>(courseCodes);

        // Semester (optional)
        JTextField semesterField = new JTextField();

        dialog.add(new JLabel("Student ID:"));
        dialog.add(studentBox);
        dialog.add(new JLabel("Course Code:"));
        dialog.add(courseBox);
        dialog.add(new JLabel("Semester (optional):"));
        dialog.add(semesterField);

        JButton enrollBtn = new JButton("Enroll");
        enrollBtn.addActionListener(e -> {
            if(studentBox.getItemCount()==0 || courseBox.getItemCount()==0){
                JOptionPane.showMessageDialog(dialog,"Need at least one student and one course.");
                return;
            }
            String studentId = (String) studentBox.getSelectedItem();
            String courseCode = (String) courseBox.getSelectedItem();
            String semester = semesterField.getText().trim();
            String line = semester.isEmpty()
                    ? String.format("%s,%s", studentId, courseCode)
                    : String.format("%s,%s,%s", studentId, courseCode, semester);
            FileHandler.writeToFile("enrollments.txt", line);
            JOptionPane.showMessageDialog(this,"Enrollment saved.");
            dialog.dispose();
        });

        dialog.add(enrollBtn);

        dialog.setSize(360,250);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        return null;
    }

    private Object showAddCourseDialog() {
        JDialog dialog = new JDialog(this,"Add Course",true);
        dialog.setLayout(new GridLayout(6,2,5,5));

        JTextField codeField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField creditField = new JTextField();

        List<String> instructors = FileHandler.readFromFile("instructors.txt");
        Vector<String> instructorIds = new Vector<>();
        for(String ins : instructors){
            String[] p = ins.split(",");
            if(p.length > 0) instructorIds.add(p[0]);
        }
        JComboBox<String> instructorBox = new JComboBox<>(instructorIds);

        dialog.add(new JLabel("Course Code:"));
        dialog.add(codeField);
        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Credits:"));
        dialog.add(creditField);
        dialog.add(new JLabel("Instructor ID:"));
        dialog.add(instructorBox);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            if(codeField.getText().trim().isEmpty() ||
               titleField.getText().trim().isEmpty() ||
               creditField.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(dialog,"All fields required.");
                return;
            }
            double credits;
            try {
                credits = Double.parseDouble(creditField.getText().trim());
            } catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(dialog,"Credits must be numeric.");
                return;
            }
            String instructorId = instructorBox.getItemCount()==0 ? "" : (String) instructorBox.getSelectedItem();
            // courseCode,title,credits,instructorId
            String line = String.format("%s,%s,%.2f,%s",
                    codeField.getText().trim(),
                    titleField.getText().trim(),
                    credits,
                    instructorId
            );
            FileHandler.writeToFile("courses.txt", line);
            JOptionPane.showMessageDialog(this,"Course added.");
            dialog.dispose();
        });

        dialog.add(saveBtn);

        dialog.setSize(380,260);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        return null;
    }

    private Object showAddInstructorDialog() {
      JDialog dialog = new JDialog(this,"Add Instructor",true);
        dialog.setLayout(new GridLayout(6,2,5,5));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField passwordField= new JTextField();
        JTextField deptField = new JTextField();

        dialog.add(new JLabel("ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("Department:"));
        dialog.add(deptField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            if(idField.getText().trim().isEmpty() ||
               nameField.getText().trim().isEmpty() ||
               passwordField.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(dialog,"All required fields must be filled.");
                return;
            }
            String instructorData = String.format("%s,%s,%s,%s",
                idField.getText().trim(),
                nameField.getText().trim(),
                passwordField.getText().trim(),
                deptField.getText().trim()
            );
            FileHandler.writeToFile("instructors.txt", instructorData);

            String loginData = String.format("%s,%s,INSTRUCTOR",
                idField.getText().trim(),
                passwordField.getText().trim()
            );
            FileHandler.writeToFile("login.txt", loginData);

            dialog.dispose();
            JOptionPane.showMessageDialog(this,"Instructor added successfully!");
        });

        dialog.add(saveBtn);
        dialog.setSize(340,250);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        return null;
    }

    private void showAddStudentDialog(){
        JDialog dialog =new JDialog(this,"Add Student",true);
        dialog.setLayout(new GridLayout(5,2,5,5));
        
        JTextField idField =new JTextField();
        JTextField nameField = new JTextField();
        JTextField passwordField= new JTextField();
        JTextField deptField = new JTextField();

        dialog.add(new JLabel("ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("Department:"));
        dialog.add(deptField);

        JButton saveBtn = new JButton("save");
        saveBtn.addActionListener(e ->{
            String studentData = String.format("%s,%s,%s,%s",
                idField.getText(),
                nameField.getText(),
                passwordField.getText(),
                deptField.getText()
            );
            FileHandler.writeToFile("students.txt",studentData);

            String loginData =String.format("%s,%s,STUDENT",
                idField.getText(),
                passwordField.getText()
            );
            FileHandler.writeToFile("login.txt",loginData);

            dialog.dispose();
            JOptionPane.showMessageDialog(this,"student added successfully!");
        });

        dialog.add(saveBtn);
        dialog.setSize(300,200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
}

