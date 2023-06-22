module ATP.Project.PartC {
    requires javafx.controls;
    requires javafx.fxml;
    requires atpproject;
    requires javafx.media;
    requires java.desktop;
    requires org.apache.logging.log4j;

    opens View to javafx.fxml;
    exports View;
}
