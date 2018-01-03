package simulator;


import java.util.ArrayList;

/**
 * @author spiros
 */
public class TdmaSimulator {
    // variables that are given by the user
    private int numberOfNodes;
    private int nodeMaxSizeOfPackageList;
    private int numberOfCircles;

    // An ArrayList which contains the nodes which can transmit
    private ArrayList<Node> nodes;

    // variable which is calculated from the given variables
    private final int maxTime;

    // variables which are calculated after the end of the Simulation
    private int packagesTransmited;
    private double averageDelayTime;

    /**
     * The constructor of the Simulator
     *
     * @param numberOfNodes            that we assign for the simulation
     * @param nodeMaxSizeOfPackageList that we assign for the simulation
     * @param numberOfCircles          that we assign for the simulation
     */
    public TdmaSimulator(int numberOfNodes, int nodeMaxSizeOfPackageList, int numberOfCircles) {
        this.numberOfNodes = numberOfNodes;
        this.nodeMaxSizeOfPackageList = nodeMaxSizeOfPackageList;
        this.numberOfCircles = numberOfCircles;

        this.maxTime = numberOfNodes * numberOfCircles;

        this.nodes = new ArrayList<>();
    }

    /**
     * The function that starts the uniform Simulation of the TDMA Mac protocol
     *
     * @param pArrival is the possibility of the arrival of a package at every node
     */
    public void runUniformSimulator(double pArrival) {
        // start of initializations
        packagesTransmited = 0;

        nodes.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackage = new ArrayList<>();

        for (int i = 0; i < nodeMaxSizeOfPackageList; i++) {
            nodes.add(new Node(nodeMaxSizeOfPackageList));
        }

        int time = 0;
        // end of initializations

        while (time < maxTime) {
            /* Increase the delay time of each package of each node by 1 and
             * Create a package for each node with possibility pArrival
             */
            for (Node node : nodes) {
                node.increaseDelayTimeOfNodePackages();
                node.createPackage(pArrival);
            }

            // Find which node is going to trasmit
            int idOfTransmitingNode = time % nodes.size();

            // If the node has something to transmit, then transmit the first package of the node.
            if (!nodes.get(idOfTransmitingNode).isEmpty()) {
                delayTimeOfTransmittedPackage.add(nodes.get(idOfTransmitingNode).getDelayTimeOfFirstPackage());

                transmit(nodes.get(idOfTransmitingNode));
            }
            // Go to the next slot time.
            time++;
        }

        // calculation of the average delay time
        int sumDelays = 0;
        for (Integer delay : delayTimeOfTransmittedPackage) {
            sumDelays += delay;
        }
        averageDelayTime = sumDelays / (packagesTransmited * 1.0);
    }

    /**
     * The function that starts the Bursty Simulation of the TDMA Mac protocol
     *
     * @param pArrival is the possibility of the arrival of a package at every node
     */
    public void runBurstySimulator(double pArrival) {
        // start of initializations
        packagesTransmited = 0;

        nodes.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackage = new ArrayList<>();
        ArrayList<Double> pArrivalOfNodes = new ArrayList<>();

        //initializing the starting possibilities of the arrival of a package at every node
        for (int i = 0; i < numberOfNodes; i++) {
            pArrivalOfNodes.add(new Double(0));
            pArrivalOfNodes.set(i, pArrival);
        }

        for (int i = 0; i < nodeMaxSizeOfPackageList; i++) {
            nodes.add(new Node(nodeMaxSizeOfPackageList));
        }

        int time = 0;
        // end of initializations

        while (time < maxTime) {
            /* Increase the delay time of each package of each node by 1 and
             * Create a package for each node with possibility pArrival
             */
            for (int i = 0; i < nodes.size(); i++) {
                nodes.get(i).increaseDelayTimeOfNodePackages();
                boolean arrived = nodes.get(i).createPackage(pArrivalOfNodes.get(i));

                // we set the high and low limits of the increase or decrease
                if (pArrivalOfNodes.get(i) > 0.05 && pArrivalOfNodes.get(i) < 0.99) {
                    // if a package arrives, we increase the possibility of the arrival of the next package
                    if (arrived == true) {
                        pArrivalOfNodes.set(i, pArrivalOfNodes.get(i) + 0.01);
                    // if a package does not arrive, we decrease the possibility of the arrival of the next package
                    } else {
                        pArrivalOfNodes.set(i, pArrivalOfNodes.get(i) - 0.01);
                    }
                }
            }

            // Find which node is going to trasmit
            int idOfTransmitingNode = time % nodes.size();

            // If the node has something to transmit, then transmit the first package of the node.
            if (!nodes.get(idOfTransmitingNode).isEmpty()) {
                delayTimeOfTransmittedPackage.add(nodes.get(idOfTransmitingNode).getDelayTimeOfFirstPackage());

                transmit(nodes.get(idOfTransmitingNode));
            }

            // Go to the next slot time.
            time++;
        }

        // calculation of the average delay time
        int sumDelays = 0;
        for (Integer delay : delayTimeOfTransmittedPackage) {
            sumDelays += delay;
        }
        averageDelayTime = sumDelays / (packagesTransmited * 1.0);
    }

    /**
     * This function removes the package from the node that has the right transmit (if there is a
     * package to transmit), increases the packages that has been transmitted and completes
     * the transmission of the package
     *
     * @param node that has the right to transmit
     */
    private void transmit(Node node) {
        node.removePackage();
        packagesTransmited++;
    }

    /**
     * @return the Throughput of the Simulation
     */
    public double getThroughput() {
        return packagesTransmited / (numberOfNodes * numberOfCircles * 1.0);
    }

    /**
     * @return the Avarage Delay time of all the transmitted packages
     */
    public double getAverageDelayTime() { return averageDelayTime; }

    /**
     * @return the number of nodes that we set in order to run the simulation
     */
    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    /**
     * @return the max size of the Package List that we set in order to run the simulation
     */
    public int getNodeMaxSizeOfPackageList() {
        return nodeMaxSizeOfPackageList;
    }

    /**
     * @return the number of circles that we set in order the run the simultation
     */
    public int getNumberOfCircles() {
        return numberOfCircles;
    }
}

