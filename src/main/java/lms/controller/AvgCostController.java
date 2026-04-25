package lms.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lms.data.DataStore;
import lms.model.Book;

import java.util.List;

public class AvgCostController {
    @FXML private Label totalBooksLabel;
    @FXML private Label totalCostLabel;
    @FXML private Label avgCostLabel;

    private final DataStore store = DataStore.getInstance();

    @FXML
    private void onCalculate() {
        List<Book> books = store.getBooks();

        if (books.isEmpty()) {
            avgCostLabel.setText("No books loaded.");
            return;
        }

        // Sum all book prices
        double totalCost = books.stream()
                .mapToDouble(Book::getPrice)
                .sum();

        double average = totalCost / books.size();

        totalBooksLabel.setText("Total books in library : " + books.size());
        totalCostLabel .setText(String.format("Total cost of all books: £%.2f", totalCost));
        avgCostLabel   .setText(String.format("Average cost per book  : £%.2f", average));
    }
}
