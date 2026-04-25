package lms.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    //Student ID Validation Tests

    @Test
    void testValidStudentId_EightDigits() {
        assertTrue(Student.isValidStudentID("12345678"),
                "8-digit student ID should be valid");
    }

    @Test
    void testValidStudentId_AllZeros() {
        assertTrue(Student.isValidStudentID("00000000"),
                "00000000 is technically valid — 8 digits");
    }

    @Test
    void testInvalidStudentId_TooShort() {
        assertFalse(Student.isValidStudentID("1234567"),
                "7-digit ID should be invalid");
    }

    @Test
    void testInvalidStudentId_TooLong() {
        assertFalse(Student.isValidStudentID("123456789"),
                "9-digit ID should be invalid");
    }

    @Test
    void testInvalidStudentId_ContainsLetters() {
        assertFalse(Student.isValidStudentID("1234567A"),
                "ID with letters should be invalid");
    }

    @Test
    void testInvalidStudentId_ContainsSpaces() {
        assertFalse(Student.isValidStudentID("1234 678"),
                "ID with spaces should be invalid");
    }

    @Test
    void testInvalidStudentId_Empty() {
        assertFalse(Student.isValidStudentID(""),
                "Empty string should be invalid");
    }

    @Test
    void testInvalidStudentId_Null() {
        assertFalse(Student.isValidStudentID(null),
                "Null should be invalid");
    }

    //Constructor and Getter Tests

    @Test
    void testStudentConstructorAndGetters() {
        Student student = new Student("12345678", "Alice");
        assertEquals("12345678", student.getStudentId());
        assertEquals("Alice",    student.getFirstName());
    }

    @Test
    void testStudentSetters() {
        Student student = new Student("12345678", "Alice");
        student.setFirstName("Bob");
        student.setStudentID("87654321");
        assertEquals("Bob",      student.getFirstName());
        assertEquals("87654321", student.getStudentId());
    }
}