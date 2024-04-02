package Classes;

import java.text.NumberFormat;
import java.util.Objects;

public class LiqourStore {
    private String storeName;
    private Neighbourhood neighbourhood;
    private Location location;

    public LiqourStore(String storeName, Neighbourhood neighbourhood, Location location) {
        this.storeName = storeName;
        this.neighbourhood = neighbourhood;
        this.location = location;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LiqourStore otherStore)) {
            return false;
        } else {
            return this.storeName == otherStore.storeName;
        }
    }

    public int hashCode() {
        return Objects.hash(this.storeName,this.neighbourhood, this.location);
    }

    public String toString() {

       return "Store Name = " + storeName
               + "\nNeighbourhood = " + String.valueOf(this.neighbourhood)
               + "\nLocation = " + String.valueOf(this.location);
   }

    public String getStoreName() {
        return this.storeName;
    }


    public Neighbourhood getNeighbourhood() {
        return this.neighbourhood;
    }

    public Location getLocation() {
        return this.location;
    }
}