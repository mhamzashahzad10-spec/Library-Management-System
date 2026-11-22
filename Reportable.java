package model;

/**
 * Interface: any class that can describe itself as a one-line report.
 * Implemented by Book and BorrowingRecord.
 */
public interface Reportable {
    String generateReport();
}
