package views;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import utils.FileHandler;

public class LoginFrame extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(){
        setTitle("Student Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc= new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx=0; gbc.gridy=0;
        panel.add(new JLabel("Username:"),gbc);
        gbc.gridx=1;
        usernameField = new JTextField(20);
        panel.add(usernameField,gbc);

        gbc.gridx=0; gbc.gridy=1;
        panel.add(new JLabel("Password:"),gbc);
        gbc.gridx=1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField,gbc);

        gbc.gridx=1; gbc.gridy=2;
        JButton loginButton = new JButton("Login");
        panel.add(loginButton,gbc);

        add(panel);

        loginButton.addActionListener(e -> validateLogin());
    }

    private void validateLogin(){
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        List<String> loginData = FileHandler.readFromFile("login.txt");
        boolean found = false;

        for(String line : loginData){
            if(line == null || line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if(parts.length < 3) continue;
            String fileUser = parts[0].trim();
            String filePass = parts[1].trim();
            String role     = parts[2].trim();
            if(fileUser.equals(username) && filePass.equals(password)){
                found = true;
                openDashboard(role, fileUser); // pass the username (ID)
                break;
            }
        }

        if(!found){
            JOptionPane.showMessageDialog(this,
                "Invalid username or password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDashboard(String role, String userId){
        this.dispose();
        switch(role.toUpperCase()){
            case "ADMIN"      -> new AdminDashboard().setVisible(true);
            case "INSTRUCTOR" -> new InstructorDashboard().setVisible(true);
            case "STUDENT"    -> new StudentDashboard(userId).setVisible(true);
            default -> JOptionPane.showMessageDialog(null,"Unknown role: "+role);
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
