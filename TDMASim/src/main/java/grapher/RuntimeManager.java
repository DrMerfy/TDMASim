package grapher;

import graphs.LineGraph;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import plotter.Plotter;
import simulator.TdmaSimulator;

import java.io.IOException;
import java.util.ArrayList;

class RuntimeManager {
    private static MainPage mn;
    private static LineGraph th$p;
    private static LineGraph del$th;

    private static final int width = 800;
    private static final int height = 400;

    public static void start() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/MainPage.fxml"));
            App.showStage(
                    new Scene(loader.load()), "TDMA Simulator");
            mn = loader.getController();
            mn.setOnStartPressed(event -> startSimulator());

            //Graph initialization
            th$p = new LineGraph(width, height);
            del$th = new LineGraph(width, height);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void startSimulator() {
        TdmaSimulator sim = new TdmaSimulator(mn.getNumberOfNodes(), mn.getNodeMaxSizeOfPackageList(), mn.getNumberOfCircles());
        th$p.setInterval(40);

        ArrayList<Double> delay = new ArrayList<>();
        ArrayList<Double> throughput = new ArrayList<>();
        for(double p = 0.05; p <= 1; p+=0.05){
            sim.runSimulator(p);
            delay.add(sim.getAverageDelayTime());
            throughput.add(sim.getThroughput());
            th$p.addValue(sim.getThroughput());
        }

        for(int i = 0; i<throughput.size(); i++){
            del$th.addPoint(throughput.get(i)*1000,delay.get(i));
        }

        //Colorization
        th$p.getGraphPath().setStroke(new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,
                new Stop(0.5,Color.valueOf("#A00000")),new Stop(1, Color.valueOf("#0000EA"))));
        del$th.getGraphPath().setStroke(new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,
                new Stop(0.7,Color.valueOf("#A00000")),new Stop(0, Color.valueOf("#0000EA"))));
        //Other style related
        th$p.getGraphPath().setStrokeWidth(3);
        th$p.setClose(false);

        del$th.getGraphPath().setStrokeWidth(3);
        //Rendering
        th$p.render(LineGraph.Render.GRAPH);
        th$p.render(LineGraph.Render.LINES);
        del$th.render(LineGraph.Render.GRAPH);
        del$th.render(LineGraph.Render.LINES);

        //Scene management
        AnchorPane pane1 = new AnchorPane(th$p);
        AnchorPane.setLeftAnchor(th$p,30.0);
        AnchorPane.setBottomAnchor(th$p, 30.0);

        NumberAxis xAxis = new NumberAxis(0, th$p.getMaxValue(), 1);
        pane1.getChildren().add(xAxis);
        AnchorPane.setBottomAnchor(xAxis,0.0);
        AnchorPane.setLeftAnchor(xAxis, 30.0);

        Plotter.mapXAxis(0,1,th$p);
        Plotter.plot(th$p, del$th);
    }

}
