package UI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class TableUI extends Application {
    // *** Below is where we collect our data into a list to be used and displayed in the table (To Be Done) ***
    // private final ObservableList<dataset> data = FXCollections.observableArrayList();

    // Left-hand side table
    private final TableView TableL = new TableView();

    // Right-hand side table
    private final GridPane GridPaneR = new GridPane();
    // Below are the labels that are used in GridPaneR
    private final Label crimeDataValue = new Label("N/A");
    private final Label storesValue = new Label("N/A");
    private final Label medianValue = new Label("N/A");
    private final Label meanValue = new Label("N/A");

    // hb contain neighbourhood search text box and search button
    final HBox hb = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Window Size = 1280 x 800 *** (Do we have a better preferred window size?) ***
        // Set stage.setMaximized to true near bottom of method if we want to open program maximized
        Scene scene = new Scene(new Group(), 1280, 800);
        scene.getStylesheets().add(getClass().getResource("TableViewDesign.css").toExternalForm());

        // Program Title
        stage.setTitle("TableUIPlaceholder");
        ComboBox comboBox = createGridPaneR(GridPaneR);

        createAllColumns(TableL);

        // Add values and columns to scene
        // tableL.setItems(data);           *** No Values to add yet so useless for now ***

        // Search functionality that is placed above table
        final TextField searchNeighbourhood = new TextField();
        searchNeighbourhood.setPromptText("Search by Neighbourhood");
        searchNeighbourhood.setPrefWidth(155);

        final Button search = createSearchButton(searchNeighbourhood, TableL);
        hb.getChildren().addAll(searchNeighbourhood, search);

        /* *** Need to implement the methods that allow for data to be retrieved when neighbourhood row clicked ***
        // Add listener so that when neighbourhood is selected, it updates the labels in right-hand side of UI
        TableL.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Get the data from the selected row
                (insert name of data set here) selectedNeighborhoodData = TableL.getSelectionModel().getSelectedItem();

                // Update labels in right-hand side with the data from the selected neighborhood
                storesValue.setText(String.valueOf(selectedNeighborhoodData.getNumberOfStores()));
                crimeDataValue.setText(String.valueOf(selectedNeighborhoodData.getCrimeData));
                medianValue.setText(String.valueOf(selectedNeighborhoodData.getMedianValue));
                meanValue.setText(String.valueOf(selectedNeighborhoodData.getMeanValue));
            }
        });
         */

        // This creates the left-hand side of the UI (Table and Search Bar)
        VBox vboxL = createLeftSideVBox(hb, TableL);

        // This creates the right-hand side of the UI (Select Crime and Neighbourhood Statistics)
        // *** Should also place our bar-graph in here ***
        // *** Should probably also have a label that displays what neighbourhood is selected ***
        VBox vboxR = createRightSideVBox(comboBox, GridPaneR);

        // This creates the whole UI
        HBox mainUI = createMainUIHBox(vboxL, vboxR, scene);

        ((Group) scene.getRoot()).getChildren().addAll(mainUI);

        scene.setFill(Color.LIGHTGRAY);
        stage.setScene(scene);
        stage.setMaximized(true); // Set to true = program opens maximized
        stage.show();
    }

    // Method that creates all columns
    private void createAllColumns(TableView table) {
        // Create and add columns
        List<TableColumn> columns = new ArrayList<>();
        columns.add(createColumn("Neighbourhood", table));
        columns.add(createColumn("Total Liquor Stores", table));
        columns.add(createColumn("Total Crimes Committed", table));
        columns.add(createColumn("Mean Property Value", table));
        columns.add(createColumn("Median Property Value", table));

        table.getColumns().addAll(columns);
        // This divides the columns up into equal sections based on size of inputted list
        columns.forEach(column -> column.prefWidthProperty().bind(table.widthProperty().divide(columns.size())));
    }

    // Method to create a single table column
    private TableColumn createColumn(String title, TableView table) {
        TableColumn column = new TableColumn(title);
        column.prefWidthProperty().bind(table.widthProperty().divide(table.getColumns().size() + 1));

        // Allows users to click on column to sort values in that column
        column.setSortable(true);

        // Configure cell value factory *** Need a dataset in order to add values ***
        // column.setCellValueFactory(...);
        return column;
    }

    // Method that adds a search button to UI and sets what action happens on press
    private Button createSearchButton(TextField searchField, TableView<?> table) {
        final Button searchButton = new Button("Search");
        // Action is here, calls performSearch method
        //searchButton.setOnAction(event -> performSearch(searchField.getText(), table));
        return searchButton;
    }

    /*
    // *** In order for action to occur, need an active dataset to search ***
    // *** Should add error checking in future as well, to ensure valid neighbourhood is entered ***
    private void performSearch(String searchText, TableView<?> table) {
        searchText = searchText.toLowerCase();

        // Filtering data based on the searchText (need to change item.getNeighbourhood to relevant data)
        // Also change "data" to relevant TableView type
        ObservableList<?> filteredData = data.stream()
                .filter(item -> item.getNeighbourhood().toLowerCase().contains(searchText))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // Update the table items with only entered neighbourhood
        table.setItems(filteredData);
    }
     */

    // Method that creates the right-hand side of the UI
    private ComboBox createGridPaneR(GridPane gridpane) {
        gridpane.getStyleClass().add("grid-pane");
        ColumnConstraints labelColumn = new ColumnConstraints();
        // Give the label column 50% of the grid width
        labelColumn.setPercentWidth(70);

        ColumnConstraints valueColumn = new ColumnConstraints();
        // Give the value column 50% of the grid width
        valueColumn.setPercentWidth(30);

        gridpane.getColumnConstraints().addAll(labelColumn, valueColumn);
        // The categories we'll be using
        Label[] labels = {
                new Label("Liquor Stores in Neighbourhood:"),
                new Label("Crime occurrences (180 days):"),
                new Label("Median assessed values:"),
                new Label("Mean Assessed values:")
        };

        Label[] values = {storesValue, crimeDataValue, medianValue, meanValue};

        for (int i = 0; i < labels.length; i++) {
            labels[i].getStyleClass().add("label-cell");
            gridpane.add(labels[i], 0, i); // Add label (i.e. list above) to first column

            values[i].getStyleClass().add("value-cell");
            gridpane.add(values[i], 1, i); // Add value  to second column
        }

        gridpane.setGridLinesVisible(true);

        // Dataset for the ComboBox
        ObservableList<String> crimeTypes =
                FXCollections.observableArrayList(
                        "Select Specific Crime", // Default value at the top
                        "Theft",
                        "Assault",
                        "Burglary"
                        // add our other crime types here, these are just placeholders
                );

        ComboBox<String> combobox = new ComboBox<>(crimeTypes);
        combobox.setPromptText("Select Specific Crime");

        // Set up the event handler for the ComboBox
        combobox.setOnAction(event -> {
            String selectedCrimeType = combobox.getValue();

            if (!"Select Specific Crime".equals(selectedCrimeType)) {
                // Search for the number of occurrences of the selected crime type
                int crimeCount = searchCrimeData(selectedCrimeType);

                // Update the crime data value label
                crimeDataValue.setText(String.valueOf(crimeCount));
            }
        });
        return combobox;
    }

    // Method to create the left-hand side of the UI
    private VBox createLeftSideVBox(HBox hbox, TableView table) {
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(hbox, table);
        // Makes it so table extends to bottom of window
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }

    // Method to create the right-hand side of the UI
    private VBox createRightSideVBox(ComboBox combobox, GridPane gridpane) {
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(combobox, gridpane);
        return vbox;
    }

    // Method to create the entire UI
    private HBox createMainUIHBox(VBox left, VBox right, Scene scene) {
        double ratioL = 0.6;
        double ratioR = 0.4;
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(0, 10, 10, 0));

        // Set the preferred width ratios of the left vs. rights side of UI
        left.prefWidthProperty().bind(hbox.widthProperty().multiply(ratioL));
        right.prefWidthProperty().bind(hbox.widthProperty().multiply(ratioR));

        hbox.getChildren().addAll(left, right);
        hbox.prefWidthProperty().bind(scene.widthProperty());
        hbox.prefHeightProperty().bind(scene.heightProperty());

        // Makes it so both tables will grow to any window size
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);

        return hbox;
    }

    // Method that will search our database for amount of specific crime occurrences within a neighbourhood
    private int searchCrimeData(String crimeType) {
        return 0;
    }
}
