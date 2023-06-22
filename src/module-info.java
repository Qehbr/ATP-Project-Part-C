module ATP.Project.PartC {
    requires javafx.controls;
    requires javafx.fxml;
    requires atpproject;
    requires javafx.media;
    requires java.desktop;


    opens View to javafx.fxml;
    exports View;
}
