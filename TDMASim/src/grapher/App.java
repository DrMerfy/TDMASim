package grapher;

import grapher.graph.LineGraph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Random;

public class App extends Application {
    private static String title = "TDMA Simulator";
    static URL theme;

    public static void Launch(String args[]){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        theme = App.class.getResource("/defaultTheme.css");

        //Scene scene = new Scene(FXMLLoader.load(App.class.getResource("/MainPage.fxml")));
        Pane pane = new Pane();
        LineGraph graph = new LineGraph(800, 500);
        pane.getChildren().add(graph);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

        Random random = new Random();
        graph.setInterval(50);
        int[] values = new int[]{50, 70, 70, 42, 50, 80, 90, 90, 52, 22, 20, 52, 48, 42, 54, 20, 19, 24, 40, 82, 78, 52, 51, 62, 62};
        //int[] values = new int[]{10,40,30,5,45,10,45,10};
        //for(int i = 0; i < 800/50; i++)
          //  graph.addValue(random.nextInt(500));
        for(int v : values)
            graph.addValue(v*5);

        graph.render();
    }
}
