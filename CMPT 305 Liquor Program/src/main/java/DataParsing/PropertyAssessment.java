package DataParsing;

import java.util.Objects;

public class PropertyAssessment {
    public static int TestTwo() {
        return 1;
    }


    private Address address;
    private int accountNum;
    private String garage;
    private String neighbourhood; //address class
    private int value;
    private PointLocation location; //class
    private AssessmentClass propertyClass;


    public PropertyAssessment(int accountNum, String suite, int houseNum, String streetName, String garage, String neighbourhoodID, String neighbourhood, String ward, int value, float[] location, int[] propertyClassPercent, String[] propertyClasses) {
        this.accountNum = accountNum;
        this.address = new Address(suite, houseNum, streetName, neighbourhoodID, neighbourhood, ward);
        this.garage = garage;
        this.neighbourhood = neighbourhood;
        this.value = value;
        this.location = new PointLocation(location);
        this.propertyClass = new AssessmentClass(propertyClassPercent[0], propertyClassPercent[1], propertyClassPercent[2], propertyClasses[0], propertyClasses[1], propertyClasses[2]);
    }

    public String toString() {
        String s = ("AccountNumber: " + this.accountNum + "\nDataParsing.Address: " + this.address + "\nAssessed Value: $" + this.value + "\nAssessment Class " + this.propertyClass + "\nNeighbourhood: " + this.neighbourhood + " +\nLocation: " + this.location);
        return s;
    }

    public int getNum() {
        return this.accountNum;
    }

    public int getValue() {
        return this.value;
    }

    public String getNeighbourhood() {
        return this.neighbourhood;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, accountNum, garage, neighbourhood, value, location, propertyClass);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PropertyAssessment)) {
            return false;
        }
        PropertyAssessment other = (PropertyAssessment) o;
        return (this.hashCode() == other.hashCode());
    }

    public boolean equalsAN(int anInt) {
        return anInt == this.accountNum;
    }

    public boolean checkAllClasses(String cls) {
        for (int i = 0; i < 3; i++) {
            if (this.propertyClass.classAtI(i) != null && this.propertyClass.classAtI(i).equals(cls)) {
                return true;
            }
        }
        return false;
    }

    public float getX() {
        return this.location.getX();
    }

    public float getY() {
        return this.location.getY();
    }

    // @Override
    public int compareTo(PropertyAssessment o) {
        return this.value - o.value; // if 1 > 2, pos int. else neg int
    }
}
