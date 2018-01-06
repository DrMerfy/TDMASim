package simulator;

import java.util.LinkedList;
import java.util.Random;

/**
 * @author spiros
 */
public class TdmaNode {
    // the queue of the packages of the of the TdmaNode
    private LinkedList<Package> packageList;

    // the max size that the queue can reach
    private final int packageListMaxSize;

    /**
     * trivial constructor
     *
     * @param packageListMaxSize of the TdmaNode
     */
    public TdmaNode(int packageListMaxSize) {
        this.packageListMaxSize = packageListMaxSize;
        this.packageList = new LinkedList<>();
    }

    /**
     * @return a boolean value which indicates if the queue that includes the packages is empty or not
     */
    public boolean isEmpty() {
        return packageList.isEmpty();
    }

    /**
     * creates a packages using the given pArrival
     *
     * @param pArrival is the possibility of the arriving of a package at the TdmaNode
     */
    public boolean createPackage(double pArrival) {
        Random rand = new Random();
        double random = rand.nextDouble();

        if (packageList.size() < packageListMaxSize) {
            if (random <= pArrival) {
                // we add a package to the list
                packageList.add(new Package());
                return true;
            }
        }
        return false;
    }

    /**
     * increases the delay time of every package of the TdmaNode by 1
     */
    public void increaseDelayTimeOfNodePackages() {
        for (Package networkPackage : packageList) {
            networkPackage.increaseDelay();
        }
    }

    /**
     * removes the first package of the queue
     */
    public void removePackage() {
        packageList.removeFirst();
    }

    /**
     * @return the delay time of the first package of the queue
     */
    public int getDelayTimeOfFirstPackage() {
        return packageList.getFirst().getDelayTime();
    }
}
