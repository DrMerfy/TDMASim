import simulator.TdmaSimulator;

public class Main {
    public static void main(String[] args) {
        // TODO code application logic here

        int numberOfNodes = 8;
        int nodeMaxSizeOfPackageList = 12;
        int numberOfCircles = 1000;

        TdmaSimulator tdma = new TdmaSimulator(numberOfNodes, nodeMaxSizeOfPackageList, numberOfCircles);
        tdma.runSimulator(0.18);

        System.out.println("Number of Nodes = " + tdma.getNumberOfNodes());
        System.out.println("Max size of package list of every node = " + tdma.getNodeMaxSizeOfPackageList());
        System.out.println("Number of Circles = " + tdma.getNumberOfCircles());
        System.out.println("Throughput = " + tdma.getThroughput());
        System.out.println("AverageDelayTime = " + tdma.getAverageDelayTime());
        /*
        for (int i = 0; i < tdma.getDelayTimes().size(); i++) {
            System.out.println("\n Delay time of " + (i+1) + "nd sent package = " + tdma.getDelayTimes().get(i));
        }
        //*/
    }
}
