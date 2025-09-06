# Student Management System (Java GUI)

A simple Java (Swing) based Student Management System that supports three roles: Admin, Instructor (Teacher), and Student.  
The system allows centralized management of students, instructors, and courses, with separate login experiences for each role.

---

## Key Features

### 1. Admin
- First (default) login:
  - Username: `admin`
  - Password: `admin`
  (These credentials are defined in the Login class/file.)
- Can create / manage:
  - Students
  - Instructors (Teachers)
  - Courses
- (If implemented) assign instructors and students to courses
- (Optional) update user credentials

### 2. Instructor (Teacher)
- Logs in with the ID/username and password created by the Admin
- Can view assigned courses
- Can view enrolled students
- (Optional future abilities) record grades, attendance, feedback

### 3. Student
- Logs in with the ID/username and password created by the Admin
- Can view:
  - Profile information
  - Enrolled courses
  - (Optional) grades / progress

---

## Typical Workflow

1. Start the application.
2. Log in as Admin using:
   - Username: `admin`
   - Password: `admin`
3. From the Admin interface:
   - Add Students
   - Add Instructors
   - Add Courses
   - (If available) link Students and Instructors to Courses
4. Distribute generated or assigned credentials to users.
5. Students and Instructors log in with their credentials to access their dashboards.

---

## Project Structure (Example / Suggested)

```
src/
  model/          (Student.java, Instructor.java, Course.java, User.java)
  ui/             (LoginFrame.java, AdminDashboard.java, etc.)
  service/        (Business logic classes)
  dao/            (Data access / storage handling)
  util/           (Helpers, validators)
```

(Actual structure may differ depending on your current implementation.)

---

## Data Storage

Depending on the current version of the project, data might be:
- Stored in memory only (lost on restart)
- Saved to simple text / serialized / CSV files
- (Future possibility) integrated with a database (e.g., SQLite, MySQL)

Clarify storage behavior in code comments if collaborating with others.

---

## Default Admin Credentials

| Role  | Username | Password |
|-------|----------|----------|
| Admin | admin    | admin    |

Recommendation: Implement a "force change password" feature on first login for security.

---

## Security Considerations

| Area | Current (Likely) | Recommendation |
|------|------------------|----------------|
| Default credentials | Hard-coded | Prompt change on first login |
| Password storage | Plain text | Use hashing (e.g., BCrypt) |
| Access control | UI-based | Enforce role checks in logic layer too |
| Input validation | Minimal | Add strict validation & sanitation |
| Session handling | Basic | Add logout + inactivity timeout |

---

## Troubleshooting

| Problem | Possible Cause | Suggested Fix |
|---------|----------------|---------------|
| Cannot log in as admin | Credentials changed in code | Restore original Login class |
| New user cannot log in | Password not stored / mismatch | Verify user creation method |
| Window not appearing | `setVisible(true)` not called or EDT misuse | Wrap startup in `SwingUtilities.invokeLater` |
| Data disappears after restart | In-memory only | Implement file or DB persistence |
| Role sees wrong options | Missing role checks | Add conditional UI + backend validation |

---
