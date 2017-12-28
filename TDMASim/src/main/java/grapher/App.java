package grapher;

import graphs.LineGraph;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import plotter.Plotter;
import simulator.TdmaSimulator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class App extends Application {
    private static Stage currentStage;

    static PseudoClass error = PseudoClass.getPseudoClass("error");
    static URL theme;

    public static void Launch(String args[]){
        Application.launch(args);
    }

    public static void showStage(Scene scene, String title){
        currentStage.setTitle(title);
        currentStage.setScene(scene);
        currentStage.show();
    }

    @Override
    public void start(Stage stage) throws IOException {
        theme = App.class.getResource("/defaultTheme.css");
        currentStage = stage;
        RuntimeManager.start();
    }
}
