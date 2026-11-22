package model;

/**
 * Librarian: issues and returns books and monitors overdue books.
 */
public class Librarian extends User {

    private String employeeId;
    private String shift;
    private int transactionsHandled;

    public Librarian(int userId, String name, String username, String password,
                     String employeeId, String shift) {
        super(userId, name, username, password);
        this.employeeId = employeeId;
        this.shift = shift;
        this.transactionsHandled = 0;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public int getTransactionsHandled() {
        return transactionsHandled;
    }

    public void setTransactionsHandled(int transactionsHandled) {
        this.transactionsHandled = transactionsHandled;
    }

    // Operation from the class diagram: issueBook()
    public String issueBook(Library library, int bookId, int studentId, int today) {
        transactionsHandled++;
        return library.issueBook(bookId, studentId, today);
    }

    // Operation from the class diagram: returnBook()
    public String returnBook(Library library, int bookId, int today) {
        transactionsHandled++;
        return library.returnBook(bookId, today);
    }

    @Override
    public String getRole() {
        return "Librarian";
    }

    @Override
    public String getPermissions() {
        return "Issue books, return books, track overdue books, manage books and student records";
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("   Employee ID: " + employeeId + " | Shift: " + shift
                + " | Transactions handled: " + transactionsHandled);
    }
}
