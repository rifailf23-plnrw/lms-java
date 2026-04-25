package lms.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import lms.model.Book;
import lms.model.Student;
import lms.model.Transaction;

import java.util.List;


class DataStoreTest {

    private DataStore store;

    //Runs before every test - gives a  clean state
    @BeforeEach
    void setUp() {
        store = DataStore.getInstance();
        store.clearAll();
    }

    //Singleton Tests

    @Test
    void testSingleton_SameInstance() {
        DataStore a = DataStore.getInstance();
        DataStore b = DataStore.getInstance();
        assertSame(a, b,
                "getInstance() should always return the same object");
    }

    //Book Operations

    @Test
    void testSetAndGetBooks() {
        List<Book> books = List.of(
                new Book("AB01", "The Great Gatsby", "9780743273565",
                        "F Scott Fitzgerald", 2, 2, 12.99)
        );
        store.setBooks(books);
        assertEquals(1, store.getBooks().size());
        assertEquals("AB01", store.getBooks().get(0).getBookId());
    }

    @Test
    void testFindBookById_Found() {
        store.setBooks(List.of(
                new Book("AB01", "1984", "9780451524935", "George Orwell", 2, 2, 9.99)
        ));
        Book found = store.findBookByID("AB01");
        assertNotNull(found, "Should find book with ID AB01");
        assertEquals("1984", found.getTitle());
    }

    @Test
    void testFindBookById_CaseInsensitive() {
        store.setBooks(List.of(
                new Book("AB01", "1984", "9780451524935", "George Orwell", 2, 2, 9.99)
        ));
        // Lowercase lookup should still work
        assertNotNull(store.findBookByID("ab01"),
                "Book lookup should be case-insensitive");
    }

    @Test
    void testFindBookById_NotFound() {
        store.setBooks(List.of(
                new Book("AB01", "1984", "9780451524935", "George Orwell", 2, 2, 9.99)
        ));
        assertNull(store.findBookByID("ZZ99"),
                "Should return null for unknown book ID");
    }

    //Student Operations

    @Test
    void testSetAndGetStudents() {
        store.setStudents(List.of(new Student("12345678", "Alice")));
        assertEquals(1, store.getStudents().size());
    }

    @Test
    void testFindStudentById_Found() {
        store.setStudents(List.of(new Student("12345678", "Alice")));
        Student s = store.findStudentByID("12345678");
        assertNotNull(s);
        assertEquals("Alice", s.getFirstName());
    }

    @Test
    void testFindStudentById_NotFound() {
        store.setStudents(List.of(new Student("12345678", "Alice")));
        assertNull(store.findStudentByID("99999999"),
                "Should return null for unknown student ID");
    }

    //Transaction Operations

    @Test
    void testSetAndGetTransactions() {
        store.setTransactions(List.of(
                new Transaction("01/04/2026", "AB01", "12345678", "1")
        ));
        assertEquals(1, store.getTransactions().size());
    }

    //isDataLoaded and clearAll Tests

    @Test
    void testIsDataLoaded_WhenEmpty() {
        assertFalse(store.isDataLoaded(),
                "Store should not be considered loaded when empty");
    }

    @Test
    void testIsDataLoaded_AfterAddingBooks() {
        store.setBooks(List.of(
                new Book("AB01", "1984", "9780451524935", "George Orwell", 2, 2, 9.99)
        ));
        assertTrue(store.isDataLoaded(),
                "Store should be considered loaded after books are added");
    }

    @Test
    void testClearAll_RemovesEverything() {
        store.setBooks(List.of(
                new Book("AB01", "1984", "9780451524935", "George Orwell", 2, 2, 9.99)
        ));
        store.setStudents(List.of(new Student("12345678", "Alice")));
        store.clearAll();

        assertEquals(0, store.getBooks().size());
        assertEquals(0, store.getStudents().size());
        assertEquals(0, store.getTransactions().size());
        assertFalse(store.isDataLoaded());
    }
}