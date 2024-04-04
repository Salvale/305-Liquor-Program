public class Address {

    private String suite;
    private int houseNum;
    private String streetName;
    private String neighbourhoodID;
    private String neighbourhood;
    private String ward;

    public Address(String suite, int houseNum, String streetName, String neighbourhoodID, String neighbourhood, String ward) {
        this.suite = suite;
        this.houseNum = houseNum;
        this.streetName = streetName;
        this.neighbourhoodID = neighbourhoodID;
        this.neighbourhood = neighbourhood;
        this.ward = ward;
    }

    public String toString() {
        return(Integer.toString(this.houseNum) + " " + this.streetName + " " + this.suite + ", " + this.neighbourhood + "(" + this.neighbourhoodID + "). " + this.ward + " ward.");
    }

}
