package grapher;

import graphs.LineGraph;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import plotter.Plotter;
import simulator.TdmaSimulator;
import sun.security.krb5.internal.PAData;

import java.io.IOException;
import java.util.ArrayList;

class RuntimeManager {
    private static MainPage mn;
    private static LineGraph th$p;
    private static LineGraph del$th;

    private static final int width = 1000;
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

        th$p.getGraphPath().setStroke(new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,
                new Stop(0.5,Color.valueOf("#A00000")),new Stop(1, Color.valueOf("#0000EA"))));
        del$th.getGraphPath().setStroke(new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE,
                new Stop(0.5,Color.valueOf("#A00000")),new Stop(1, Color.valueOf("#0000EA"))));

        Plotter.plot(th$p, del$th);
    }

    /*LineGraph graphr = new LineGraph(800, 300);
        LineGraph graph = new LineGraph(800,300);
        //pane.getChildren().add(graph);

        TdmaSimulator tdma = new TdmaSimulator(numberOfNodes, nodeMaxSizeOfPackageList, numberOfCircles);


        graphr.setInterval(40);
        ArrayList<Double> delay = new ArrayList<>();
        ArrayList<Double> throughput = new ArrayList<>();
        for(double p = 0.05; p <= 1; p+=0.05){
            tdma.runSimulator(p);
            delay.add(tdma.getAverageDelayTime());
            throughput.add(tdma.getThroughput());
            graphr.addValue(tdma.getThroughput());
            System.out.println(tdma.getThroughput());
        }

        for(int i = 0; i<throughput.size(); i++){
            graph.addPoint(throughput.get(i)*1000,delay.get(i));
        }

        graphr;*/

}
