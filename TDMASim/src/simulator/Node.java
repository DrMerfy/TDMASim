package simulator;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author spiros
 */
public class Node {
    private LinkedList<Integer> packageList;
    private final int packageListMaxSize;
    
    public Node(int packageListMaxSize) {
        this.packageListMaxSize = packageListMaxSize;
        packageList = new LinkedList<Integer>();
    }
    
    public boolean isEmpty() {
        return packageList.isEmpty();
    }

    public void createPackage(double pArrival) {
        Random rand = new Random();
        double  random = rand.nextDouble();
        
        if (packageList.size() < packageListMaxSize) {
            if (random <= pArrival) {
                // we add a package to the list
                packageList.add(1);
            }
        }
    }
    
    public void removePackage() {
        packageList.removeFirst();
    }
    
}
