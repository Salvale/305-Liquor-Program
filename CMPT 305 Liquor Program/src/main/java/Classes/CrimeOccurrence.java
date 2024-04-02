package Classes;

import java.util.Objects;

public class CrimeOccurrence {
    private String occurrenceGroup, occurrenceType;
    private Neighbourhood neighbourhood;
    private Location location;

    public CrimeOccurrence(String occurrenceGroup, String occurrenceType,Neighbourhood neighbourhood, Location location) {
        this.occurrenceGroup = occurrenceGroup; // general category of crime
        this.occurrenceType = occurrenceType; // actual crime description
        this.neighbourhood = neighbourhood;
        this.location = location;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CrimeOccurrence otherOccurrence)) {
            return false;
        } else {
            return this.occurrenceGroup  == otherOccurrence.occurrenceGroup
                    && this.occurrenceType == otherOccurrence.occurrenceType
                    && this.neighbourhood == otherOccurrence.neighbourhood
                    && this.location == otherOccurrence.location;
        }
    }

    public int hashCode() {
        return Objects.hash(this.occurrenceGroup, this.occurrenceType, this.neighbourhood, this.location);
    }

    public String toString() {

        return "Occurrence Group = " + this.occurrenceGroup
                + "Occurrence Type = " + this.occurrenceType
                + "\nNeighbourhood = " +this.neighbourhood
                + "\nLocation = " + this.location;
    }

    public String getOccurrenceGroup() {
        return this.occurrenceGroup;
    }

    public String getOccurrenceType() {
        return this.occurrenceType;
    }

    public Neighbourhood getNeighbourhood() {
        return this.neighbourhood;
    }

    public Location getLocation() {
        return this.location;
    }
}

