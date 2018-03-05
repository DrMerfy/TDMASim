import grapher.App;
import simulator.Station;
import simulator.TdmaSimulator;

import java.util.ArrayList;

import static simulator.Station.*;

public class Main {
    public static void main(String[] args) {
        //App.Launch(args);


        ArrayList<Integer> bursty = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            bursty.add(1000);
        }


        TdmaSimulator sim = new TdmaSimulator(10, 200, 1000000);
        System.out.println("Simulation info: \n" + "Number of Stations = 10, max Size Of Packets' List = 200, number of Circles = 1000000, Burst Length = 1000, R = 0.2\n" );

        //*
        System.out.println("simple bursty simulation");
        sim.runBurstyTdmaSimulation(0.2, bursty);
        System.out.println("total time slots = " + sim.getTotalTimeSlots());
        System.out.println("Average delay = " + sim.getAverageDelayTimeSlots());
        System.out.println("Throughput = " + sim.getThroughput());
        System.out.println("Packet Loss Ratio = " + sim.getPacketLossRatio());
        //*/

        //*
        System.out.println("\nbursty simulation with extra header according to LastKnownMaxDelayTimeOfPacketInQueue");
        sim.runBurstyEHTdmaSimulation(0.2, bursty, LastKnownMaxDelayTimeOfPacketInQueue);
        System.out.println("total time slots = " + sim.getTotalTimeSlots());
        System.out.println("Average delay = " + sim.getAverageDelayTimeSlots());
        System.out.println("Throughput = " + sim.getThroughput());
        System.out.println("Packet Loss Ratio = " + sim.getPacketLossRatio());
        //*/

        //*
        System.out.println("\nbursty simulation with extra header according to LastKnownQueueOfPacketsSize");
        sim.runBurstyEHTdmaSimulation(0.2, bursty, LastKnownQueueOfPacketsSize);
        System.out.println("total time slots = " + sim.getTotalTimeSlots());
        System.out.println("Average delay = " + sim.getAverageDelayTimeSlots());
        System.out.println("Throughput = " + sim.getThroughput());
        System.out.println("Packet Loss Ratio = " + sim.getPacketLossRatio());
        //*/

        int nc1 = 6;
        //*
        System.out.println("\nbursty simulation with ordering every " + nc1 + " active-station circles according the their queue of packets size");
        sim.runBurstyASOENCTdmaSimulation(0.2, bursty, nc1, QueueOfPacketsSize);
        System.out.println("total time slots = " + sim.getTotalTimeSlots());
        System.out.println("Average delay = " + sim.getAverageDelayTimeSlots());
        System.out.println("Throughput = " + sim.getThroughput());
        System.out.println("Packet Loss Ratio = " + sim.getPacketLossRatio());
        //*/

        //*
        System.out.println("\nbursty simulation with ordering every " + nc1 + " active-station circles according the their max size packet delay time");
        sim.runBurstyASOENCTdmaSimulation(0.2, bursty, nc1, MaxDelayTimeOfPacketInQueue);
        System.out.println("total time slots = " + sim.getTotalTimeSlots());
        System.out.println("Average delay = " + sim.getAverageDelayTimeSlots());
        System.out.println("Throughput = " + sim.getThroughput());
        System.out.println("Packet Loss Ratio = " + sim.getPacketLossRatio());
        //*/

        int nc2 = 10;
        //*
        System.out.println("\nbursty simulation with ordering every " + nc2 + " active-station circles according the their queue of packets size");
        sim.runBurstyASOENCTdmaSimulation(0.2, bursty, nc2, QueueOfPacketsSize);
        System.out.println("total time slots = " + sim.getTotalTimeSlots());
        System.out.println("Average delay = " + sim.getAverageDelayTimeSlots());
        System.out.println("Throughput = " + sim.getThroughput());
        System.out.println("Packet Loss Ratio = " + sim.getPacketLossRatio());
        //*/

        //*
        System.out.println("\nbursty simulation with ordering every " + nc2 + " active-station circles according the their max size packet delay time");
        sim.runBurstyASOENCTdmaSimulation(0.2, bursty, nc2, MaxDelayTimeOfPacketInQueue);
        System.out.println("total time slots = " + sim.getTotalTimeSlots());
        System.out.println("Average delay = " + sim.getAverageDelayTimeSlots());
        System.out.println("Throughput = " + sim.getThroughput());
        System.out.println("Packet Loss Ratio = " + sim.getPacketLossRatio());
        //*/
    }
}
