package org.imesense.dynamicspawncontroleditor.dynamicspawncontroleditor.core.component;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

public class MainWindowApp
{
    @FXML
    private Line line;

    @FXML
    private AnchorPane innerPane;

    @FXML
    public void initialize()
    {
        line.endXProperty().bind(innerPane.widthProperty());
    }
}
