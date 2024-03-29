package project.UI;

import javafx.scene.control.Label;

// Class that is used for the right hand side labels in the table
public class DataLabel {
    private Label titleLabel;
    private Label valueLabel;

    public DataLabel(String title, String initialValue) {
        titleLabel = new Label(title);
        valueLabel = new Label(initialValue);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public Label getValueLabel() {
        return valueLabel;
    }
}
