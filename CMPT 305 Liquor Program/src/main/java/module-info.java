module project.cmpt305liquorprogram {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens project.UI to javafx.fxml;
    exports project.UI;
    exports DataCollection;
    opens DataCollection to javafx.fxml;
}