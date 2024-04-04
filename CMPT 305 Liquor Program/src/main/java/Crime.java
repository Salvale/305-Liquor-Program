import java.util.List;

public class Crime {
    private float x;
    private float y;
    private String group;
    private String typeGroup;
    public Crime(String x, String y, String group, String typeGroup){
        this.x = Float.parseFloat(x);
        this.y = Float.parseFloat(y);
        this.group = group;
        this.typeGroup = typeGroup;
    }

    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }
    public String getGroup(){
        return this.group;
    }
    public String getTypeGroup() {
        return this.typeGroup;
    }
    private float distBetween(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
    public String nearestNeighbourhood(List<PropertyAssessment> assmts){



        double minDistance = Double.MAX_VALUE;
        PropertyAssessment closestPoint = null;

        // Binary search for the closest point based on x-coordinate
        int left = 0;
        int right = assmts.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            PropertyAssessment currentPoint = assmts.get(mid);
            double distance = Math.abs(currentPoint.getX() - this.x);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = currentPoint;
            }
            if (currentPoint.getX() < this.x) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Check neighboring points
        for (int i = Math.max(0, left - 1); i <= Math.min(assmts.size() - 1, left + 1); i++) {
            PropertyAssessment currentPoint = assmts.get(i);
            double distance = Math.sqrt(Math.pow(currentPoint.getX() - this.x, 2) + Math.pow(currentPoint.getY() - this.y, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = currentPoint;
            }
        }

        return closestPoint.getNeighbourhood();

/*
        PropertyAssessment[][] pointsB = assmts.stream().toArray(PropertyAssessment[][]::new);

        String nearest = assmts.get(0).getNeighbourhood();
        float nearestDist = Float.MAX_VALUE;//this.distBetween(this.x,this.y,assmts.get(0).getX(),assmts.get(0).getY());
        for (int i = 0; i < assmts.size(); i++) {
            float aDist = 1.0f;//this.distBetween(this.x,this.y,a.getX(),a.getY());
            if (aDist < nearestDist) {
                nearestDist = aDist;
                nearest = assmts.get(i).getNeighbourhood();
            }
        }
        return nearest;*/
    }
}

