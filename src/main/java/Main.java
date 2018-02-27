import grapher.App;
import simulator.Station;
import simulator.TdmaSimulator;

import static simulator.Station.LastKnownMaxDelayTimeOfPacketInQueue;
import static simulator.Station.LastKnownQueueOfPacketsSize;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //App.Launch(args);


        ArrayList<Integer> bursty = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            bursty.add(10);
        }


        TdmaSimulator sim = new TdmaSimulator(10, 5, 1000000);

        //*
        System.out.println("simple bursty simulation");
        sim.runBurstyTdmaSimulation(0.2, bursty);
        System.out.println("total time slots = " + sim.getTotalTimeSlots());
        System.out.println("Average delay = " + sim.getAverageDelayTimeSlots());
        System.out.println("Throughput = " + sim.getThroughput());
        System.out.println("Packet Loss Ratio = " + sim.getPacketLossRatio());
        //*/

        //*
        System.out.println("bursty simulation with extra header according to LastKnownMaxDelayTimeOfPacketInQueue");
        sim.runBurstyEHTdmaSimulation(0.2, bursty, LastKnownMaxDelayTimeOfPacketInQueue);
        System.out.println("\ntotal time slots = " + sim.getTotalTimeSlots());
        System.out.println("Average delay = " + sim.getAverageDelayTimeSlots());
        System.out.println("Throughput = " + sim.getThroughput());
        System.out.println("Packet Loss Ratio = " + sim.getPacketLossRatio());
        //*/

        //*
        System.out.println("bursty simulation with extra header according to LastKnownQueueOfPacketsSize");
        sim.runBurstyEHTdmaSimulation(0.2, bursty, LastKnownQueueOfPacketsSize);
        System.out.println("\ntotal time slots = " + sim.getTotalTimeSlots());
        System.out.println("Average delay = " + sim.getAverageDelayTimeSlots());
        System.out.println("Throughput = " + sim.getThroughput());
        System.out.println("Packet Loss Ratio = " + sim.getPacketLossRatio());
        //*/
    }

}
