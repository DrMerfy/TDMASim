package grapher;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;


public class MainPage {
    @FXML public StackPane root;
    @FXML public StackPane loading_dialog;
    @FXML public StackPane node_holder;
    @FXML public ScrollPane r_holder;
    @FXML public JFXTextField num_nodes;
    @FXML public JFXTextField list_size;
    @FXML public JFXTextField num_circles;
    @FXML public JFXButton btn_start;
    @FXML public JFXToggleButton btn_bursty;
    @FXML public Label r_values;

    //Events and catchers
    private EventHandler<ActionEvent> num_nodesChecker;
    private EventHandler<ActionEvent> list_sizeChecker;
    private EventHandler<ActionEvent> num_circlesChecker;
    private EventHandler<ActionEvent> onButtonPressed;

    //User-taken fields
    private int numberOfNodes = 10;
    private int nodeMaxSizeOfPackageList = 5;
    private int numberOfCircles = 100000;
    private int defaultB = 5;

    //Other fields
    private boolean isBursty = false;
    private JFXSpinner spinner;

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getNodeMaxSizeOfPackageList() {
        return nodeMaxSizeOfPackageList;
    }

    public int getNumberOfCircles() {
        return numberOfCircles;
    }

    public ArrayList<Integer> getRValues() {
        ArrayList<Integer> values = new ArrayList<>();
        Pane r = (Pane) r_holder.getContent();
        int i =1;
        for(Node n : r.getChildren()) {
            i++;
            values.add(Integer.parseInt(((JFXTextField) n).getText()));
        }
        System.out.println("Returned "+i+" integers");
        return values;
    }

    public boolean isBursty(){
        return isBursty;
    }

    public void setOnStartPressed(EventHandler<ActionEvent> event){
        onButtonPressed = event;
        btn_start.setOnAction(onButtonPressed);
    }

    @FXML
    public void initialize(){
        //Make the root focusable
        root.setOnMouseClicked(event -> root.requestFocus());
        r_holder.setVisible(false);
        r_values.setVisible(false);
        loading_dialog.setVisible(true);
        loading_dialog.setMouseTransparent(true);
        loading_dialog.setOpacity(0);
        buildCatchers();
        //Set up the loading
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                spinner = new JFXSpinner();
                spinner.setRadius(40);
                loading_dialog.getChildren().add(spinner);
                return null;
            }
        };
        new Thread(task).start();
        //Theme setting
        root.getStylesheets().add(App.theme.toString());

        //Internationalization can be added
        num_nodes.setPromptText(String.valueOf(numberOfNodes));
        num_nodes.setFocusTraversable(false);
        num_nodes.setOnAction(num_nodesChecker);
        onBlur(num_nodes,num_nodesChecker);
        list_size.setPromptText(String.valueOf(nodeMaxSizeOfPackageList));
        list_size.setFocusTraversable(false);
        list_size.setOnAction(list_sizeChecker);
        onBlur(list_size, list_sizeChecker);
        num_circles.setPromptText(String.valueOf(numberOfCircles));
        num_circles.setFocusTraversable(false);
        num_circles.setOnAction(num_circlesChecker);

        btn_start.setOnAction(onButtonPressed);

        btn_bursty.setOnAction(event -> {
            isBursty = !isBursty;
            if(isBursty){
                createRPane();
                r_holder.setVisible(true);
                r_values.setVisible(true);
            }else{
                r_holder.setVisible(false);
                r_values.setVisible(false);
            }
        });
    }

    public void startLoading() {
        loading_dialog.setOpacity(0.9);
    }

    public void stopLoading() {
        loading_dialog.setVisible(false);
    }

    //Non-Number insertion catchers
    private void buildCatchers(){
        num_nodesChecker = event -> {
            String text = num_nodes.getText();

            if(text.equals(""))
                return;

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
            createRPane();
        };

        list_sizeChecker = event -> {
            String text = list_size.getText();


            if(text.equals(""))
                return;

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
            defaultB = nodeMaxSizeOfPackageList;
            createRPane();
        };

        num_circlesChecker = event -> {
            String text = num_circles.getText();


            if(text.equals(""))
                return;

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

    private void createRPane() {
        r_holder.setContent(null);
        GridPane r = new GridPane();
        for(int i =0; i<numberOfNodes; i++) {
            JFXTextField textField = new JFXTextField();
            textField.setText(String.valueOf(defaultB));
            GridPane.setMargin(textField, new Insets(10,0,0,0));
            r.add(textField,1,i);
        }
        r_holder.setContent(r);
    }

    private static void onBlur(Node node, EventHandler<ActionEvent> event){
        node.focusedProperty().addListener((observable,oldValue,newValue) -> {
            if(oldValue && !newValue) {
                event.handle(new ActionEvent());
            }
        });
    }
}
