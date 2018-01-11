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
    private ArrayList<AtdmaNode> atdmaNodes;

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
        this.atdmaNodes = new ArrayList<>();
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
     * The function that starts the Bursty Simulation of the ATDMA Mac protocol without noise
     * Based on the paper IEEE transactions on communications, vol 51, No 4 April 2003
     * Georgios I. Papadimitriou, Senior Member. IEEE, and Andreas S.Pomportsis
     *
     *
     * @param R is the percentage of slots with package generation
     * @param meanBurstLengths is an array that includes the mean burst lengths of every AtdmaNode
     */
    public void runBurstyAtdmaSimulator(double R, ArrayList<Integer> meanBurstLengths ) {
        // start of initializations
        packagesTransmited = 0;

        atdmaNodes.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackage = new ArrayList<>();

        for (int i = 0; i < nodeMaxSizeOfPackageList; i++) {
            atdmaNodes.add(new AtdmaNode(nodeMaxSizeOfPackageList, meanBurstLengths.get(i)));
        }

        int time = 0;
        // end of initializations

        while (time < maxTime) {
            /* Increase the delay time of each package of each node by 1 and
             * for every Atdma node act according to it's state
             */
            for (AtdmaNode atdmaNode : atdmaNodes) {
                atdmaNode.increaseDelayTimeOfNodePackages();
                atdmaNode.actAccordingToState(R, atdmaNodes.size());
            }

            // Find which node is going to trasmit
            int idOfTransmitingNode = time % atdmaNodes.size();

            // If the node has something to transmit, then transmit the first package of the node.
            if (!atdmaNodes.get(idOfTransmitingNode).isEmpty()) {
                delayTimeOfTransmittedPackage.add(atdmaNodes.get(idOfTransmitingNode).getDelayTimeOfFirstPackage());

                transmit(atdmaNodes.get(idOfTransmitingNode));
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
     * This function removes the package from the AtdmaNode that has the right to transmit (if there is a
     * package to transmit), increases the packages that has been transmitted and completes
     * the transmission of the package
     *
     * @param atdmaNode that has the right to transmit
     */
    private void transmit(AtdmaNode atdmaNode) {
        atdmaNode.removePackage();
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

