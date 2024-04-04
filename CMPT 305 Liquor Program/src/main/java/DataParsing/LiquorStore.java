package DataParsing;

public class LiquorStore {
    private String neighbourhood;
    private float x;
    private float y;

    public LiquorStore(String x, String y, String neighbourhood) {
        this.x = Float.parseFloat(x);
        this.y = Float.parseFloat(y);
        this.neighbourhood = neighbourhood;
    }

    public String getNeighbourhood() {
        return this.neighbourhood;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
