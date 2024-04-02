package Classes;

/**
 * 
 */

public class Neighbourhood {

	private int neighbourhoodId;
	private String neighbourhood;
	
	public Neighbourhood (int neighbourhoodId, String neighbourhood ) {

		this.neighbourhoodId = neighbourhoodId;
		this.neighbourhood = neighbourhood;

		

	}
	
	public String toString() {
		return  neighbourhood ;
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof Neighbourhood)) {
			return false;
		}
			Neighbourhood otherNeighbourhood = (Neighbourhood) obj;
			return this.neighbourhoodId == otherNeighbourhood.neighbourhoodId;
		
	}

	public int hashCode(){
		return Integer.hashCode(neighbourhoodId);
	}
	
	public int getNeighbourhoodId() {
    	
    	
    	return neighbourhoodId;
    }
	
	public String getNeighbourhood() {
    	
    	
    	return neighbourhood;
    }
	

}
