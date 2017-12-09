package grapher;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class MainPage {

    @FXML public StackPane root;
    @FXML public StackPane node_holder;
    @FXML public StackPane graph_holder;
    @FXML public JFXTextField num_nodes;
    @FXML public JFXTextField list_size;
    @FXML public JFXTextField num_circles;

    @FXML
    public void initialize(){
        //Theme setting
        root.getStylesheets().add(App.theme.toString());

        //Internationalization can be added
        num_nodes.setPromptText("Number of nodes");
        num_nodes.setFocusTraversable(false);
        list_size.setPromptText("List size");
        list_size.setFocusTraversable(false);
        num_circles.setPromptText("Number of circles");
        num_circles.setFocusTraversable(false);
    }
}
