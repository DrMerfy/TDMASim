package simulator;

import java.util.ArrayList;

/**
 *
 * @author spiros
 */
public class TdmaSimulator {
    private int numberOfNodes;
    private int nodeMaxSizeOfPackageList;
    private int  numberOfCircles;
    
    private int time;
    private final int maxTime;
    private long packagesTransmited;
    
    private ArrayList<Node> nodes;
    
    private ArrayList<Integer> delayTimeOfTransmittedPackage;

    public TdmaSimulator(int numberOfNodes, int nodeMaxSizeOfPackageList, int numberOfCircles) {
        this.numberOfNodes = numberOfNodes;
        this.nodeMaxSizeOfPackageList = nodeMaxSizeOfPackageList;
        this.numberOfCircles = numberOfCircles;
        
        this.maxTime = numberOfNodes * numberOfCircles;
        
        this.nodes = new ArrayList<>();
        this.delayTimeOfTransmittedPackage = new ArrayList<>();
    }
    
    public void runSimulator(double pArrival) {
        packagesTransmited = 0;
        
        nodes.clear();
        delayTimeOfTransmittedPackage.clear();
        
        for (int i = 0; i < nodeMaxSizeOfPackageList; i++) {
            nodes.add(new Node(nodeMaxSizeOfPackageList));
        }
        
        time = 0;
        while (time < maxTime) {
            /* Increase the delay time of each package of each node by one and
             * Create a package for each node with possibility p_arrival        
             */         
            for (Node node : nodes) {
                node.increaseDelayTimeOfNodePackages();
                node.createPackage(pArrival);
            }

            // Find which node is going to trasmit
            int idOfTransmitingNode =  time % nodes.size();
            
            // If the node has something to transmit, then transmit the first package of the node.
            if(!nodes.get(idOfTransmitingNode).isEmpty()) {
                delayTimeOfTransmittedPackage.add(nodes.get(idOfTransmitingNode).getDelayTimeOfFirstPackage());
                
                transmit(nodes.get(idOfTransmitingNode));
            }
            // Go to the next slot time.
            time++;
        }
    }
    
    private void transmit (Node node) {
        node.removePackage();
        packagesTransmited++;
    }
    
    public long getThroughput() {
        return packagesTransmited;
    }
    
    public double getAverageDelayTime() {
        long sumDelays = 0;
        for(Integer delay: delayTimeOfTransmittedPackage) {
            sumDelays += delay;
        }
        return sumDelays / (packagesTransmited * 1.0);
    }
    
    public ArrayList<Integer> getDelayTimes() {
        return delayTimeOfTransmittedPackage;
    }
    
    public int getNumberOfNodes() {
        return numberOfNodes;
    }
    
    public int getNodeMaxSizeOfPackageList() {
        return nodeMaxSizeOfPackageList;
    }
    
    public int getNumberOfCircles() {
        return numberOfCircles;
    }
}
