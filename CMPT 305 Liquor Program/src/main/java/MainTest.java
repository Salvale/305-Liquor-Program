import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class MainTest {
    public static void main(String args[]) {
        String dirName = "";//"DataFiles/";
        String amtFile = "src/main/resources/Property_Assessment_Data_2024.csv";
        String liqFile = "src/main/resources/Alcohol_Sales_Licences_20240324.csv";
        String criFile = "src/main/resources/EPS_OCC_30DAY_-5791830293904934989.csv";
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(amtFile));
        } catch (IOException e) {
            System.out.println(e);
        }
        PropertyAssessments edmonton = new PropertyAssessments(amtFile, liqFile, criFile);
        System.out.println(edmonton);
        List<PropertyAssessments> all = edmonton.getAllNeighbourhoods().stream().sorted(Comparator.comparing(PropertyAssessments::crimeCount)).toList();
        System.out.println("Info on all neighbourhoods:");
        for (int i = 0; i < all.size(); i++) {
            System.out.println(all.get(i));
        }
    }
}
