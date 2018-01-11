import grapher.App;
import simulator.TdmaSimulator;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        App.Launch(args);

        //*
        TdmaSimulator tdmaSimulator = new TdmaSimulator(8, 6, 100000);
        ArrayList<Integer> meanBurstLengths = new ArrayList<>();

        meanBurstLengths.add(6);
        meanBurstLengths.add(6);
        meanBurstLengths.add(5);
        meanBurstLengths.add(5);
        meanBurstLengths.add(5);
        meanBurstLengths.add(4);
        meanBurstLengths.add(4);
        meanBurstLengths.add(4);

        tdmaSimulator.runBurstyAtdmaSimulator(0.18, meanBurstLengths);

        System.out.println("Throughput =  " + tdmaSimulator.getThroughput());
        System.out.println("Average Delay Time =  " + tdmaSimulator.getAverageDelayTime());
        //*/

    }
}
