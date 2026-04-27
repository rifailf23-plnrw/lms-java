package lms.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Transaction {
    public static final String TYPE_ISSUE = "1";
    public static final String TYPE_RETURN = "2";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    //Fields
    private String date;
    private String bookID;
    private String studentID;
    private String type;

    //Constructor
    public Transaction(String date, String bookID, String studentID, String type) {
        this.date = date;
        this.bookID = bookID;
        this.studentID = studentID;
        this.type = type;
    }

    //Getters & Setters
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getBookId() {
        return bookID;
    }
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getStudentId() {
        return studentID;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    //Convenience helpers
    public boolean isIssue() {
        return TYPE_ISSUE.equals(type);
    }
    public boolean isReturn() {
        return TYPE_RETURN.equals(type);
    }

    public LocalDate getParsedDate() {
        try {
            return LocalDate.parse(date, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    //Validation
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null) return false;
        try {
            LocalDate.parse(dateStr, DATE_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    //toString
    @Override
    public String toString() {
        String typeLabel = TYPE_ISSUE.equals(type) ? "Issue" : "Return";
        return String.format("Transaction[%s, book=%s, student=%s, %s]", date, bookID, studentID, typeLabel);
    }
}
