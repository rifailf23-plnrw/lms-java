package lms.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class TransactionTest {

//Data validation Tests

    @Test
    void testValidDate_Normal() {
        assertTrue(Transaction.isValidDate("16/04/2026"),
                "Valid DD/MM/YYYY date should return true");
    }

    @Test
    void testValidDate_FirstOfJanuary() {
        assertTrue(Transaction.isValidDate("01/01/2026"),
                "01/01/2026 should be valid");
    }

    @Test
    void testInvalidDate_WrongFormat_YYYYMMDD() {
        assertFalse(Transaction.isValidDate("2026/04/16"),
                "YYYY/MM/DD format should be rejected");
    }

    @Test
    void testInvalidDate_WrongFormat_MMDDYYYY() {
        assertFalse(Transaction.isValidDate("04/16/2026"),
                "MM/DD/YYYY format should be rejected");
    }

    @Test
    void testInvalidDate_Day32() {
        assertFalse(Transaction.isValidDate("32/01/2026"),
                "Day 32 does not exist — should be invalid");
    }

    @Test
    void testInvalidDate_Month13() {
        assertFalse(Transaction.isValidDate("01/13/2026"),
                "Month 13 does not exist — should be invalid");
    }

    @Test
    void testInvalidDate_LeapYearInvalid() {
        // 29/02/2025 — 2025 is not a leap year
        assertFalse(Transaction.isValidDate("29/02/2025"),
                "29 Feb on a non-leap year should be invalid");
    }

    @Test
    void testValidDate_LeapYear() {
        // 29/02/2028 — 2028 IS a leap year
        assertTrue(Transaction.isValidDate("29/02/2028"),
                "29 Feb on a leap year should be valid");
    }

    @Test
    void testInvalidDate_Empty() {
        assertFalse(Transaction.isValidDate(""),
                "Empty string should be invalid");
    }

    @Test
    void testInvalidDate_Null() {
        assertFalse(Transaction.isValidDate(null),
                "Null should be invalid");
    }

    @Test
    void testInvalidDate_TextInsteadOfDate() {
        assertFalse(Transaction.isValidDate("tomorrow"),
                "Plain text should be invalid");
    }

    //Type Helper Tests

    @Test
    void testIsIssue_TypeOne() {
        Transaction t = new Transaction("01/04/2026", "AB01", "12345678", "1");
        assertTrue(t.isIssue(),  "Type 1 should be an issue");
        assertFalse(t.isReturn(),"Type 1 should not be a return");
    }

    @Test
    void testIsReturn_TypeTwo() {
        Transaction t = new Transaction("05/04/2026", "AB01", "12345678", "2");
        assertTrue(t.isReturn(), "Type 2 should be a return");
        assertFalse(t.isIssue(), "Type 2 should not be an issue");
    }

    //getParsedDate Tests

    @Test
    void testGetParsedDate_ValidDate() {
        Transaction t = new Transaction("16/04/2026", "AB01", "12345678", "1");
        LocalDate parsed = t.getParsedDate();
        assertNotNull(parsed, "Valid date should parse successfully");
        assertEquals(LocalDate.of(2026, 4, 16), parsed);
    }

    @Test
    void testGetParsedDate_InvalidDate_ReturnsNull() {
        Transaction t = new Transaction("99/99/9999", "AB01", "12345678", "1");
        assertNull(t.getParsedDate(),
                "Invalid date string should return null from getParsedDate()");
    }

    //Constructor and Getter Tests

    @Test
    void testTransactionConstructorAndGetters() {
        Transaction t = new Transaction("01/04/2026", "AB01", "12345678", "1");
        assertEquals("01/04/2026", t.getDate());
        assertEquals("AB01",       t.getBookId());
        assertEquals("12345678",   t.getStudentId());
        assertEquals("1",          t.getType());
    }
}