package DataCollection;

// Class that holds all the required data that is displayed in our table
public class NeighborhoodData {
        private String neighborhood;
        private int numberOfStores;
        private int crimeData;
        private double medianValue;
        private double meanValue;

    public NeighborhoodData(String neighborhood, int numberOfStores, int crimeData, double medianValue, double meanValue) {
        this.neighborhood = neighborhood;
        this.numberOfStores = numberOfStores;
        this.crimeData = crimeData;
        this.medianValue = medianValue;
        this.meanValue = meanValue;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public int getNumberOfStores() {
        return numberOfStores;
    }

    public int getCrimeData() {
        return crimeData;
    }

    public double getMedianValue() {
        return medianValue;
    }

    public double getMeanValue() {
        return meanValue;
    }

    public Number getValueByCategory(String category) {
        // Use a switch expression to determine the value to return based on the category
        return switch (category.toLowerCase()) {
            // Return based on what is found
            case "liquor stores" -> this.numberOfStores;
            case "crime data" -> this.crimeData;
            case "median value" -> this.medianValue;
            case "mean value" -> this.meanValue;
            default -> throw new IllegalArgumentException("Unknown category: " + category);
        };
    }

    // toString method for possible debugging
    @Override
    public String toString() {
        return "NeighborhoodData{" +
                "neighborhood='" + neighborhood + '\'' +
                ", numberOfStores=" + numberOfStores +
                ", crimeData=" + crimeData +
                ", medianValue=" + medianValue +
                ", meanValue=" + meanValue +
                '}';
    }

}
