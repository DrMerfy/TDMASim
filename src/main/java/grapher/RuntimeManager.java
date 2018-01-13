package grapher;

import graphs.LineGraph;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
            mn.setOnStartPressed(event ->{
                mn.startLoading();
                //Graph initialization
                th$p = new LineGraph(width, height);
                del$th = new LineGraph(width, height);

                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        startSimulator();
                        return this;
                    }
                };
                task.setOnSucceeded(event1 ->{
                    mn.stopLoading();
                    Plotter.plot(th$p, del$th);
                });
                new Thread(task).start();
            });



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void startSimulator() {
        TdmaSimulator sim = new TdmaSimulator(mn.getNumberOfNodes(), mn.getNodeMaxSizeOfPackageList(), mn.getNumberOfCircles());
        th$p.setInterval(40);


        ArrayList<Double> delay = new ArrayList<>();
        ArrayList<Double> throughput = new ArrayList<>();

        if(!mn.isBursty()) {
            for (double p = 0.05; p <= 1; p += 0.05) {
                sim.runTdmaSimulation(p);
                delay.add(sim.getAverageDelayTimeSlots());
                throughput.add(sim.getThroughput());
                th$p.addValue(sim.getThroughput());
            }
            Plotter.setTitle("TDMA Simulation", th$p);
            Plotter.setTitle("TDMA Simulation", del$th);
            Plotter.setAxisLabel("probability of packet arrival","throughput(%)",th$p);
        }else {
            ArrayList<Integer> rValues = mn.getRValues();
            for (double p = 0.05; p <= 1; p += 0.05) {
                sim.runBurstyTdmaSimulator(p,rValues);
                delay.add(sim.getAverageDelayTimeSlots());
                throughput.add(sim.getThroughput());
                th$p.addValue(sim.getThroughput());
            }
            del$th.setSmoothing(0);
            Plotter.setTitle("TDMA Bursty traffic Simulation", th$p);
            Plotter.setTitle("TDMA Bursty traffic Simulation", del$th);
            Plotter.setAxisLabel("percentage of slots with packet generation","throughput(%)",th$p);
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
        Plotter.mapXAxis(0,1,th$p);
        Plotter.mapYAxis(0,100,th$p);
        Plotter.setAxisLabel("throughput(%)", "delay(slots)", del$th);
        Plotter.mapXAxis(0,100,del$th);

    }

}
