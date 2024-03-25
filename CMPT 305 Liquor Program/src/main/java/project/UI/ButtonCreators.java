package project.UI;

import DataCollection.NeighborhoodData;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.stream.Collectors;

public class ButtonCreators {

        public static Button createSearchButton(TextField searchField, TableView<NeighborhoodData> table, ObservableList<NeighborhoodData> data) {
            Button searchButton = new Button("Search");
            searchButton.setOnAction(event -> performSearch(searchField.getText(), table, data));
            searchField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    performSearch(searchField.getText(), table, data);
                }
            });
            return searchButton;
        }

        public static Button createClearButton(TextField searchField, TableView<NeighborhoodData> table, ObservableList<NeighborhoodData> data) {
            Button clearButton = new Button("Clear");
            clearButton.setOnAction(e -> {
                searchField.clear();
                table.setItems(data);
            });
            return clearButton;
        }

        private static void performSearch(String searchText, TableView<NeighborhoodData> table, ObservableList<NeighborhoodData> data) {
            searchText = searchText.toLowerCase();

            String finalSearchText = searchText;
            ObservableList<NeighborhoodData> filteredData = data.stream()
                    .filter(item -> item.getNeighborhood().toLowerCase().contains(finalSearchText))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            table.setItems(filteredData);
        }
}

