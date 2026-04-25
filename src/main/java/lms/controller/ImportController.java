package lms.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lms.data.CsvLoader;
import lms.data.CsvLoader.ImportError;
import lms.data.DataStore;
import lms.model.Book;
import lms.model.Student;
import lms.model.Transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportController {
    // ── FXML Injected fields ─────────────────────────────────
    @FXML private TextField   folderPathField;
    @FXML private Label       statusLabel;

    // Books table
    @FXML private TableView<Book>        booksTable;
    @FXML private TableColumn<Book, String> colBookId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colCopies;
    @FXML private TableColumn<Book, Integer> colAvail;
    @FXML private TableColumn<Book, Double>  colPrice;

    // Students table
    @FXML private TableView<Student>         studentsTable;
    @FXML private TableColumn<Student, String> colStudentId;
    @FXML private TableColumn<Student, String> colFirstName;

    // Transactions table
    @FXML private TableView<Transaction>         transactionsTable;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, String> colTxBookId;
    @FXML private TableColumn<Transaction, String> colTxStudId;
    @FXML private TableColumn<Transaction, String> colType;

    // Errors table
    @FXML private TableView<ImportError>         errorsTable;
    @FXML private TableColumn<ImportError, String> colErrFile;
    @FXML private TableColumn<ImportError, Integer> colErrLine;
    @FXML private TableColumn<ImportError, String> colErrReason;
    @FXML private TableColumn<ImportError, String> colErrRaw;

    @FXML private TextField editField;

    // ── Internal state ───────────────────────────────────────
    private final DataStore store = DataStore.getInstance();
    private final List<ImportError> allErrors = new ArrayList<>();
    private ImportError selectedError = null;

    // ── Initialise table columns ──────────────────────────────
    @FXML
    public void initialize() {
        // Books
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle .setCellValueFactory(new PropertyValueFactory<>("title"));
        colIsbn  .setCellValueFactory(new PropertyValueFactory<>("isbn13"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCopies.setCellValueFactory(new PropertyValueFactory<>("copies"));
        colAvail .setCellValueFactory(new PropertyValueFactory<>("availability"));
        colPrice .setCellValueFactory(new PropertyValueFactory<>("price"));

        // Students
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        // Transactions
        colDate     .setCellValueFactory(new PropertyValueFactory<>("date"));
        colTxBookId .setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTxStudId .setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colType     .setCellValueFactory(new PropertyValueFactory<>("type"));

        // Errors
        colErrFile  .setCellValueFactory(new PropertyValueFactory<>("file"));
        colErrLine  .setCellValueFactory(new PropertyValueFactory<>("lineNumber"));
        colErrReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colErrRaw   .setCellValueFactory(new PropertyValueFactory<>("rawLine"));
    }

    // ── Browse for folder ────────────────────────────────────
    @FXML
    private void onBrowseFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select folder containing CSV files");
        File dir = chooser.showDialog(folderPathField.getScene().getWindow());
        if (dir != null) {
            folderPathField.setText(dir.getAbsolutePath());
        }
    }

    // ── Import CSV files ─────────────────────────────────────
    @FXML
    private void onImport() {
        String folder = folderPathField.getText().trim();
        if (folder.isEmpty()) {
            showStatus("Please select a folder first.", true);
            return;
        }

        store.clearAll();
        allErrors.clear();

        // Load each file
        List<Book>        books        = new ArrayList<>();
        List<Student>     students     = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();

        allErrors.addAll(CsvLoader.loadBooks(
                folder + File.separator + "book.csv", books));
        allErrors.addAll(CsvLoader.loadStudents(
                folder + File.separator + "student.csv", students));
        allErrors.addAll(CsvLoader.loadTransactions(
                folder + File.separator + "transactions.csv", transactions));

        store.setBooks(books);
        store.setStudents(students);
        store.setTransactions(transactions);

        // Populate tables
        booksTable.setItems(FXCollections.observableArrayList(books));
        studentsTable.setItems(FXCollections.observableArrayList(students));
        transactionsTable.setItems(FXCollections.observableArrayList(transactions));
        errorsTable.setItems(FXCollections.observableArrayList(allErrors));

        // Status message
        String msg = String.format(
                "Loaded: %d books, %d students, %d transactions.  Errors: %d",
                books.size(), students.size(), transactions.size(), allErrors.size());
        showStatus(msg, !allErrors.isEmpty());
    }

    // ── Select error row for editing ─────────────────────────
    @FXML
    private void onErrorSelected() {
        selectedError = errorsTable.getSelectionModel().getSelectedItem();
        if (selectedError != null) {
            editField.setText(selectedError.rawLine);
        }
    }

    // ── Save inline correction ────────────────────────────────
    @FXML
    private void onSaveCorrection() {
        if (selectedError == null) {
            showStatus("Please select an error row first.", true);
            return;
        }
        selectedError.rawLine = editField.getText().trim();
        errorsTable.refresh();
        showStatus("Row updated. Re-import to apply the correction.", false);
    }

    // ── Navigation helpers ───────────────────────────────────
    @FXML private void onGoIssuedReport()  { openScreen("/lms/view/IssuedReportView.fxml",  "Books Issued Report"); }
    @FXML private void onGoAvgCost()       { openScreen("/lms/view/AvgCostView.fxml",        "Average Book Cost"); }
    @FXML private void onGoAvailSearch()   { openScreen("/lms/view/AvailSearchView.fxml",    "Book Availability Search"); }

    private void openScreen(String fxmlPath, String title) {
        if (!store.isDataLoaded()) {
            showStatus("Please import files before navigating to reports.", true);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(loader.load(), 800, 520));
            stage.show();
        } catch (Exception e) {
            showStatus("Could not open screen: " + e.getMessage(), true);
        }
    }

    // ── Utility ──────────────────────────────────────────────
    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError
                ? "-fx-text-fill: #cc2200; -fx-font-size: 12px;"
                : "-fx-text-fill: #225522; -fx-font-size: 12px;");
    }
}
