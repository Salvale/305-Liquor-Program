package DataParsing;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;


public class MainTest {
    public static void main(String args[]) {
        String dirName = "";//"DataFiles/";
        String amtFile = "CMPT 305 Liquor Program/src/main/resources/project/UI/Property_Assessment_Data_2024.csv";
        String liqFile = "CMPT 305 Liquor Program/src/main/resources/project/UI/Alcohol_Sales_Licences_20240324.csv";
        String criFile = "CMPT 305 Liquor Program/src/main/resources/project/UI/EPS_OCC_30DAY_-5791830293904934989.csv";

        PropertyAssessments edmonton = new PropertyAssessments(amtFile, liqFile, criFile);
        System.out.println(edmonton);
        List<PropertyAssessments> all = edmonton.getAllNeighbourhoods().stream().sorted(Comparator.comparing(PropertyAssessments::crimeCount)).toList();
        System.out.println("Info on all neighbourhoods:");
        for (int i = 0; i < all.size(); i++) {
            System.out.println(all.get(i));
        }

    }
}
