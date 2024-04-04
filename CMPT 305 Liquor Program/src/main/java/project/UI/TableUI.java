package project.UI;

import DataParsing.PropertyAssessments;
import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import DataCollection.NeighborhoodData;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TableUI extends Application {
    // *** Below is where we collect our data into a list to be used and displayed in the table (To Be Done) ***
    // These are just temp hardcoded values to see functionality of UI
    private final ObservableList<NeighborhoodData> data = FXCollections.observableArrayList();
    // Left-hand side table
    private final TableView<NeighborhoodData> TableL = new TableView<>();
    // Right-hand side table
    private final GridPane GridPaneR = new GridPane();
    BarChartCreator barChart = new BarChartCreator(data);
    private Map<String, DataLabel> dataLabels = new HashMap<>();
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.CANADA);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Window Size = 1280 x 800 *** (Do we have a better preferred window size?) ***
        // Set stage.setMaximized to true near bottom of method if we want to open program maximized
        Scene scene = new Scene(new Group(), 1280, 800);
        scene.getStylesheets().add(getClass().getResource("TableViewDesign.css").toExternalForm());

        String amtFile = "CMPT 305 Liquor Program/src/main/resources/project/UI/Property_Assessment_Data_2024.csv";
        String liqFile = "CMPT 305 Liquor Program/src/main/resources/project/UI/Alcohol_Sales_Licences_20240324.csv";
        String criFile = "CMPT 305 Liquor Program/src/main/resources/project/UI/EPS_OCC_30DAY_-5791830293904934989.csv";

        PropertyAssessments edmonton = new PropertyAssessments(amtFile, liqFile, criFile);
        List<PropertyAssessments> allNeighborhoodAssessments = edmonton.getAllNeighbourhoods();
        initializeDataList(allNeighborhoodAssessments);

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
        stage.setMaximized(true); // Set to true = program opens maximized
        stage.show();
    }

    public void initializeDataList(List<PropertyAssessments> allNeighborhoodAssessments) {
        for (PropertyAssessments pa : allNeighborhoodAssessments) {
            NeighborhoodData nd = pa.convertToNeighborhoodData();
            data.add(nd);
        }
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
                createCurrencyColumn("Mean Property Value", "meanValue");
        TableColumn<NeighborhoodData, Number> medianValueColumn =
                createCurrencyColumn("Median Property Value", "medianValue");

        table.getColumns().addAll(neighbourhoodColumn, liquorStoresColumn, crimesColumn, meanValueColumn, medianValueColumn);

        // This divides the columns up into equal sections based on size of inputted list
        table.getColumns().forEach(column -> column.prefWidthProperty().bind(
                table.widthProperty().divide(table.getColumns().size())
        ));
    }

    // Method that creates a column in the table that has a formatted currency value within it
    private TableColumn<NeighborhoodData, Number> createCurrencyColumn(String title, String propertyName) {
        TableColumn<NeighborhoodData, Number> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));

        column.setCellFactory(tc -> new TableCell<NeighborhoodData, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    // remove the zeroes after the decimal
                    setText(currencyFormatter.format(value.doubleValue()).replaceAll("0*$", "").replaceAll("\\.$", ""));
                }
            }
        });

        return column;
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
                updateLabel("Stores", String.valueOf(newSelection.getNumberOfStores()));
                updateLabel("CrimeData", String.valueOf(newSelection.getCrimeData()));
                updateLabel("Median", String.valueOf(currencyFormatter.format(newSelection.getMedianValue()).replaceAll("0*$", "").replaceAll("\\.$", "")));
                updateLabel("Mean", String.valueOf(currencyFormatter.format(newSelection.getMeanValue()).replaceAll("0*$", "").replaceAll("\\.$", "")));
            }
        });
    }

    // Method that creates the right-hand gridpane
    private void createGridPaneR(GridPane gridpane) {
        gridpane.getStyleClass().add("grid-pane");
        ColumnConstraints labelColumn = new ColumnConstraints();
        // Give the label column 50% of the grid width
        labelColumn.setPercentWidth(70);
        ColumnConstraints valueColumn = new ColumnConstraints();
        // Give the value column 50% of the grid width
        valueColumn.setPercentWidth(30);
        gridpane.getColumnConstraints().addAll(labelColumn, valueColumn);

        dataLabels = new HashMap<>();
        dataLabels.put("Stores", new DataLabel("Liquor Stores in Neighbourhood:", "Select a Neighbourhood"));
        dataLabels.put("CrimeData", new DataLabel("Crime occurrences:", "Select a Neighbourhood"));
        dataLabels.put("Median", new DataLabel("Median assessed values:", "Select a Neighbourhood"));
        dataLabels.put("Mean", new DataLabel("Mean Assessed values:", "Select a Neighbourhood"));

        int row = 0;
        for (DataLabel dataLabel : dataLabels.values()) {
            dataLabel.getTitleLabel().getStyleClass().add("label-cell");
            dataLabel.getValueLabel().getStyleClass().add("value-cell");

            GridPane.setHalignment(dataLabel.getValueLabel(), HPos.RIGHT);

            gridpane.add(dataLabel.getTitleLabel(), 0, row); // Add title label to first column
            gridpane.add(dataLabel.getValueLabel(), 1, row); // Add value label to second column
            row++;
        }

        gridpane.setGridLinesVisible(true);
    }

    // Method that creates the right hand side combo box
    private ComboBox<String> createComboBox() {
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
                updateLabel("CrimeData", String.valueOf(crimeCount));
            }
        });
        return combobox;
    }

    // used in the createComboBox and createGridPaneR methods to update the labels within them
    private void updateLabel(String key, String value) {
        if (dataLabels.containsKey(key)) {
            dataLabels.get(key).setValue(value);
        }
    }

    // Method to create the left-hand side of the UI
    private VBox createLeftSideVBox() {
        // Search functionality that is placed above table
        TextField searchNeighbourhood = new TextField();
        searchNeighbourhood.setPromptText("Search by Neighbourhood");
        searchNeighbourhood.setPrefWidth(155);

        // collect all the neighbourhood names to be used below
        ObservableList<String> neighborhoodNames = data.stream()
                .map(NeighborhoodData::getNeighborhood)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // used to list the available neighborhoods as you are searching in the search box (top-left)
        Popup suggestionsPopup = new Popup();
        ListView<String> suggestionsView = new ListView<>();
        suggestionsView.setPrefHeight(75);
        suggestionsPopup.getContent().add(suggestionsView);

        // displays the neighbourhood as you type
        searchNeighbourhood.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<String> filteredItems = neighborhoodNames.filtered(item ->
                    item.toLowerCase().startsWith(newValue.toLowerCase()));
            if (filteredItems.isEmpty() || newValue.isEmpty()) {
                suggestionsPopup.hide();
            } else {
                suggestionsView.setItems(filteredItems);
                suggestionsView.setPrefWidth(searchNeighbourhood.getWidth());
                Bounds boundsInScreen = searchNeighbourhood.localToScreen(searchNeighbourhood.getBoundsInLocal());
                suggestionsPopup.show(searchNeighbourhood, boundsInScreen.getMinX(), boundsInScreen.getMaxY());
            }
        });

        // handle mouse clicks on the suggestions list
        suggestionsView.setOnMouseClicked(event -> {
            if (!suggestionsView.getSelectionModel().isEmpty()) {
                String selectedNeighborhood = suggestionsView.getSelectionModel().getSelectedItem();
                searchNeighbourhood.setText(selectedNeighborhood);

                // filter data to match the selected neighborhood
                ObservableList<NeighborhoodData> filteredData = data.stream()
                        .filter(item -> item.getNeighborhood().equalsIgnoreCase(selectedNeighborhood))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

                TableL.setItems(filteredData);
                suggestionsPopup.hide();
            }
        });

        Button search = ButtonCreators.createSearchButton(searchNeighbourhood, TableL, data);
        Button clear = ButtonCreators.createClearButton(searchNeighbourhood, TableL, data);
        HBox hb = new HBox(5);
        hb.getChildren().addAll(searchNeighbourhood, search, clear);

        Label label = new Label("Neighbourhoods of Edmonton");
        label.getStyleClass().add("left-label-style");
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, hb, TableL);
        // Makes it so table extends to bottom of window
        VBox.setVgrow(TableL, Priority.ALWAYS);
        return vbox;
    }

    // Method to create the right-hand side of the UI
    private VBox createRightSideVBox() {
        createGridPaneR(GridPaneR);
        ComboBox<String> combobox = createComboBox();

        HBox buttonBox = new HBox(10); // HBox to hold buttons
        buttonBox.setAlignment(Pos.CENTER);
        String[] categories = {"Liquor Stores", "Crime Data", "Median Value", "Mean Value"};
        for (String category : categories) {
            Button button = barChart.createButtonForCategory(category);
            HBox.setHgrow(button, Priority.ALWAYS);
            button.setMaxWidth(Double.MAX_VALUE);
            buttonBox.getChildren().add(button);
        }

        Label label = new Label("Neighbourhood Data");
        label.getStyleClass().add("left-label-style");
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, combobox, GridPaneR, buttonBox, barChart.getBarChart());
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