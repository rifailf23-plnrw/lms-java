package lms.data;

import lms.model.Book;
import lms.model.Student;
import lms.model.Transaction;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {
    public static class ImportError {
        public final String file;
        public final int lineNumber;
        public String rawLine;
        public final String reason;

        public ImportError(String file, int lineNumber, String rawLine, String reason) {
            this.file = file;
            this.lineNumber = lineNumber;
            this.rawLine = rawLine;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return String.format("[%s] Line %d: %s → %s", file, lineNumber, rawLine, reason);
        }

    }

    //Load Books
    public static List<ImportError> loadBooks(String filePath, List<Book> target) {
        List<ImportError> errors = new ArrayList<>();
        target.clear();

        try (BufferedReader br = Files.newBufferedReader(Path.of(filePath))) {
            String line;
            int lineNumber = 0;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (firstLine) {firstLine = false; continue;}
                if (line.isBlank()) continue;

                String[] parts = line.split(",", -1);

                if (parts.length < 6) {
                    errors.add(new ImportError("book.csv", lineNumber, line, "Expected at least 6 columns, found " + parts.length));
                    continue;
                }

                String bookID = parts[0].trim();
                String title =  parts[1].trim();
                String isbn13 = parts[2].trim();
                String author = parts[3].trim();
                String copiesStr = parts[4].trim();
                String availStr = parts[5].trim();
                double price = 0.0;
                if (parts.length >= 7) {
                    try {price = Double.parseDouble(parts[6].trim());}
                    catch (NumberFormatException ignored) {}
                }

                boolean valid = true;

                //Validate ISBN-13
                if(!Book.isValidIsbn13(isbn13)) {
                    errors.add(new ImportError("book.csv", lineNumber, line, "isbn13 is invalid: '" + isbn13 + "'"));
                    valid = false;
                }

                //Validate copies
                int copies = 0;
                try {copies = Integer.parseInt(copiesStr);
                    if (!Book.isValidCopies(copies)) {
                        errors.add(new ImportError("book.csv", lineNumber, line, "copies is invalid: '" + copies + "'"));
                        valid = false;
                    }}
                catch (NumberFormatException e) {
                    errors.add(new ImportError("book.csv", lineNumber, line, "Availability is not a number: '" + availStr + "'"));
                    valid = false;
                }

                //Validate Availability
                int avail = 0;
                try {
                    avail = Integer.parseInt(availStr);
                    if(!Book.isValidAvailability(avail, copies)) {
                        errors.add(new ImportError("book.csv", lineNumber, line, "Invalid availability: " + avail + "(must be 0-" + copies + ")"));
                        valid = false;
                    }
                }
                catch (NumberFormatException e) {
                    errors.add(new ImportError("book.csv", lineNumber, line, "Availability is not a number: '" + availStr + "'"));
                    valid = false;
                }

                if (valid) {
                    target.add(new Book(bookID, title,isbn13, author, copies, avail, price));
                }
            }
        }
        catch (IOException e) {
            errors.add(new ImportError("book.csv", 0, "", "Could not read file: " + e.getMessage()));
        }
        return errors;
    }

    //Load Students
    public static List<ImportError> loadStudents(String filePath, List<Student> target) {
        List<ImportError> errors = new ArrayList<>();
        target.clear();

        try (BufferedReader br = Files.newBufferedReader(Path.of(filePath))) {
            String line;
            int lineNum = 0;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                lineNum++;
                if (firstLine) { firstLine = false; continue; }
                if (line.isBlank()) continue;

                String[] parts = line.split(",", -1);

                if (parts.length < 2) {
                    errors.add(new ImportError("student.csv", lineNum, line,
                            "Expected 2 columns, found " + parts.length));
                    continue;
                }

                String studentId  = parts[0].trim();
                String firstName  = parts[1].trim();

                // Validate student ID
                if (!Student.isValidStudentID(studentId)) {
                    errors.add(new ImportError("student.csv", lineNum, line,
                            "Invalid Student ID: '" + studentId + "' (must be 8 digits)"));
                } else {
                    target.add(new Student(studentId, firstName));
                }
            }
        } catch (IOException e) {
            errors.add(new ImportError("student.csv", 0, "",
                    "Could not read file: " + e.getMessage()));
        }
        return errors;
    }

    //Load Transactions
    public static List<ImportError> loadTransactions(String filePath, List<Transaction> target) {
        List<ImportError> errors = new ArrayList<>();
        target.clear();

        try (BufferedReader br = Files.newBufferedReader(Path.of(filePath))) {
            String line;
            int lineNum = 0;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                lineNum++;
                if (firstLine) { firstLine = false; continue; }
                if (line.isBlank()) continue;

                String[] parts = line.split(",", -1);

                if (parts.length < 4) {
                    errors.add(new ImportError("transactions.csv", lineNum, line,
                            "Expected 4 columns, found " + parts.length));
                    continue;
                }

                String date      = parts[0].trim();
                String bookId    = parts[1].trim();
                String studentId = parts[2].trim();
                String type      = parts[3].trim();

                // Validate date
                if (!Transaction.isValidDate(date)) {
                    errors.add(new ImportError("transactions.csv", lineNum, line,
                            "Invalid date: '" + date + "' (must be DD/MM/YYYY)"));
                } else {
                    target.add(new Transaction(date, bookId, studentId, type));
                }
            }
        } catch (IOException e) {
            errors.add(new ImportError("transactions.csv", 0, "",
                    "Could not read file: " + e.getMessage()));
        }
        return errors;
    }
}
