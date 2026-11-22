package model;

/**
 * Abstract parent class for every person who uses the Library Management System.
 * Child classes: Student, Librarian, LibraryAdministrator, SystemAdministrator.
 */
public abstract class User {

    private int userId;
    private String name;
    private String username;
    private String password;

    // Constructor 1: all details given
    public User(int userId, String name, String username, String password) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // Constructor 2 (overloading): only id and name, default login details are created
    public User(int userId, String name) {
        this(userId, name, "user" + userId, "1234");
    }

    // ---------- getters and setters ----------
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ---------- concrete methods ----------

    // Use case: Login (checks username and password)
    public boolean login(String enteredUsername, String enteredPassword) {
        return username.equals(enteredUsername) && password.equals(enteredPassword);
    }

    // Default permission text; child classes override it
    public String getPermissions() {
        return "Basic access";
    }

    public void displayDetails() {
        System.out.println("ID: " + userId + " | Name: " + name
                + " | Username: " + username + " | Role: " + getRole());
    }

    // ---------- abstract method (every child must override it) ----------
    public abstract String getRole();
}
