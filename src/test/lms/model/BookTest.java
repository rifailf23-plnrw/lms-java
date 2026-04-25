package lms.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

//ISBN-13 Validation Test
@Test
void isValidIsbn13_GreatGatsby() {
    // 9780743273565 — real ISBN, valid check digit
    assertTrue(Book.isValidIsbn13("9780743273565"),
            "Valid ISBN-13 should return true");
}

    @Test
    void isValidIsbn13_HarryPotter() {
        // 9780747532743 — Harry Potter and the Philosopher's Stone
        assertTrue(Book.isValidIsbn13("9780747532743"),
                "Valid ISBN-13 should return true");
    }

    @Test
    void isValidIsbn13_WithHyphens() {
        // Hyphens should be stripped before validation
        assertTrue(Book.isValidIsbn13("978-0-7475-3274-3"),
                "ISBN-13 with hyphens should be accepted");
    }

    @Test
    void isInvalidIsbn13_WrongCheckDigit() {
        // Change last digit to make check digit wrong
        assertFalse(Book.isValidIsbn13("9780747532741"),
                "ISBN-13 with wrong check digit should return false");
    }

    @Test
    void isInvalidIsbn13_TooShort() {
        assertFalse(Book.isValidIsbn13("978074753274"),
                "ISBN-13 with only 12 digits should return false");
    }

    @Test
    void isInvalidIsbn13_TooLong() {
        assertFalse(Book.isValidIsbn13("97807475327430"),
                "ISBN-13 with 14 digits should return false");
    }

    @Test
    void isInvalidIsbn13_ContainsLetters() {
        assertFalse(Book.isValidIsbn13("978074753274X"),
                "ISBN-13 with letters should return false");
    }

    @Test
    void isInvalidIsbn13_Null() {
        assertFalse(Book.isValidIsbn13(null),
                "Null ISBN-13 should return false");
    }

    @Test
    void isInvalidIsbn13_Empty() {
        assertFalse(Book.isValidIsbn13(""),
                "Empty ISBN-13 should return false");
    }

    //Copies Validation Tests

    @Test
    void isValidCopies_One() {
        assertTrue(Book.isValidCopies(1),
                "1 copy should be valid");
    }

    @Test
    void isValidCopies_Two() {
        assertTrue(Book.isValidCopies(2),
                "2 copies should be valid");
    }

    @Test
    void isInvalidCopies_Zero() {
        assertFalse(Book.isValidCopies(0),
                "0 copies should be invalid");
    }

    @Test
    void isInvalidCopies_Three() {
        assertFalse(Book.isValidCopies(3),
                "3 copies should be invalid (max is 2)");
    }

    @Test
    void isInvalidCopies_Negative() {
        assertFalse(Book.isValidCopies(-1),
                "Negative copies should be invalid");
    }

    //Availability Validation Tests

    @Test
    void isValidAvailability_EqualsCopies() {
        assertTrue(Book.isValidAvailability(2, 2),
                "Availability equal to copies should be valid");
    }

    @Test
    void isValidAvailability_Zero() {
        assertTrue(Book.isValidAvailability(0, 2),
                "Zero availability should be valid (all copies out on loan)");
    }

    @Test
    void isValidAvailability_OneLessThanCopies() {
        assertTrue(Book.isValidAvailability(1, 2),
                "Availability of 1 with 2 copies should be valid");
    }

    @Test
    void isInvalidAvailability_ExceedsCopies() {
        assertFalse(Book.isValidAvailability(3, 2),
                "Availability exceeding copies should be invalid");
    }

    @Test
    void isInvalidAvailability_Negative() {
        assertFalse(Book.isValidAvailability(-1, 2),
                "Negative availability should be invalid");
    }

    //Constructor and Getters Tests

    @Test
    void testBookConstructorAndGetters() {
        Book book = new Book("AB01", "The Great Gatsby",
                "9780743273565", "F Scott Fitzgerald", 2, 2, 12.99);

        assertEquals("AB01",            book.getBookId());
        assertEquals("The Great Gatsby",book.getTitle());
        assertEquals("9780743273565",   book.getIsbn13());
        assertEquals("F Scott Fitzgerald", book.getAuthor());
        assertEquals(2,                 book.getCopies());
        assertEquals(2,                 book.getAvailability());
        assertEquals(12.99,             book.getPrice(), 0.001);
    }

    @Test
    void testBookSetters() {
        Book book = new Book("AB01", "Old Title",
                "9780743273565", "Old Author", 1, 1, 5.00);

        book.setTitle("New Title");
        book.setAvailability(0);
        book.setPrice(9.99);

        assertEquals("New Title", book.getTitle());
        assertEquals(0,           book.getAvailability());
        assertEquals(9.99,        book.getPrice(), 0.001);
    }
}