package model;

/**
 * Student: a user who searches, borrows and returns books.
 */
public class Student extends User {

    private String program;
    private boolean accountActive;
    private int booksBorrowed;

    // Constructor 1: full details
    public Student(int userId, String name, String username, String password, String program) {
        super(userId, name, username, password);
        this.program = program;
        this.accountActive = true;
        this.booksBorrowed = 0;
    }

    // Constructor 2 (overloading): short form, uses the default login of User
    public Student(int userId, String name, String program) {
        super(userId, name);
        this.program = program;
        this.accountActive = true;
        this.booksBorrowed = 0;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public boolean isAccountActive() {
        return accountActive;
    }

    public void setAccountActive(boolean accountActive) {
        this.accountActive = accountActive;
    }

    public int getBooksBorrowed() {
        return booksBorrowed;
    }

    public void setBooksBorrowed(int booksBorrowed) {
        this.booksBorrowed = booksBorrowed;
    }

    // Operation: check that the student can be issued a book
    public boolean hasValidAccount() {
        return accountActive;
    }

    public void increaseBorrowed() {
        booksBorrowed++;
    }

    public void decreaseBorrowed() {
        if (booksBorrowed > 0) {
            booksBorrowed--;
        }
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String getPermissions() {
        return "Search books, check availability, view own borrowing history";
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        String status;
        if (accountActive) {
            status = "Active";
        } else {
            status = "Inactive";
        }
        System.out.println("   Program: " + program + " | Account: " + status
                + " | Books currently borrowed: " + booksBorrowed);
    }
}
