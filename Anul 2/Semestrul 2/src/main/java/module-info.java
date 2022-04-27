module socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.jfr;
    requires java.sql;
    requires javafx.graphics;

    opens socialnetwork to javafx.fxml, javafx.base;
    opens socialnetwork.controller to javafx.fxml;

    opens socialnetwork.domain to javafx.fxml, javafx.base;


    exports socialnetwork.controller to javafx.fxml, javafx.graphics;
    exports socialnetwork.domain to javafx.fxml, javafx.graphics;
    exports socialnetwork to javafx.fxml, javafx.graphics;

}