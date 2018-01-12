package simulator;

import java.util.LinkedList;
import java.util.Random;

/**
 * @author Spiros
 */
public class Station {
    // a queue of waiting packets of the TdmaStation
    private LinkedList<Packet> queueOfPackets;

    // the max size that the packets' queue can reach
    private final int packetListMaxSize;

    // the mean burst-length which we use only when we want bursty simulation
    private final int meanBurstLength;

    // states of the AtdmaStation
    public enum State {
        Idle, Active,
    }

    // the state of the AtdmaStation
    private State state;

    /**
     * trivial constructor
     *
     * @param packetListMaxSize of the TdmaStation
     */
    public Station(int packetListMaxSize) {
        this.packetListMaxSize = packetListMaxSize;
        //trivial initialization
        this.meanBurstLength = 1;
        this.queueOfPackets = new LinkedList<>();
    }

    /**
     * trivial constructor
     *
     * we initialize the TtmaStation with bursty traffic to be idle while we construct it
     *
     * @param packetListMaxSize of the TdmaStation with bursty traffic
     * @param meanBurstLength of the TdmaStation with bursty traffic
     */
    public Station(int packetListMaxSize, int meanBurstLength) {
        this.packetListMaxSize = packetListMaxSize;
        this.meanBurstLength = meanBurstLength;
        this.queueOfPackets = new LinkedList<>();

        this.state = State.Idle;
    }

    /**
     * @return a boolean value if the queue Of Packets is empty
     */
    public boolean isEmpty() {
        return queueOfPackets.isEmpty();
    }

    /**
     * creates a packet using the given pArrival
     *
     * @param pArrival is the probability of the arrival of a packet at the TdmaStation
     */
    public boolean createPacket(double pArrival) {
        Random rand = new Random();
        double random = rand.nextDouble();

        if (queueOfPackets.size() < packetListMaxSize) {
            if (random <= pArrival) {
                // we add a packet to the list
                queueOfPackets.add(new Packet());
                return true;
            }
        }
        return false;
    }

    /**
     * This function tries to change the state of the Atdma Station according to a probability which is selected
     * according to each state and it is defined by R, N and B
     *
     * @param R is the percentage of slots with packet generation
     * @param N is the number of AtdmaNodes that participate in the simulation
     */
    private void tryToChangeState(double R, int N) {
        Random rand = new Random();
        double random = rand.nextDouble();

        if (state == State.Active) {
            double B = meanBurstLength;
            double p10 = 1 / (B * 1.0);
            if (random <= p10) {
                state = State.Idle;
            }
        } else {
            double B = meanBurstLength;
            double p01 = R / (B * (N - R) * 1.0);
            if (random <= p01) {
                state = State.Active;
            }
        }
    }

    /**
     * Acts according to the state of the AtdmaStation
     * if the node is in Active state, it generates a packet per slot and then tries to change state
     * if the node is in Idle state, it tries to change state
     *
     * @param R is the percentage of slots with packet generation
     * @param N is the number of AtdmaNodes that participate in the simulation
     */
    public void actAccordingToState(double R, int N) {
        if (state == State.Active) {
            if (queueOfPackets.size() < packetListMaxSize) {
                // creation of packet
                queueOfPackets.add(new Packet());
            }
            tryToChangeState(R, N);
        } else {
            tryToChangeState(R, N);
        }
    }

    /**
     * increases the delay time of every packet of the TdmaStation by 1
     */
    public void increaseDelayTimeOPackets() {
        for (Packet packet : queueOfPackets) {
            packet.increaseDelay();
        }
    }

    /**
     * removes the first packet of the waiting queue
     */
    public void removeFirstPacket() {
        queueOfPackets.removeFirst();
    }

    /**
     * @return the delay time of the first packet of the queue
     */
    public int getDelayTimeOfFirstPacket() {
        return queueOfPackets.getFirst().getDelayTime();
    }
}
