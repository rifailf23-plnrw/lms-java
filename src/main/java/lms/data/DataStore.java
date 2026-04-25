package lms.data;

import lms.model.Book;
import lms.model.Student;
import lms.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    //Singleton
    private static DataStore instance;

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    //Private constructor prevents direct instantiation
    private DataStore() {}

    //Data Lists
    private final List<Book> books = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();

    //Books
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> newBooks) {
        books.clear();
        books.addAll(newBooks);
    }

    public Book findBookByID(String bookID) {
        return books.stream()
                .filter(book -> book.getBookId().equalsIgnoreCase(bookID))
                .findFirst()
                .orElse(null);
    }

    //Students
    public List<Student> getStudents() {
        return students;
    }
    public void setStudents(List<Student> newStudents) {
        students.clear();
        students.addAll(newStudents);
    }

    public Student findStudentByID(String studentID) {
        return students.stream()
                .filter(student -> student.getStudentId().equals(studentID))
                .findFirst()
                .orElse(null);
    }

    //Transactions
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> newTransactions) {
        transactions.clear();
        transactions.addAll(newTransactions);
    }

    //Utility
    //returns true if all three files have been loaded successfully.
    public boolean isDataLoaded() {
        return !books.isEmpty() || !students.isEmpty() || !transactions.isEmpty();
    }

    //Clears all data (used when re-importing).
    public void clearAll() {
        books.clear();
        students.clear();
        transactions.clear();
    }
}
