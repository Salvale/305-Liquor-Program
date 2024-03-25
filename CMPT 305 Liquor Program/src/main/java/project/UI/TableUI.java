package project.UI;

import DataCollection.NeighborhoodData;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TableUI extends Application {
    // *** Below is where we collect our data into a list to be used and displayed in the table (To Be Done) ***
    // These are just temp hardcoded values to see functionality of UI
    private final ObservableList<NeighborhoodData> data = FXCollections.observableArrayList(
        new NeighborhoodData("Greenwood", 15, 120, 250000.0, 275000.0),
        new NeighborhoodData("Sunnydale", 10, 80, 300000.0, 320000.0),
        new NeighborhoodData("Oakridge", 20, 200, 220000.0, 230000.0),
        new NeighborhoodData("Maple Town", 8, 50, 280000.0, 290000.0),
        new NeighborhoodData("Brookfield", 12, 110, 260000.0, 270000.0)
    );

    // Left-hand side table
    private final TableView<NeighborhoodData> TableL = new TableView<>();

    // Right-hand side table
    private final GridPane GridPaneR = new GridPane();

    BarChartCreator barChart = new BarChartCreator(data);

    // Below are the labels that are used in GridPaneR
    private final Label crimeDataValue = new Label("N/A");
    private final Label storesValue = new Label("N/A");
    private final Label medianValue = new Label("N/A");
    private final Label meanValue = new Label("N/A");

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
        stage.setTitle("Crime vs. Liquor Stores of Edmonton");

        rowSelectionListener(TableL, barChart);

        createAllColumns(TableL);

        // Add values and columns to scene
        TableL.setItems(data);

        // Add listener so that when neighbourhood is selected, it updates the labels in right-hand side of UI
        rowSelectionListener(TableL, barChart);

        // This creates the left-hand side of the UI (Table and Search Bar)
        VBox vboxL = createLeftSideVBox();

        // This creates the right-hand side of the UI (Select Crime and Neighbourhood Statistics)
        // *** Should also place our bar-graph in here ***
        VBox vboxR = createRightSideVBox();

        // This creates the whole UI
        HBox mainUI = createMainUIHBox(vboxL, vboxR, scene);

        ((Group) scene.getRoot()).getChildren().addAll(mainUI);

        scene.setFill(Color.LIGHTGRAY);
        stage.setScene(scene);
        stage.setMaximized(false); // Set to true = program opens maximized
        stage.show();
    }

    // Method that creates all columns
    private void createAllColumns(TableView<NeighborhoodData> table) {
        // Create and add columns
        TableColumn<NeighborhoodData, String> neighbourhoodColumn =
                createColumn("Neighbourhood", "neighborhood");
        TableColumn<NeighborhoodData, Number> liquorStoresColumn =
                createColumn("Total Liquor Stores", "numberOfStores");
        TableColumn<NeighborhoodData, Number> crimesColumn =
                createColumn("Total Crimes Committed", "crimeData");
        TableColumn<NeighborhoodData, Number> meanValueColumn =
                createColumn("Mean Property Value", "meanValue");
        TableColumn<NeighborhoodData, Number> medianValueColumn =
                createColumn("Median Property Value", "medianValue");

        table.getColumns().addAll(neighbourhoodColumn, liquorStoresColumn, crimesColumn, meanValueColumn, medianValueColumn);

        // This divides the columns up into equal sections based on size of inputted list
        table.getColumns().forEach(column -> column.prefWidthProperty().bind(
                table.widthProperty().divide(table.getColumns().size())
        ));
    }

    // Method to create a single table column (helper method to createAllColumns)
    private <T> TableColumn<NeighborhoodData, T> createColumn(String title, String propertyName) {
        TableColumn<NeighborhoodData, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    // Method that listens for mouse clicks within table and updates the right hand side if clicked
    private void rowSelectionListener(TableView<NeighborhoodData> table, BarChartCreator barChart) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // set what our current neighbourhood is for the bar graph
                barChart.setCurrentSelectedNeighborhood(newSelection);

                // Update labels in right-hand side with the data from the selected neighborhood
                storesValue.setText(String.valueOf(newSelection.getNumberOfStores()));
                crimeDataValue.setText(String.valueOf(newSelection.getCrimeData()));
                medianValue.setText(String.valueOf(newSelection.getMedianValue()));
                meanValue.setText(String.valueOf(newSelection.getMeanValue()));
            }
        });
    }

    // Method that creates the right-hand side of the UI
    private ComboBox<String> createGridPaneR(GridPane gridpane) {
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
                new Label("Crime occurrences (within 180 days):"),
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
    private VBox createLeftSideVBox() {
        // Search functionality that is placed above table
        TextField searchNeighbourhood = new TextField();
        searchNeighbourhood.setPromptText("Search by Neighbourhood");
        searchNeighbourhood.setPrefWidth(155);

        Button search = ButtonCreators.createSearchButton(searchNeighbourhood, TableL, data);
        Button clear = ButtonCreators.createClearButton(searchNeighbourhood, TableL, data);
        HBox hb = new HBox(5);
        hb.getChildren().addAll(searchNeighbourhood, search, clear);

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(hb, TableL);
        // Makes it so table extends to bottom of window
        VBox.setVgrow(TableL, Priority.ALWAYS);
        return vbox;
    }

    // Method to create the right-hand side of the UI
    private VBox createRightSideVBox() {
        ComboBox<String> combobox = createGridPaneR(GridPaneR);

        HBox buttonBox = new HBox(10); // HBox to hold buttons
        buttonBox.setAlignment(Pos.CENTER);
        String[] categories = {"Liquor Stores", "Crime Data", "Median Value", "Mean Value"};
        for (String category : categories) {
            Button button = barChart.createButtonForCategory(category);
            buttonBox.getChildren().add(button);
        }

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(combobox, GridPaneR, buttonBox, barChart.getBarChart());
        VBox.setVgrow(barChart.getBarChart(), Priority.ALWAYS);
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