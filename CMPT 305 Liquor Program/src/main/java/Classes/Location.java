package Classes;

import java.util.Objects;

public class Location {

private double latitude, longitude;

public Location(double latitude, double longitude){

	this.latitude = latitude;
	this.longitude = longitude;
}
public String toString() {
	return "(" + latitude + " , " + longitude + ")";
	}

public boolean equals(Object obj) {
	if(!(obj instanceof Location)) {
		return false;
	}
		Location otherLocation = (Location) obj;
		return this.latitude == otherLocation.latitude && this.longitude == otherLocation.longitude;
	
}

public int hashCode(){
	return Objects.hash(latitude,longitude);
}

public double getLatitude() {
	
	
	return latitude;
}

public double getLongitude() {
	
	
	return longitude;
}
}
