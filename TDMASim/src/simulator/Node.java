package simulator;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author spiros
 */
public class Node {
    private LinkedList<Package> packageList;
    private final int packageListMaxSize;
    
    public Node(int packageListMaxSize) {
        this.packageListMaxSize = packageListMaxSize;
        packageList = new LinkedList<>();
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
                packageList.add(new Package());
            }
        }
    }
    
    public void increaseDelayTimeOfNodePackages() {
        for(Package networkPackage : packageList) {
            networkPackage.increaseDelay();
        }
    }
    
    public void removePackage() {
        packageList.removeFirst();
    }
    
    public int getDelayTimeOfFirstPackage() {
        return packageList.getFirst().getDelayTime();
    }
}
