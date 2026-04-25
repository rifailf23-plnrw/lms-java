package lms.model;

public class Book {
    //Fields
    private String bookID;
    private String title;
    private String isbn13;
    private String author;
    private int copies;
    private int availability;
    private double price;

    //Constructor
    public Book(String bookID, String title, String isbn13, String author, int copies, int availability, double price) {
        this.bookID = bookID;
        this.title = title;
        this.isbn13 = isbn13;
        this.author = author;
        this.copies = copies;
        this.availability = availability;
        this.price = price;
    }

    //Getters & Setters
    public String getBookId() {
        return bookID;
    }
    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn13() {
        return isbn13;
    }
    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCopies() {
        return copies;
    }
    public void setCopies(int copies) {
        this.copies = copies;
    }

    public int getAvailability() {
        return availability;
    }
    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    //Validation

    /* Validates ISBN-13 using the official check digit algorithm.
    Alternating weights of 1 and 3; total must be divisible by 10
     */
    public static boolean isValidIsbn13(String isbn) {
        if(isbn == null) return false;
        isbn = isbn.replaceAll("[\\s-]", "");
        if (!isbn.matches("\\d{13}")) return false;
        int total = 0;
        for(int i = 0; i < 13; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            total += (i % 2 == 0) ? digit : digit * 3;
        }
        return total % 10 == 0;
    }

    //Validates copies count: must be 1 or 2.
    public static boolean isValidCopies(int copies) {
        return copies >= 1 && copies <= 2;
    }

    //Validates availability: cannot be negative, cannot exceed copies.
    public static boolean isValidAvailability(int availability, int copies) {
        return availability >= 0 && availability <= copies;
    }

    //toString for debugging
    @Override
    public String toString() {
        return String.format("Book[%s, %s, isbn=%s, copies=%d, avail=%d, price=%.2f]",
                bookID, title, isbn13, copies, availability, price);
    }
}
