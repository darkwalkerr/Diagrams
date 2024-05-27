module com.server.diagramserver {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.datatransfer;

    opens com.server.diagramserver to javafx.fxml;
    exports com.server.diagramserver;
}