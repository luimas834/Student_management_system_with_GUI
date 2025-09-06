package models;

public class User {
    private String id;
    private String name;
    private String password;
    private String role;
    private String department;

    public User(String id,String name,String password,String role,String department){
        this.id=id;
        this.name=name;
        this.password=password;
        this.role=role;
        this.department=department;
    }
    

    public String getId() {
        return id; 
    }
    public void setId(String id) {
        this.id = id; 
    }
    
    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public String getRole() { 
        return role; 
    }
    public void setRole(String role) { 
        this.role = role; 
    }
    
    public String getDepartment() { 
        return department; 
    }
    public void setDepartment(String department) {
        this.department = department; 
    }
}

