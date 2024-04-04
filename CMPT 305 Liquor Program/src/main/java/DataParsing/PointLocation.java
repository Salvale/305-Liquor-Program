package DataParsing;

import java.util.Objects;

public class PointLocation {
    private float x;
    private float y;

    public PointLocation(float[] locList) {
        this.x = locList[0];
        this.y = locList[1];
    }

    @Override
    public String toString() {
        return (this.x + ", " + this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointLocation that = (PointLocation) o;
        return Float.compare(x, that.x) == 0 && Float.compare(y, that.y) == 0;
    }

    public float getX() {
        return this.y;
    }

    public float getY() {
        return this.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
