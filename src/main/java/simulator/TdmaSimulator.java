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
    private ArrayList<TdmaNode> tdmaNodes;
    private ArrayList<LtdmaNode> ltdmaNodes;

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

        this.tdmaNodes = new ArrayList<>();
        this.ltdmaNodes = new ArrayList<>();
    }

    /**
     * The function that starts the uniform Simulation of the TDMA Mac protocol
     *
     * @param pArrival is the possibility of the arrival of a package at every tdmaNodes
     */
    public void runUniformTdmaSimulator(double pArrival) {
        // start of initializations
        packagesTransmited = 0;

        tdmaNodes.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackage = new ArrayList<>();

        for (int i = 0; i < nodeMaxSizeOfPackageList; i++) {
            tdmaNodes.add(new TdmaNode(nodeMaxSizeOfPackageList));
        }

        int time = 0;
        // end of initializations

        while (time < maxTime) {
            /* Increase the delay time of each package of each node by 1 and
             * Create a package for each node with possibility pArrival
             */
            for (TdmaNode tdmaNode : tdmaNodes) {
                tdmaNode.increaseDelayTimeOfNodePackages();
                tdmaNode.createPackage(pArrival);
            }

            // Find which node is going to trasmit
            int idOfTransmitingNode = time % tdmaNodes.size();

            // If the node has something to transmit, then transmit the first package of the node.
            if (!tdmaNodes.get(idOfTransmitingNode).isEmpty()) {
                delayTimeOfTransmittedPackage.add(tdmaNodes.get(idOfTransmitingNode).getDelayTimeOfFirstPackage());

                transmit(tdmaNodes.get(idOfTransmitingNode));
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
     * The function that starts the Bursty Simulation of the LTDMA Mac protocol, without noise
     *
     * @param R is the percentage of slots with package generation
     * @param meanBurstLengths is an array that includes the mean burst lengths of every LtdmaNode
     */
    public void runBurstyLtdmaSimulator(double R, ArrayList<Integer> meanBurstLengths ) {
        // start of initializations
        packagesTransmited = 0;

        ltdmaNodes.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackage = new ArrayList<>();

        for (int i = 0; i < nodeMaxSizeOfPackageList; i++) {
            ltdmaNodes.add(new LtdmaNode(nodeMaxSizeOfPackageList, meanBurstLengths.get(i)));
        }

        int time = 0;
        // end of initializations

        while (time < maxTime) {
            /* Increase the delay time of each package of each node by 1 and
             * for every Ltdma node act according to it's state
             */
            for (LtdmaNode ltdmaNode : ltdmaNodes) {
                ltdmaNode.increaseDelayTimeOfNodePackages();
                ltdmaNode.actAccordingToState(R, ltdmaNodes.size());
            }

            // Find which node is going to trasmit
            int idOfTransmitingNode = time % ltdmaNodes.size();

            // If the node has something to transmit, then transmit the first package of the node.
            if (!ltdmaNodes.get(idOfTransmitingNode).isEmpty()) {
                delayTimeOfTransmittedPackage.add(ltdmaNodes.get(idOfTransmitingNode).getDelayTimeOfFirstPackage());

                transmit(ltdmaNodes.get(idOfTransmitingNode));
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
     * This function removes the package from the TdmaNode that has the right to transmit (if there is a
     * package to transmit), increases the packages that has been transmitted and completes
     * the transmission of the package
     *
     * @param tdmaNode that has the right to transmit
     */
    private void transmit(TdmaNode  tdmaNode ) {
        tdmaNode.removePackage();
        packagesTransmited++;
    }

    /**
     * This function removes the package from the LtdmaNode that has the right to transmit (if there is a
     * package to transmit), increases the packages that has been transmitted and completes
     * the transmission of the package
     *
     * @param ltdmaNode that has the right to transmit
     */
    private void transmit(LtdmaNode ltdmaNode) {
        ltdmaNode.removePackage();
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

