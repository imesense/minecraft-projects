package org.imesense.dynamicspawncontroleditor.dynamicspawncontroleditor;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.imesense.dynamicspawncontroleditor.dynamicspawncontroleditor.core.logfile.LogFile;

import java.io.IOException;
import java.util.Objects;

/**
 *
 */
public class StartEditor extends Application
{
    /**
     *
     * @param args
     */
    public static void main(String... args)
    {
        launch();
    }

    /**
     *
     * @param primaryStage
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException
    {
        String logPath = System.getProperty("user.dir");
        boolean IDEA_RT = true; //System.getProperty("java.class.path").toLowerCase().contains("idea_rt.jar");

        LogFile.createLogFile(logPath, IDEA_RT);

        Parent mainWindowApp = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainWindowApp.fxml")));

        primaryStage.setTitle("Dynamic Spawn Control Editor");

        Scene scene = new Scene(mainWindowApp);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(false);
        primaryStage.show();

        LogFile.writeDataToLogFile(0, "Редактор запущен");
    }
}