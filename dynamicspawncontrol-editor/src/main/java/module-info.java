module org.imesense.dynamicspawncontroleditor.dynamicspawncontroleditor
{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens org.imesense.dynamicspawncontroleditor.dynamicspawncontroleditor to javafx.fxml;
    exports org.imesense.dynamicspawncontroleditor.dynamicspawncontroleditor;
    exports org.imesense.dynamicspawncontroleditor.dynamicspawncontroleditor.core.component;
    opens org.imesense.dynamicspawncontroleditor.dynamicspawncontroleditor.core.component to javafx.fxml;
}