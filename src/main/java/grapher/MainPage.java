package grapher;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang.StringUtils;


public class MainPage {

    @FXML public StackPane root;
    @FXML public StackPane node_holder;
    @FXML public StackPane graph_holder;
    @FXML public JFXTextField num_nodes;
    @FXML public JFXTextField list_size;
    @FXML public JFXTextField num_circles;
    @FXML public JFXButton btn_start;

    //Events and catchers
    private EventHandler<ActionEvent> num_nodesChecker;
    private EventHandler<ActionEvent> list_sizeChecker;
    private EventHandler<ActionEvent> num_circlesChecker;
    private EventHandler<ActionEvent> onButtonPressed;

    //User-taken variables
    private int numberOfNodes = 8;
    private int nodeMaxSizeOfPackageList = 3;
    private int numberOfCircles = 10000;

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getNodeMaxSizeOfPackageList() {
        return nodeMaxSizeOfPackageList;
    }

    public int getNumberOfCircles() {
        return numberOfCircles;
    }

    public void setOnStartPressed(EventHandler<ActionEvent> event){
        onButtonPressed = event;
        btn_start.setOnAction(onButtonPressed);
    }

    @FXML
    public void initialize(){
        buildCatchers();
        //Theme setting
        root.getStylesheets().add(App.theme.toString());

        //Internationalization can be added
        num_nodes.setPromptText(String.valueOf(numberOfNodes));
        num_nodes.setFocusTraversable(false);
        num_nodes.setOnAction(num_nodesChecker);
        list_size.setPromptText(String.valueOf(nodeMaxSizeOfPackageList));
        list_size.setFocusTraversable(false);
        list_size.setOnAction(list_sizeChecker);
        num_circles.setPromptText(String.valueOf(numberOfCircles));
        num_circles.setFocusTraversable(false);
        num_circles.setOnAction(num_circlesChecker);

        btn_start.setOnAction(onButtonPressed);
    }

    //Non-Number insertion catchers
    private void buildCatchers(){
        num_nodesChecker = event -> {
            String text = num_nodes.getText();

            int num;
            if (StringUtils.isNumeric(text)){
                num = Integer.parseInt(text);
                num_nodes.pseudoClassStateChanged(App.error, false);

                if (num == 0){
                    num_nodes.pseudoClassStateChanged(App.error, true);
                    return;
                }
                numberOfNodes = num;

            }else {
                num_nodes.pseudoClassStateChanged(App.error, true);
            }
        };

        list_sizeChecker = event -> {
            String text = list_size.getText();

            int num;
            if (StringUtils.isNumeric(text)){
                num = Integer.parseInt(text);
                list_size.pseudoClassStateChanged(App.error, false);

                if (num == 0){
                    list_size.pseudoClassStateChanged(App.error, true);
                    return;
                }
                nodeMaxSizeOfPackageList = num;

            }else {
                list_size.pseudoClassStateChanged(App.error, true);
            }
        };

        num_circlesChecker = event -> {
            String text = num_circles.getText();

            int num;
            if (StringUtils.isNumeric(text)){
                num = Integer.parseInt(text);
                num_circles.pseudoClassStateChanged(App.error, false);

                if (num == 0){
                    num_circles.pseudoClassStateChanged(App.error, true);
                    return;
                }
                numberOfCircles = num;

            }else {
                num_circles.pseudoClassStateChanged(App.error, true);
            }
        };

    }
}
