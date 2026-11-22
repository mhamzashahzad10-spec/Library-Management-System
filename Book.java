package model;

/**
 * Book: one copy of a book in the library.
 * Status follows the Book state machine: Available, Borrowed, Overdue, Removed.
 */
public class Book implements Reportable {

    private int bookId;
    private String title;
    private String author;
    private String category;
    private String status;

    // Constructor 1: a new book is always Available (state diagram: start -> Available)
    public Book(int bookId, String title, String author, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = "Available";
    }

    // Constructor 2 (overloading): status is given
    public Book(int bookId, String title, String author, String category, String status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ---------- operations ----------
    public boolean isAvailable() {
        return status.equals("Available");
    }

    public boolean isRemoved() {
        return status.equals("Removed");
    }

    // Available -> Borrowed
    public boolean markBorrowed() {
        if (isAvailable()) {
            status = "Borrowed";
            return true;
        }
        return false;
    }

    // Borrowed or Overdue -> Available
    public void markReturned() {
        if (status.equals("Borrowed") || status.equals("Overdue")) {
            status = "Available";
        }
    }

    // Borrowed -> Overdue
    public void markOverdue() {
        if (status.equals("Borrowed")) {
            status = "Overdue";
        }
    }

    // Available -> Removed
    public boolean markRemoved() {
        if (isAvailable()) {
            status = "Removed";
            return true;
        }
        return false;
    }

    // Used by Search Book: matches the keyword with title, author or category
    public boolean matches(String keyword) {
        String key = keyword.toLowerCase();
        return title.toLowerCase().contains(key)
                || author.toLowerCase().contains(key)
                || category.toLowerCase().contains(key);
    }

    public void displayDetails() {
        System.out.println("Book ID : " + bookId);
        System.out.println("Title   : " + title);
        System.out.println("Author  : " + author);
        System.out.println("Category: " + category);
        System.out.println("Status  : " + status);
    }

    @Override
    public String generateReport() {
        return "[" + bookId + "] " + title + " by " + author
                + " (" + category + ") - " + status;
    }
}
