package model;

/**
 * LibraryAdministrator: manages the overall book collection and student records.
 */
public class LibraryAdministrator extends User {

    private String adminCode;
    private String office;
    private int booksManaged;

    public LibraryAdministrator(int userId, String name, String username, String password,
                                String adminCode, String office) {
        super(userId, name, username, password);
        this.adminCode = adminCode;
        this.office = office;
        this.booksManaged = 0;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public int getBooksManaged() {
        return booksManaged;
    }

    public void setBooksManaged(int booksManaged) {
        this.booksManaged = booksManaged;
    }

    // Operation from the class diagram: addBook() (part of Manage Books)
    public boolean addBook(Library library, Book book) {
        boolean added = library.addBook(book);
        if (added) {
            booksManaged++;
        }
        return added;
    }

    // Operation from the class diagram: removeBook() (part of Manage Books)
    public String removeBook(Library library, int bookId) {
        String result = library.removeBook(bookId);
        if (result.startsWith("SUCCESS")) {
            booksManaged++;
        }
        return result;
    }

    @Override
    public String getRole() {
        return "Library Administrator";
    }

    @Override
    public String getPermissions() {
        return "Add books, update books, remove books, manage student records";
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("   Admin code: " + adminCode + " | Office: " + office
                + " | Book changes made: " + booksManaged);
    }
}
