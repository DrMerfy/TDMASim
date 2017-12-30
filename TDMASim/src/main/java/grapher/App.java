package grapher;

import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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

    public static void newStage(Scene scene, String title){
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage stage) throws IOException {
        theme = App.class.getResource("/defaultTheme.css");
        currentStage = stage;
        RuntimeManager.start();
    }
}
