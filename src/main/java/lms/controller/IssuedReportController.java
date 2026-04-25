package lms.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lms.data.DataStore;
import lms.model.Book;
import lms.model.Student;
import lms.model.Transaction;

import java.util.List;

public class IssuedReportController {
    @FXML private TextField dateField;
    @FXML private Label     statusLabel;

    @FXML private TableView<Transaction> resultsTable;
    @FXML private TableColumn<Transaction, String> colBookId;
    @FXML private TableColumn<Transaction, String> colTitle;
    @FXML private TableColumn<Transaction, String> colAuthor;
    @FXML private TableColumn<Transaction, String> colStudentId;
    @FXML private TableColumn<Transaction, String> colName;
    @FXML private TableColumn<Transaction, String> colDate;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        // Use lambda cell factories to look up related data from DataStore
        colBookId   .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBookId()));
        colStudentId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStudentId()));
        colDate     .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate()));

        colTitle.setCellValueFactory(c -> {
            Book b = store.findBookByID(c.getValue().getBookId());
            return new SimpleStringProperty(b != null ? b.getTitle() : "Unknown");
        });
        colAuthor.setCellValueFactory(c -> {
            Book b = store.findBookByID(c.getValue().getBookId());
            return new SimpleStringProperty(b != null ? b.getAuthor() : "Unknown");
        });
        colName.setCellValueFactory(c -> {
            Student s = store.findStudentByID(c.getValue().getStudentId());
            return new SimpleStringProperty(s != null ? s.getFirstName() : "Unknown");
        });
    }

    @FXML
    private void onSearch() {
        String date = dateField.getText().trim();

        if (!Transaction.isValidDate(date)) {
            statusLabel.setText("Invalid date. Use DD/MM/YYYY format.");
            statusLabel.setStyle("-fx-text-fill: #cc2200;");
            return;
        }

        // Filter: only issue transactions (type "1") on the given date
        List<Transaction> results = store.getTransactions().stream()
                .filter(t -> t.isIssue() && t.getDate().equals(date))
                .toList();

        resultsTable.setItems(FXCollections.observableArrayList(results));

        if (results.isEmpty()) {
            statusLabel.setText("No books were issued on " + date + ".");
        } else {
            statusLabel.setText(results.size() + " book(s) issued on " + date + ".");
        }
        statusLabel.setStyle("-fx-text-fill: #225522;");
    }
}
