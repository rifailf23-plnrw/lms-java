package lms.model;

public class Student {
    //Fields
    private String studentID;
    private String firstName;

    //Constructor
    public Student(String studentID, String firstName) {
        this.studentID = studentID;
        this.firstName = firstName;
    }

    //Getters & Setters
    public String getStudentId() {
        return studentID;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //Validation

    //Student ID must be exactly 8 digits.
    public static boolean isValidStudentID(String id) {
        return id != null && id.matches("\\d{8}");
    }

    //toString
    @Override
    public String toString() {
        return String.format("Student[%s, %s]", studentID, firstName);
    }
}
