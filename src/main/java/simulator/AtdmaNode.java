package simulator;

import java.util.LinkedList;
import java.util.Random;

/**
 * @author spiros
 */
public class AtdmaNode {
    // the queue of the packages of the of the AtdmaNode
    private LinkedList<Package> packageList;

    // the max size that the packages' queue can reach
    private final int packageListMaxSize;

    // the mean burst length
    private final int meanBurstLength;

    // states of the AtdmaNode
    public enum State {
        Idle, Transmitting
    }

    // the state of the AtdmaNode
    private State state;


    /**
     * we initialize the LdtmaNode to be idle while we construct it
     *
     * @param packageListMaxSize of the LdtmaNode
     */
    public AtdmaNode(int packageListMaxSize, int meanBurstLength) {
        this.packageListMaxSize = packageListMaxSize;
        this.meanBurstLength = meanBurstLength;
        this.state = State.Idle;

        this.packageList = new LinkedList<>();
    }


    /**
     * @return a boolean value which indicates if the queue that includes the packages is empty or not
     */
    public boolean isEmpty() {
        return packageList.isEmpty();
    }

    /**
     * This functions changes the state of the Atdma Node according to it's state and the probability given by it
     *
     * @param R is the percentage of slots with package generation
     * @param N is the number of AtdmaNodes that participate in the simulation
     */
    private void tryToChangeState(double R, int N) {
        Random rand = new Random();
        double random = rand.nextDouble();

        if (state == State.Idle) {
            double B = meanBurstLength;
            double p01 = R / (B * (N - R) * 1.0);
            if (random <= p01) {
                state = State.Transmitting;
            }
        } else {
            double B = meanBurstLength;
            double p10 = 1 / (B * 1.0);
            if (random <= p10) {
                state = State.Idle;
            }
        }
    }

    /**
     * Acts according to the state of the AtdmaNode
     * <p>
     * if the node has finished the generation of as many packages as the mean Burst Length, then it is going to
     * change state from transmitting to idle
     *
     * @param R is the percentage of slots with package generation
     * @param N is the number of AtdmaNodes that participate in the simulation
     */
    public void actAccordingToState(double R, int N) {
        if (state == State.Transmitting) {
            if (packageList.size() < packageListMaxSize) {
                // creation of package
                packageList.add(new Package());
            }
            tryToChangeState(R, N);
        } else {
            tryToChangeState(R, N);
        }
    }

    /**
     * increases the delay time of every package of the AtdmaNode by 1
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
