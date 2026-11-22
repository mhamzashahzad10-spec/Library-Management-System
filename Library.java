package model;

/**
 * Library: the main manager class of the system.
 * It stores arrays of Book, Student, User and BorrowingRecord objects.
 */
public class Library {

    public static final int MAX_BOOKS = 50;
    public static final int MAX_USERS = 50;
    public static final int MAX_RECORDS = 100;

    private Book[] books;
    private Student[] students;
    private User[] users;
    private BorrowingRecord[] records;
    private int bookCount;
    private int studentCount;
    private int userCount;
    private int recordCount;

    public Library() {
        books = new Book[MAX_BOOKS];
        students = new Student[MAX_USERS];
        users = new User[MAX_USERS];
        records = new BorrowingRecord[MAX_RECORDS];
        bookCount = 0;
        studentCount = 0;
        userCount = 0;
        recordCount = 0;
    }

    // ---------- getters ----------
    public Book[] getBooks() {
        return books;
    }

    public User[] getUsers() {
        return users;
    }

    public BorrowingRecord[] getRecords() {
        return records;
    }

    public int getBookCount() {
        return bookCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public int getRecordCount() {
        return recordCount;
    }

    // ---------- users ----------

    // Adds a staff member (Librarian, Administrator ...)
    public boolean addUser(User user) {
        if (userCount >= MAX_USERS || findUserById(user.getUserId()) != null) {
            return false;
        }
        users[userCount] = user;
        userCount++;
        return true;
    }

    // Use case: Register Account
    public String registerStudent(Student student) {
        if (userCount >= MAX_USERS) {
            return "FAILED: The system cannot store more users.";
        }
        if (findUserById(student.getUserId()) != null) {
            return "FAILED: This ID is already used.";
        }
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equals(student.getUsername())) {
                return "FAILED: This username is already taken.";
            }
        }
        students[studentCount] = student;
        studentCount++;
        users[userCount] = student;
        userCount++;
        return "SUCCESS: Account registered for " + student.getName() + ".";
    }

    // Use case: Login. Returns the User object, or null when the details are wrong.
    public User login(String username, String password) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].login(username, password)) {
                return users[i];
            }
        }
        return null;
    }

    public User findUserById(int userId) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUserId() == userId) {
                return users[i];
            }
        }
        return null;
    }

    public Student findStudentById(int studentId) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getUserId() == studentId) {
                return students[i];
            }
        }
        return null;
    }

    // ---------- books (Manage Books) ----------
    public boolean addBook(Book book) {
        if (bookCount >= MAX_BOOKS || findBookById(book.getBookId()) != null) {
            return false;
        }
        books[bookCount] = book;
        bookCount++;
        return true;
    }

    // A book can be removed only when it is Available (state diagram: Available -> Removed)
    public String removeBook(int bookId) {
        Book book = findBookById(bookId);
        if (book == null || book.isRemoved()) {
            return "FAILED: No book found with ID " + bookId + ".";
        }
        if (book.markRemoved()) {
            return "SUCCESS: Book '" + book.getTitle() + "' was removed.";
        }
        return "FAILED: The book is currently " + book.getStatus()
                + " and cannot be removed.";
    }

    public Book findBookById(int bookId) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getBookId() == bookId) {
                return books[i];
            }
        }
        return null;
    }

    // ---------- Search Book (overloaded) ----------

    // Search by one keyword (title, author or category)
    public Book[] searchBooks(String keyword) {
        int matches = 0;
        for (int i = 0; i < bookCount; i++) {
            if (!books[i].isRemoved() && books[i].matches(keyword)) {
                matches++;
            }
        }
        Book[] result = new Book[matches];
        int index = 0;
        for (int i = 0; i < bookCount; i++) {
            if (!books[i].isRemoved() && books[i].matches(keyword)) {
                result[index] = books[i];
                index++;
            }
        }
        return result;
    }

    // Search by keyword and also filter by an exact category
    public Book[] searchBooks(String keyword, String category) {
        Book[] byKeyword = searchBooks(keyword);
        int matches = 0;
        for (int i = 0; i < byKeyword.length; i++) {
            if (byKeyword[i].getCategory().equalsIgnoreCase(category)) {
                matches++;
            }
        }
        Book[] result = new Book[matches];
        int index = 0;
        for (int i = 0; i < byKeyword.length; i++) {
            if (byKeyword[i].getCategory().equalsIgnoreCase(category)) {
                result[index] = byKeyword[i];
                index++;
            }
        }
        return result;
    }

    // ---------- methods that receive an ARRAY as a parameter ----------

    // Prints every book of the given array
    public void printBooks(Book[] list) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                System.out.println("  " + list[i].generateReport());
            }
        }
    }

    // Counts the Available books in the given array
    public int countAvailable(Book[] list) {
        int count = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null && list[i].isAvailable()) {
                count++;
            }
        }
        return count;
    }

    // Counts the overdue records in the given array
    public int countOverdue(BorrowingRecord[] list, int today) {
        int count = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null && list[i].isOverdue(today)) {
                count++;
            }
        }
        return count;
    }

    // Percentage of Available books (removed books are not counted). Uses TYPE CASTING.
    public double getAvailabilityPercentage() {
        int total = 0;
        for (int i = 0; i < bookCount; i++) {
            if (!books[i].isRemoved()) {
                total++;
            }
        }
        if (total == 0) {
            return 0.0;
        }
        int available = countAvailable(books);
        return (double) available / total * 100;
    }

    // ---------- Issue Book ----------
    public String issueBook(int bookId, int studentId, int today) {
        Book book = findBookById(bookId);
        if (book == null || book.isRemoved()) {
            return "FAILED: No book found with ID " + bookId + ".";
        }
        if (!book.isAvailable()) {
            return "FAILED: The book is already " + book.getStatus()
                    + ". The librarian informs the student.";
        }
        Student student = findStudentById(studentId);
        if (student == null) {
            return "FAILED: No student record found with ID " + studentId + ".";
        }
        if (!student.hasValidAccount()) {
            return "FAILED: The student does not have a valid account. Book cannot be issued.";
        }
        if (recordCount >= MAX_RECORDS) {
            return "FAILED: The system cannot store more borrowing records.";
        }
        BorrowingRecord record = new BorrowingRecord(recordCount + 1, book, student, today);
        records[recordCount] = record;
        recordCount++;
        book.markBorrowed();
        student.increaseBorrowed();
        return "SUCCESS: '" + book.getTitle() + "' issued to " + student.getName()
                + ". Due on day " + record.getDueDay() + ".";
    }

    // ---------- Return Book ----------
    public String returnBook(int bookId, int today) {
        Book book = findBookById(bookId);
        if (book == null || book.isRemoved()) {
            return "FAILED: No book found with ID " + bookId + ".";
        }
        BorrowingRecord record = null;
        for (int i = 0; i < recordCount; i++) {
            if (records[i].getBook().getBookId() == bookId && !records[i].isReturned()) {
                record = records[i];
            }
        }
        if (record == null) {
            return "FAILED: This book is not currently borrowed.";
        }
        record.closeRecord(today);
        book.markReturned();
        record.getStudent().decreaseBorrowed();
        double fine = record.calculateFine(today);
        int wholeFine = (int) fine;   // type casting: double to int
        String message = "SUCCESS: '" + book.getTitle() + "' returned by "
                + record.getStudent().getName() + " on day " + today + ".";
        if (wholeFine > 0) {
            message = message + " Late fine: Rs. " + wholeFine;
        } else {
            message = message + " No fine.";
        }
        return message;
    }

    // ---------- Track Overdue Books ----------
    public int trackOverdue(int today) {
        int total = countOverdue(records, today);
        for (int i = 0; i < recordCount; i++) {
            if (records[i].isOverdue(today)) {
                records[i].getBook().markOverdue();
                System.out.println("  OVERDUE -> " + records[i].generateReport()
                        + " | Fine so far: Rs. " + (int) records[i].calculateFine(today));
            }
        }
        return total;
    }

    // ---------- View Borrowing History ----------
    public int viewBorrowingHistory(int studentId) {
        int found = 0;
        for (int i = 0; i < recordCount; i++) {
            if (records[i].getStudent().getUserId() == studentId) {
                System.out.println("  " + records[i].generateReport());
                found++;
            }
        }
        return found;
    }
}
