package simulator;

import java.util.LinkedList;
import java.util.Random;

/**
 * @author spiros
 */
public class LtdmaNode {
    // the queue of the packages of the of the LtdmaNode
    private LinkedList<Package> packageList;

    // the max size that the queue can reach
    private final int packageListMaxSize;

    // the mean burst length
    private final int meanBurstLength;

    private int currentBurstLength;

    // the state of the LtdmaNode
    public enum State {
        Idle, Transmitting
    }

    private State state;



    /**
     * we initialize the LdtmaNode to be idle while we construct it
     *
     * @param packageListMaxSize of the LdtmaNode
     */
    public LtdmaNode(int packageListMaxSize, int meanBurstLength) {
        this.packageListMaxSize = packageListMaxSize;
        this.meanBurstLength = meanBurstLength;
        this.state = State.Idle;
        this.currentBurstLength = 0;

        this.packageList = new LinkedList<>();
    }


    /**
     * @return a boolean value which indicates if the queue that includes the packages is empty or not
     */
    public boolean isEmpty() {
        return packageList.isEmpty();
    }

    private void tryToChangeState (double R, int N) {
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
                currentBurstLength = 0;
            }
        }
    }

    /**
     * acts according to the state of the LtdmaNode
     *
     * if the node has finished the generation of as many packages as the mean Burst Length, then it is going to
     * change state from transmitting to idle
     *
     * @param R is the percentage of slots with package generation
     * @param N is the number of LtdmaNodes that participate in the simulation
     */
    public void actAccordingToState(double R, int N) {
        if (state == State.Transmitting) {
            if (currentBurstLength < meanBurstLength) {
                if (packageList.size() < packageListMaxSize) {
                    // creation of package
                    packageList.add(new Package());
                }
                currentBurstLength++;
                tryToChangeState(R, N);
            } else {
                state = State.Idle;
                currentBurstLength = 0;
            }
        } else {
            tryToChangeState(R, N);
        }
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
