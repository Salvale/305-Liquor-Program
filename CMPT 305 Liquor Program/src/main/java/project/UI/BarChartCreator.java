package project.UI;

import DataCollection.NeighborhoodData;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import javafx.scene.text.Font;


import java.util.*;
import java.util.stream.Collectors;

public class BarChartCreator {
    private BarChart<String, Number> barChart;
    private final ObservableList<NeighborhoodData> data;

    // Holds the current value of our neighbourhood
    private NeighborhoodData selectedNeighborhood;

    public BarChartCreator(ObservableList<NeighborhoodData> data) {
        this.data = data;
        createBarChart();
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);
        barChart.setTitle("Sorted Against Highest in Category");
        barChart.setCategoryGap(15);
    }

    private void createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelFont(new Font(16));
        yAxis.setTickLabelFont(new Font(16));
        barChart = new BarChart<>(xAxis, yAxis);
        xAxis.setLabel("Neighbourhood");
        yAxis.setLabel("Value");
    }

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }

    // Method that creates a button for the sorting function
    public Button createButtonForCategory(String category) {
        Button button = new Button("Sort " + category);
        button.setOnAction(e -> {
            if (selectedNeighborhood != null)
                updateBarChart(selectedNeighborhood, category);
        });
        return button;
    }

    private void updateBarChart(NeighborhoodData selectedNeighborhood, String category) {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // retrieve the value for the given category from the selected neighborhood
        XYChart.Data<String, Number> selectedData = new XYChart.Data<>(selectedNeighborhood.getNeighborhood(), selectedNeighborhood.getValueByCategory(category));
        series.getData().add(selectedData);

        List<NeighborhoodData> sortedList = getSortedList(category);

        // remove the selected neighborhood from the sorted list to avoid duplicate
        sortedList.remove(selectedNeighborhood);

        // sort by largest values in the category and add to the chart
        // this takes the top 4 largest values as the comparison.
        for (int i = 0; i < Math.min(4, sortedList.size()); i++) {
            NeighborhoodData item = sortedList.get(i);
            XYChart.Data<String, Number> data = new XYChart.Data<>(item.getNeighborhood(), item.getValueByCategory(category));
            series.getData().add(data);
        }

        barChart.getData().add(series);

        // wait until the chart has been laid out, then apply colours
        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node node = data.getNode();
                if (data == selectedData) {
                    // highlight the selected neighborhood bar
                    node.setStyle("-fx-bar-fill: green; -fx-background-radius: 10;");
                } else {
                    // style for other bars
                    node.setStyle("-fx-bar-fill: lightgrey; -fx-background-radius: 10;");
                }
            }
        });
        animateBars(series);
    }

    // Method that sorts the list of data
    private List<NeighborhoodData> getSortedList(String category) {
        return data.stream()
                .sorted(Comparator.comparing(item ->
                                item.getValueByCategory(category).doubleValue(),
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    // method that allows smooth animation of bars from bottom of chart
    private void animateBars(XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            Node node = data.getNode();
            double chartHeight = barChart.getHeight();

            node.applyCss();
            node.layoutYProperty();

            double nodeHeight = node.getBoundsInParent().getHeight();
            double yTranslateStart = chartHeight - node.getLayoutY();

            // how long the bars should take to animate
            TranslateTransition tt = new TranslateTransition(Duration.millis(500), node);
            tt.setFromY(yTranslateStart + nodeHeight);
            tt.setToY(0);
            tt.play();
        }
    }

    // Method that tells BarChart what our current neighbourhood is
    public void setCurrentSelectedNeighborhood(NeighborhoodData selectedNeighborhood) {
        this.selectedNeighborhood = selectedNeighborhood;
    }

}
