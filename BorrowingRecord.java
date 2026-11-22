package model;

/**
 * BorrowingRecord: saved when a book is issued to a student.
 * COMPOSITION: a BorrowingRecord has a Book object and a Student object as members.
 * Days are stored as simple day numbers (day 1, day 2, ...).
 */
public class BorrowingRecord implements Reportable {

    public static final int LOAN_DAYS = 14;
    public static final double FINE_PER_DAY = 10.0;

    private int recordId;
    private Book book;
    private Student student;
    private int issueDay;
    private int dueDay;
    private int returnDay;
    private boolean returned;

    // Constructor 1: due date is calculated (issue day + 14 days)
    public BorrowingRecord(int recordId, Book book, Student student, int issueDay) {
        this.recordId = recordId;
        this.book = book;
        this.student = student;
        this.issueDay = issueDay;
        this.dueDay = issueDay + LOAN_DAYS;
        this.returnDay = -1;
        this.returned = false;
    }

    // Constructor 2 (overloading): due day is given
    public BorrowingRecord(int recordId, Book book, Student student, int issueDay, int dueDay) {
        this.recordId = recordId;
        this.book = book;
        this.student = student;
        this.issueDay = issueDay;
        this.dueDay = dueDay;
        this.returnDay = -1;
        this.returned = false;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getIssueDay() {
        return issueDay;
    }

    public void setIssueDay(int issueDay) {
        this.issueDay = issueDay;
    }

    public int getDueDay() {
        return dueDay;
    }

    public void setDueDay(int dueDay) {
        this.dueDay = dueDay;
    }

    public int getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(int returnDay) {
        this.returnDay = returnDay;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    // ---------- operations ----------

    // Operation: record that the book was brought back
    public void closeRecord(int day) {
        returnDay = day;
        returned = true;
    }

    // Operation: a record is overdue if the book is not returned and the due day has passed
    public boolean isOverdue(int today) {
        return !returned && today > dueDay;
    }

    // Overloaded method 1: fine with the standard rate
    public double calculateFine(int today) {
        return calculateFine(today, FINE_PER_DAY);
    }

    // Overloaded method 2: fine with a given rate per day
    public double calculateFine(int today, double ratePerDay) {
        int endDay;
        if (returned) {
            endDay = returnDay;
        } else {
            endDay = today;
        }
        int lateDays = endDay - dueDay;
        if (lateDays > 0) {
            return lateDays * ratePerDay;
        }
        return 0.0;
    }

    @Override
    public String generateReport() {
        String state;
        if (returned) {
            state = "Returned on day " + returnDay;
        } else {
            state = "Not returned yet";
        }
        return "Record " + recordId + ": '" + book.getTitle() + "' issued to "
                + student.getName() + " on day " + issueDay + ", due day " + dueDay
                + " - " + state;
    }
}
