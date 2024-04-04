package DataParsing;

import java.util.List;

public class Crime {
    private float x;
    private float y;
    private String group;
    private String typeGroup;

    public Crime(String x, String y, String group, String typeGroup) {
        this.x = Float.parseFloat(x);
        this.y = Float.parseFloat(y);
        this.group = group;
        this.typeGroup = typeGroup;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }


    public String getTypeGroup() {
        return this.typeGroup;
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
    }
}

