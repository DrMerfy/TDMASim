package simulator;

import java.util.ArrayList;

/**
 *
 * @author spiros
 */
public class TdmaSimulator {
    private final int numberOfNodes;
    private final int numberOfCircles;
    private final int nodeMaxSizeOfPackageList;
    private double pArrival;
    private int time;
    private final int maxTime;
    
    private ArrayList<Node> nodes;

    public TdmaSimulator(int numberOfNodes, int numberOfCircles, int nodeMaxSizeOfPackageList, double pArrival) {
        this.numberOfNodes = numberOfNodes;
        this.numberOfCircles = numberOfCircles;
        this.nodeMaxSizeOfPackageList = nodeMaxSizeOfPackageList;
        this.maxTime = numberOfNodes * numberOfCircles;
        
        nodes = new ArrayList<>(numberOfNodes);
        
        for (Node node : nodes) {
            node = new Node(nodeMaxSizeOfPackageList);
        }
    }
    
    public void runSimulator() {
        this.time = 0;
        while (time < maxTime) {
            //Create a package to each node with possibility p_arrival
            for (Node node : nodes) {
                node.createPackage(pArrival);
            }

            //Find which node is going to trasmit
            int idOfTransmitingNode =  time % numberOfNodes;
            
              //If the node has something to transmit, then transmit one package.
            if(!nodes.get(idOfTransmitingNode).isEmpty()) {
              transmit(nodes.get(idOfTransmitingNode));
            }

            //Go to the next slot time.
            time++;
        }
    }
    
    private void transmit (Node node) {
        node.removePackage();
    }
}
