package DataParsing;

public class AssessmentClass {
    private int[] percents = new int[3];
    private String[] classes = new String[3];

    public AssessmentClass(int p1, int p2, int p3, String c1, String c2, String c3) {
        this.percents[0] = p1;
        this.percents[1] = p2;
        this.percents[2] = p3;
        this.classes[0] = c1;
        this.classes[1] = c2;
        this.classes[2] = c3;
    }

    public String toString() {
        int i = 0;
        String returnable = "";
        while (i < 3) {
            if (this.classes[i] != null && !this.classes[i].isEmpty() && !this.classes[i].isBlank()) {
                returnable += this.classes[i] + " (" + this.percents[i] + "%) ";
            }
            i++;
        }
        return (returnable);
    }

    public int hashCode() {
        return Integer.hashCode(this.classes.length);
    }

    public int[] getPercents() {
        return this.percents;
    }

    public String classAtI(int i) {
        return this.classes[i];
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PropertyAssessment)) {
            return false;
        }

        AssessmentClass assClass = (AssessmentClass) o;
        return (this.percents == assClass.getPercents());
    }
}
