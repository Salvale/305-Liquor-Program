import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

//      ALL HOPE ABANDON
// (of understanding this code)
//      YE WHO ENTER HERE

public class PropertyAssessments {
    public static int testOne(){
        return PropertyAssessment.TestTwo();
    }
    private List<LiquorStore> stores;
    private List<Crime> crimes;
    private List<PropertyAssessment> data; //as LIST
    private int n = 0;
    private int max;
    private int min;
    private int range;
    private double mean;
    private int median;


    public String whichNeighbourhood(){
        if (this.data.stream().map(a->a.getNeighbourhood()).distinct().collect(Collectors.toList()).size() == 1) {
            return this.data.get(0).getNeighbourhood();
        } else {
            return "All of Edmonton";
        }
    }
    public PropertyAssessments getByAssessmentClass(String classSearch) { //don't use this function it wont work
        System.out.println(this.data.get(1));
        PropertyAssessments assmt = new PropertyAssessments();
        ArrayList<PropertyAssessment> newData = new ArrayList<>();
        for (int i = 0; i < this.n; i++) {
            if (this.data.get(i).checkAllClasses(classSearch)) {
                newData.add(this.data.get(i));
                i++;
            }
        }
        assmt.data = newData;
        if(newData.size() == 0) {
            return null;
        }
        assmt.analyzeData(newData, null, null);
        return assmt;
    }
    public List<PropertyAssessments> getAllNeighbourhoods() {
        List<PropertyAssessments> allHoods = new ArrayList<>();
        List<String> hoodsAsString = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (!(hoodsAsString.contains(data.get(i).getNeighbourhood()))) {
                PropertyAssessments hood = getNeighbourhood(data.get(i).getNeighbourhood());
                allHoods.add(hood);
                hoodsAsString.add(data.get(i).getNeighbourhood());
            }
        }
        return allHoods;
    }
    public PropertyAssessments getNeighbourhood(String neighbourhood) {
        PropertyAssessments assmt = new PropertyAssessments();
        ArrayList<PropertyAssessment> newData = new ArrayList<>();
        ArrayList<Crime> newCrimes = new ArrayList<>();
        ArrayList<LiquorStore> newAlc = new ArrayList<>();
        for (int i = 0; i < this.n; i++) {
            if (this.data.get(i).getNeighbourhood().equals(neighbourhood)) {
                newData.add(this.data.get(i));
            //    i++;
            }
        }



        for (int i = 0; i < this.crimes.size(); i++) {
            if (this.crimes.get(i).nearestNeighbourhood(this.data).equals(neighbourhood)) {
                newCrimes.add(this.crimes.get(i));
                ///i++;
            }
        }

        for (int i = 0; i < this.stores.size(); i++) {
            if (this.stores.get(i).getNeighbourhood().equals(neighbourhood)) {
                newAlc.add(this.stores.get(i));
              //  i++;
            }
        }

//        this.analyzeData(dataArray, storeArray, crimeArray);
        assmt.data = newData;
 //       if (newData.size() == 0) {
   //         System.out.println("Not Found!!!"); //move to main
     //       return null;
        //}

        assmt.analyzeData(newData, newAlc,newCrimes);
        return assmt;
    }


    public PropertyAssessments() {
    }

    private void analyzeData(ArrayList<PropertyAssessment> data, List<LiquorStore> stores, List<Crime> crimes) {
        this.stores = stores;
        this.crimes = crimes;
        this.data = data;
        this.n = data.size();
        ArrayList<Integer> vals = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            vals.add(data.get(i).getValue());
        }
        vals.sort(Comparator.naturalOrder());

        if (vals.size() % 2 == 0) {
            this.median = (int) ((double) vals.get(vals.size() / 2) + (double) vals.get(vals.size() / 2 - 1)) / 2;
        } else {
            this.median = vals.get(vals.size() / 2);
        }
        this.max = Collections.max(vals);
        this.min = Collections.min(vals);
        this.range = this.max - this.min;
        this.mean = vals.stream().mapToInt(val -> val).average().orElse(0.0);
    }

    private PropertyAssessment createEntry(String[] line) {
        int acN = Integer.parseInt(line[0]);
        String sui = line[1];
        int hoN = 0;
        if (!line[2].isEmpty()) {
            hoN = Integer.parseInt(line[2]);
        }
        String sNa = line[3];
        String gar = line[4];
        String nID = line[5];
        String nei = line[6];
        String war = line[7];
        int val = Integer.parseInt(line[8]);
        //int currentVal = Integer.parseInt(line[8]);
        float[] loc = {Float.parseFloat(line[9]), Float.parseFloat(line[10])};
        int[] clP = new int[3];
        for (int j = 0; j < 3; j++) {
            if (!(line[j + 12].isEmpty())) {
                clP[j] = Integer.parseInt(line[j + 12]);
            }
        }
        String[] cla = new String[3];
        for (int j = 0; j < 3; j++) {
            if (!(line[j + 12].isEmpty())) {
                cla[j] = line[j + 15];
            }
        }
        PropertyAssessment anEntry =    new PropertyAssessment(acN, sui, hoN, sNa, gar, nID, nei, war, val, loc, clP, cla);
        return anEntry;
    }

    private static String[][] readData(String csvFileName) throws IOException {
        // Create a stream to read the CSV file
        BufferedReader reader = Files.newBufferedReader(Paths.get(csvFileName));
        // Skip the header - this assumes the first line is a header
        reader.readLine();

        // Create 2D array to store all rows of data as String
        int initialSize = 100;
        String[][] data = new String[initialSize][];

        // Read the file line by line and store all rows into a 2D array
        int index = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            // Split a line by comma works for simple CSV files
            String[] values = line.split(",");

            // Check if the array is full
            if (index == data.length)
                // Array is full, create and copy all values to a larger array
                data = Arrays.copyOf(data, data.length * 2);
            data[index++] = values;
        }
        // Remove empty rows in the array and return it
        return Arrays.copyOf(data, index);
    }

    public PropertyAssessments(String assessmentData, String liquorData, String crimeData) {
        try {
            String[][] aData = readData(assessmentData);
            String[][] bData = readData(crimeData);
            String[][] cData = readData(liquorData);
            ArrayList<PropertyAssessment> dataArray = new ArrayList<>();
            List<Crime> crimeArray = new ArrayList<>();
            List<LiquorStore> storeArray = new ArrayList<>();
            for (int i = 0; i < aData.length; i++) {
                PropertyAssessment anEntry = createEntry(aData[i]);
                dataArray.add(anEntry);
            }

            for (int i = 0; i < bData.length; i++) {
                Crime aCrime = new Crime(bData[i][10], bData[i][11], bData[i][2], bData[i][3]);
                crimeArray.add(aCrime);
            }

            for (int i = 0; i < cData.length; i++) {
                String[] name = cData[i];
                LiquorStore aStore = new LiquorStore(cData[i][7], cData[i][8], cData[i][5]);
                storeArray.add(aStore);
            }

            this.analyzeData(dataArray, storeArray, crimeArray);
        } catch (IOException e) {
            System.out.println("Failed to read " + assessmentData); //change to just a throw
        }
    }

    public String searchFor(int id) { // can return just an assessment
        for (int i = 0; i < this.n; i++) {
            if (this.data.get(i).equalsAN(id)) {
                return data.get(i).toString();
            }
        }
        return "ID not found!\n";
    }

    public String toString() {
        return ("Descriptive statistics of all property assessments\n"+"Neighbourhood: "+this.whichNeighbourhood()+"\nn = " + this.n + "\nmin = $"+this.min+"\nmax= $"+this.max+"\nrange: $"+this.range+"\nmean: "+this.mean+"\nmedian:"+this.median+"\nCrime count: "+this.crimeCount()+"\nLiquor store count: "+this.storeCount());

    }

    public int storeCount(){
        return this.stores.size();
    }

    public int crimeCount(){
        return this.crimes.size();
    }

    public int crimeCountByGroup(String group){
        return this.crimes.stream().filter(crime -> crime.getGroup().equals(group)).collect(Collectors.toList()).size();
    }

    public int crimeCountByTypeGroup(String typeGroup){
        return this.crimes.stream().filter(crime -> crime.getGroup().equals(typeGroup)).collect(Collectors.toList()).size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyAssessments that = (PropertyAssessments) o;
        return n == that.n && max == that.max && min == that.min && range == that.range && Double.compare(mean, that.mean) == 0 && median == that.median && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, n, max, min, range, mean, median);
    }
}
