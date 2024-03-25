package project.UI;

import DataCollection.NeighborhoodData;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.collections.ObservableList;
import java.util.Comparator;
import java.util.List;
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
    }

    private void createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        xAxis.setLabel("Neighborhood");
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
        series.setName(category);

        // Retrieve the value for the given category from the selected neighborhood
        Number value = selectedNeighborhood.getValueByCategory(category);
        series.getData().add(new XYChart.Data<>(selectedNeighborhood.getNeighborhood(), value));

        List<NeighborhoodData> sortedList = getSortedList(category);

        // Sort by largest values in the category and add to the chart
        // This takes the top 4 largest values as the comparison. Can be changed to another comparator if desired
        for (int i = 0; i < Math.min(4, sortedList.size()); i++) {
            NeighborhoodData item = sortedList.get(i);
            Number values = item.getValueByCategory(category);
            series.getData().add(new XYChart.Data<>(item.getNeighborhood(), values));
        }

        barChart.getData().add(series);
    }

    // Method that sorts the list of data
    private List<NeighborhoodData> getSortedList(String category) {
        return data.stream()
                .sorted(Comparator.comparing(item ->
                                item.getValueByCategory(category).doubleValue(),
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    // Method that tells BarChart what our current neighbourhood is
    public void setCurrentSelectedNeighborhood(NeighborhoodData selectedNeighborhood) {
        this.selectedNeighborhood = selectedNeighborhood;
    }

}
