package lms.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import lms.data.DataStore;
import lms.model.Book;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AvailSearchController {
    @FXML private TextField  searchField;
    @FXML private Label      statusLabel;

    @FXML private TableView<Book>              resultsTable;
    @FXML private TableColumn<Book, String>    colBookId;
    @FXML private TableColumn<Book, String>    colTitle;
    @FXML private TableColumn<Book, String>    colAuthor;
    @FXML private TableColumn<Book, Integer>   colCopies;
    @FXML private TableColumn<Book, Integer>   colAvail;
    @FXML private TableColumn<Book, String>    colIsbn;

    private final DataStore store = DataStore.getInstance();
    private List<Book> currentResults = List.of();

    @FXML
    public void initialize() {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle .setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCopies.setCellValueFactory(new PropertyValueFactory<>("copies"));
        colAvail .setCellValueFactory(new PropertyValueFactory<>("availability"));
        colIsbn  .setCellValueFactory(new PropertyValueFactory<>("isbn13"));
    }

    @FXML
    private void onSearch() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            showStatus("Please enter a search term.", true);
            return;
        }

        // Wildcard: if query ends with '*', match titles that start with the prefix
        boolean wildcard = query.endsWith("*");
        String  prefix   = wildcard
                ? query.substring(0, query.length() - 1).toLowerCase()
                : query.toLowerCase();

        currentResults = store.getBooks().stream()
                .filter(b -> {
                    String title = b.getTitle().toLowerCase();
                    return wildcard ? title.startsWith(prefix) : title.equals(prefix);
                })
                .toList();

        resultsTable.setItems(FXCollections.observableArrayList(currentResults));

        if (currentResults.isEmpty()) {
            showStatus("No books found matching '" + query + "'.", true);
        } else {
            showStatus(currentResults.size() + " book(s) found.", false);
        }
    }

    @FXML
    private void onExport() {
        if (currentResults.isEmpty()) {
            showStatus("No results to export. Run a search first.", true);
            return;
        }

        // Ask user where to save the file
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export Search Results");
        chooser.setInitialFileName("availability_results.txt");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = chooser.showSaveDialog(searchField.getScene().getWindow());
        if (file == null) return;

        // Write results to the text file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(String.format("%-10s %-22s %-20s %-7s %-9s %-15s%n",
                    "Book ID", "Title", "Author", "Copies", "Available", "ISBN-13"));
            bw.write("-".repeat(85));
            bw.newLine();

            for (Book b : currentResults) {
                bw.write(String.format("%-10s %-22s %-20s %-7d %-9d %-15s%n",
                        b.getBookId(), b.getTitle(), b.getAuthor(),
                        b.getCopies(), b.getAvailability(), b.getIsbn13()));
            }

            bw.write("-".repeat(85));
            bw.newLine();
            bw.write("Total results: " + currentResults.size());
            bw.newLine();

            showStatus("Exported to: " + file.getAbsolutePath(), false);
        } catch (IOException e) {
            showStatus("Export failed: " + e.getMessage(), true);
        }
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError
                ? "-fx-text-fill: #cc2200; -fx-font-size: 12px;"
                : "-fx-text-fill: #225522; -fx-font-size: 12px;");
    }
}
