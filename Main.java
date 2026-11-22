package main;

import java.util.Scanner;

import model.Book;
import model.Librarian;
import model.Library;
import model.LibraryAdministrator;
import model.Student;
import model.SystemAdministrator;
import model.User;

/**
 * Main: console menu that runs the Library Management System.
 */
public class Main {

    private static Scanner input = new Scanner(System.in);
    private static Library library = new Library();
    private static Librarian librarian;
    private static LibraryAdministrator libraryAdmin;
    private static SystemAdministrator systemAdmin;
    private static User loggedInUser = null;
    private static int today = 1;

    public static void main(String[] args) {
        loadSampleData();

        System.out.println("=====================================================");
        System.out.println("        LIBRARY MANAGEMENT SYSTEM (Console)");
        System.out.println("=====================================================");

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    registerAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    searchBook();
                    break;
                case 4:
                    checkAvailability();
                    break;
                case 5:
                    issueBook();
                    break;
                case 6:
                    returnBook();
                    break;
                case 7:
                    manageBooks();
                    break;
                case 8:
                    viewBorrowingHistory();
                    break;
                case 9:
                    trackOverdueBooks();
                    break;
                case 10:
                    showAllUsers();
                    break;
                case 11:
                    showSystemStatus();
                    break;
                case 12:
                    advanceDay();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using the Library Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from the menu.");
            }
        }
    }

    // ---------------------------------------------------------------
    // Menu and input helpers
    // ---------------------------------------------------------------
    private static void showMenu() {
        System.out.println();
        System.out.println("-------------------- MAIN MENU --------------------");
        if (loggedInUser == null) {
            System.out.println("Logged in as: (nobody)   | System day: " + today);
        } else {
            System.out.println("Logged in as: " + loggedInUser.getName() + " ("
                    + loggedInUser.getRole() + ")   | System day: " + today);
        }
        System.out.println(" 1. Register Account");
        System.out.println(" 2. Login");
        System.out.println(" 3. Search Book");
        System.out.println(" 4. Check Book Availability");
        System.out.println(" 5. Issue Book");
        System.out.println(" 6. Return Book");
        System.out.println(" 7. Manage Books (add / remove / view all)");
        System.out.println(" 8. View Borrowing History");
        System.out.println(" 9. Track Overdue Books");
        System.out.println("10. Show All System Users (polymorphism demo)");
        System.out.println("11. System Status and Statistics");
        System.out.println("12. Advance System Day");
        System.out.println(" 0. Exit");
        System.out.println("---------------------------------------------------");
    }

    // Reads an integer; asks again while the user types something else
    private static int readInt(String message) {
        System.out.print(message);
        while (!input.hasNextInt()) {
            input.next();
            System.out.print("Invalid number. " + message);
        }
        int value = input.nextInt();
        input.nextLine();
        return value;
    }

    private static String readText(String message) {
        System.out.print(message);
        return input.nextLine().trim();
    }

    private static void loadSampleData() {
        librarian = new Librarian(101, "Imran Malik", "imran", "lib123", "EMP-01", "Morning");
        libraryAdmin = new LibraryAdministrator(201, "Hina Tariq", "hina", "admin123",
                "ADM-01", "Main Office");
        systemAdmin = new SystemAdministrator(301, "Bilal Shah", "bilal", "sys123", "SYS-01", 3);
        library.addUser(librarian);
        library.addUser(libraryAdmin);
        library.addUser(systemAdmin);

        Student s1 = new Student(1, "Ali Khan", "ali", "1234", "BCS");
        Student s2 = new Student(2, "Sara Ahmed", "sara", "1234", "BSE");
        Student s3 = new Student(3, "Usman Raza", "usman", "1234", "BCS");
        s3.setAccountActive(false);   // an inactive account, used to test the invalid-account flow
        library.registerStudent(s1);
        library.registerStudent(s2);
        library.registerStudent(s3);

        library.addBook(new Book(1, "Java: The Complete Reference", "Herbert Schildt", "Programming"));
        library.addBook(new Book(2, "Database System Concepts", "Abraham Silberschatz", "Databases"));
        library.addBook(new Book(3, "Software Engineering", "Ian Sommerville", "Software Engineering"));
        library.addBook(new Book(4, "Operating System Concepts", "Abraham Silberschatz", "Operating Systems"));
        library.addBook(new Book(5, "Introduction to Algorithms", "Thomas Cormen", "Algorithms"));
    }

    // ---------------------------------------------------------------
    // Use case: Register Account
    // ---------------------------------------------------------------
    private static void registerAccount() {
        System.out.println("\n--- Register Account ---");
        int id = readInt("Enter new student ID (positive number): ");
        if (id <= 0) {
            System.out.println("FAILED: ID must be a positive number.");
            return;
        }
        String name = readText("Enter full name: ");
        String username = readText("Enter username: ");
        String password = readText("Enter password: ");
        String program = readText("Enter program (e.g. BCS): ");

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            System.out.println("FAILED: Name, username and password cannot be empty.");
            return;
        }
        Student student = new Student(id, name, username, password, program);
        System.out.println(library.registerStudent(student));
    }

    // ---------------------------------------------------------------
    // Use case: Login
    // ---------------------------------------------------------------
    private static void login() {
        System.out.println("\n--- Login ---");
        String username = readText("Username: ");
        String password = readText("Password: ");
        User user = library.login(username, password);
        if (user == null) {
            System.out.println("Login failed: wrong username or password.");
        } else {
            loggedInUser = user;
            System.out.println("Login successful. Welcome, " + user.getName() + "!");
            System.out.println("Role       : " + user.getRole());
            System.out.println("Permissions: " + user.getPermissions());
        }
    }

    // ---------------------------------------------------------------
    // Use case: Search Book
    // ---------------------------------------------------------------
    private static void searchBook() {
        System.out.println("\n--- Search Book ---");
        String keyword = readText("Enter title, author or category: ");
        String filter = readText("Filter by an exact category as well? (y/n): ");

        Book[] results;
        if (filter.equalsIgnoreCase("y")) {
            String category = readText("Enter category: ");
            results = library.searchBooks(keyword, category);   // overloaded method 2
        } else {
            results = library.searchBooks(keyword);              // overloaded method 1
        }

        if (results.length == 0) {
            System.out.println("No matching book found. Please try again with other details.");
        } else {
            System.out.println("Matching books (" + results.length + "):");
            library.printBooks(results);                         // array passed to a method
            int id = readInt("Enter a Book ID to see its details (0 to skip): ");
            if (id != 0) {
                Book book = library.findBookById(id);
                if (book == null || book.isRemoved()) {
                    System.out.println("No book found with ID " + id + ".");
                } else {
                    book.displayDetails();
                    if (book.isAvailable()) {
                        System.out.println("Availability: This book is AVAILABLE.");
                    } else {
                        System.out.println("Availability: This book is NOT available right now.");
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // Use case: Check Book Availability
    // ---------------------------------------------------------------
    private static void checkAvailability() {
        System.out.println("\n--- Check Book Availability ---");
        int id = readInt("Enter Book ID: ");
        Book book = library.findBookById(id);
        if (book == null || book.isRemoved()) {
            System.out.println("No book found with ID " + id + ".");
        } else if (book.isAvailable()) {
            System.out.println("'" + book.getTitle() + "' is AVAILABLE.");
        } else {
            System.out.println("'" + book.getTitle() + "' is NOT available (status: "
                    + book.getStatus() + ").");
        }
    }

    // ---------------------------------------------------------------
    // Use case: Issue Book (follows the Issue Book main flow)
    // ---------------------------------------------------------------
    private static void issueBook() {
        System.out.println("\n--- Issue Book ---");
        int bookId = readInt("Enter Book ID the student asked for: ");
        Book book = library.findBookById(bookId);
        if (book == null || book.isRemoved()) {
            System.out.println("No book found with ID " + bookId + ".");
            return;
        }
        System.out.println("System displays book information:");
        book.displayDetails();

        if (!book.isAvailable()) {
            System.out.println("The book is already borrowed. The librarian informs the student.");
            return;
        }
        System.out.println("The book is available.");
        int studentId = readInt("Librarian selects the student's record (enter Student ID): ");
        System.out.println(librarian.issueBook(library, bookId, studentId, today));
    }

    // ---------------------------------------------------------------
    // Use case: Return Book
    // ---------------------------------------------------------------
    private static void returnBook() {
        System.out.println("\n--- Return Book ---");
        int bookId = readInt("Enter Book ID being returned: ");
        System.out.println(librarian.returnBook(library, bookId, today));
    }

    // ---------------------------------------------------------------
    // Use case: Manage Books
    // ---------------------------------------------------------------
    private static void manageBooks() {
        System.out.println("\n--- Manage Books ---");
        System.out.println("1. Add a new book");
        System.out.println("2. Remove a book");
        System.out.println("3. View all books");
        int option = readInt("Choose an option: ");

        if (option == 1) {
            int id = readInt("Enter Book ID: ");
            String title = readText("Enter title: ");
            String author = readText("Enter author: ");
            String category = readText("Enter category: ");
            if (id > 0 && !title.isEmpty() && !author.isEmpty()) {
                Book book = new Book(id, title, author, category);
                if (libraryAdmin.addBook(library, book)) {
                    System.out.println("SUCCESS: Book added and set to Available.");
                } else {
                    System.out.println("FAILED: Book ID already exists or the library is full.");
                }
            } else {
                System.out.println("FAILED: ID must be positive and title/author cannot be empty.");
            }
        } else if (option == 2) {
            int id = readInt("Enter Book ID to remove: ");
            System.out.println(libraryAdmin.removeBook(library, id));
        } else if (option == 3) {
            System.out.println("All books in the library:");
            library.printBooks(library.getBooks());              // array passed to a method
        } else {
            System.out.println("Invalid option.");
        }
    }

    // ---------------------------------------------------------------
    // Use case: View Borrowing History
    // ---------------------------------------------------------------
    private static void viewBorrowingHistory() {
        System.out.println("\n--- View Borrowing History ---");
        int studentId = readInt("Enter Student ID: ");
        Student student = library.findStudentById(studentId);
        if (student == null) {
            System.out.println("No student found with ID " + studentId + ".");
            return;
        }
        System.out.println("Borrowing history of " + student.getName() + ":");
        int found = library.viewBorrowingHistory(studentId);
        if (found == 0) {
            System.out.println("  No borrowing records yet.");
        }
    }

    // ---------------------------------------------------------------
    // Track Overdue Books
    // ---------------------------------------------------------------
    private static void trackOverdueBooks() {
        System.out.println("\n--- Track Overdue Books (system day " + today + ") ---");
        int total = library.trackOverdue(today);
        if (total == 0) {
            System.out.println("  No overdue books.");
        } else {
            System.out.println("Total overdue books: " + total);
        }
    }

    // ---------------------------------------------------------------
    // Polymorphism demo: one User[] array holds Student, Librarian and both Administrators
    // ---------------------------------------------------------------
    private static void showAllUsers() {
        System.out.println("\n--- All System Users ---");
        User[] users = library.getUsers();
        for (int i = 0; i < library.getUserCount(); i++) {
            users[i].displayDetails();                    // overridden method, runtime binding
            System.out.println("   Permissions: " + users[i].getPermissions());
        }
    }

    private static void showSystemStatus() {
        System.out.println("\n--- System Status and Statistics ---");
        System.out.println(systemAdmin.checkSystemStatus(library, today));
        double percentage = library.getAvailabilityPercentage();
        int roundedPercentage = (int) percentage;         // type casting: double to int
        System.out.println("Books available right now: " + roundedPercentage + "%");
    }

    private static void advanceDay() {
        int days = readInt("How many days do you want to move forward? ");
        if (days > 0 && days <= 365) {
            today = today + days;
            System.out.println("System day is now " + today + ".");
        } else {
            System.out.println("Please enter a number between 1 and 365.");
        }
    }
}
