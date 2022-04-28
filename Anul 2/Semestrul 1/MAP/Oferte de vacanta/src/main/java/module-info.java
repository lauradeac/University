module practic {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens practic to javafx.fxml;

    exports practic.controller to javafx.fxml;

    exports practic to javafx.graphics;
    opens practic.controller to javafx.fxml;
    opens practic.domain to javafx.base;

}