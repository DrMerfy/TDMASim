package simulator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;


/**
 * @author Spiros
 */
public class Station {
    // station id number
    private int id;

    // a queue of waiting packets of the TdmaStation
    private LinkedList<Packet> queueOfPackets;

    // the max size that the packets' queue can reach
    private final int packetListMaxSize;

    // the size of queue of the packets of the station that we get when it tries to transmit
    private int lastKnownQueueOfPacketsSize;

    // the last known max Delay time of packet in queue
    private int lastKnownMaxDelayTimeOfPacketInQueue;

    // the mean burst-length which we use only when we want bursty simulation
    private final int meanBurstLength;

    // state of the Station which indicates if a station is producing packets or not
    public enum State {
        Idle, Active,
    }

    // the state of the Station
    private State state;

    /**
     * trivial constructor
     *
     * @param id                of the station
     * @param packetListMaxSize of the Station
     */
    public Station(int id, int packetListMaxSize) {
        this.id = id;
        this.packetListMaxSize = packetListMaxSize;
        //trivial initialization
        this.meanBurstLength = 1;
        this.queueOfPackets = new LinkedList<>();
    }

    /**
     * trivial constructor
     * we initialize the TtmaStation with bursty traffic to be idle while we construct it
     *
     * @param id                of the Station
     * @param packetListMaxSize of the Station with bursty traffic
     * @param meanBurstLength   of the Station with bursty traffic
     */
    public Station(int id, int packetListMaxSize, int meanBurstLength) {
        this.id = id;
        this.packetListMaxSize = packetListMaxSize;
        this.meanBurstLength = meanBurstLength;
        this.queueOfPackets = new LinkedList<>();

        this.state = State.Idle;
    }

    /**
     * @return the size size of the queue of packets of a station
     */
    public int getQueueOfPacketsSize() {
        return queueOfPackets.size();
    }

    /**
     * creates a packet using the given pArrival
     *
     * @param pArrival is the probability of the arrival of a packet at the TdmaStation
     * @return a int value which indicates if there was a non generation or a packet loss or a generation of a packet
     */
    public int createPacket(double pArrival) {
        /*
         * if packetGenerationState = -1, the station did not generate a packet.
         * else if packetGenerationState = 0, the station generated a packet, but there was a packet loss.
         * else if packetGenerationState = 1, the station generated a packet and stored it  into the queue of packets.
         */
        int packetGenerationState = -1;

        Random rand = new Random();
        double random = rand.nextDouble();

        // if the there is a packet generation
        if (random <= pArrival) {
            // if the queue is not full
            if (queueOfPackets.size() < packetListMaxSize) {
                // we add a packet to the queue
                queueOfPackets.add(new Packet());
                packetGenerationState = 1;
            } else {// if the queue is full
                // we have a packet loss
                packetGenerationState = 0;
            }
        }
        return packetGenerationState;
    }

    /**
     * This function tries to change the state of the Station according to a probability which is selected
     * according to each state and it is defined by R, N and B
     *
     * @param R is the percentage of slots with packet generation
     * @param N is the number of Stations that participate in the simulation
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
     * Acts according to the state of the State
     * if the node is in Active state, it generates a packet per time slot and then tries to change state
     * if the node is in Idle state, it tries to change state
     *
     * @param R is the percentage of slots with packet generation
     * @param N is the number of States that participate in the simulation
     * @return a int value which indicates if there was a non generation or a packet loss or a generation of a packet
     */
    public int actAccordingToState(double R, int N) {
        /*
         * if the value = -1, the station did not generate a packet.
         * else if the value = 0, the station generated a packet, but there was a packet loss.
         * else if the value = 1, the station generated a packet and stored it normally into the queue of packets.
         */
        int packetGenerationState;

        if (state == State.Active) {
            // if the queue is not full
            if (queueOfPackets.size() < packetListMaxSize) {
                // we add a packet to the queue
                queueOfPackets.add(new Packet());
                packetGenerationState = 1;
            } else {// if the queue is full
                // we have a packet loss
                packetGenerationState = 0;
            }
            tryToChangeState(R, N);
        } else {
            packetGenerationState = -1;
            tryToChangeState(R, N);
        }
        return packetGenerationState;
    }

    /**
     * we set the last known queue of packets when it tries to transmit
     */
    public void setLastKnownQueueOfPacketsSize() {
        this.lastKnownQueueOfPacketsSize = queueOfPackets.size();
    }

    /**
     * @return the last known queue of packets size
     */
    public int getLastKnownQueueOfPacketsSize() {
        return lastKnownQueueOfPacketsSize;
    }

    /**
     * we set the last known average delay time of packets in of queue when it tries to transmit
     */
    public void setLastKnownMaxDelayTimeOfPacketInQueue() {
        int maxDelayTime = 0;
        for (Packet packet : queueOfPackets) {
            if (maxDelayTime < packet.getDelayTime()) {
                maxDelayTime = packet.getDelayTime();
            }
        }
        lastKnownMaxDelayTimeOfPacketInQueue = maxDelayTime;
    }

    /**
     * @return the last known average delay time of packets in of queue
     */
    public int getLastKnownMaxDelayTimeOfPacketInQueue() {
        return lastKnownMaxDelayTimeOfPacketInQueue;
    }

    /**
     * increases the delay time of every packet of the Station by 1
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

    /**
     * a comparator which helps us to sort the stations according to their LastKnownQueueOfPacketsSize in descending order
     */
    public static Comparator<Station> LastKnownQueueOfPacketsSize = new Comparator<Station>() {
        @Override
        public int compare(Station s1, Station s2) {
            return s2.getLastKnownQueueOfPacketsSize() - s1.getLastKnownQueueOfPacketsSize();
        }
    };

    /**
     * a comparator which helps us to sort the stations according to their maxDelayTimeOfPacketInQueue in descending order
     */
    public static Comparator<Station> LastKnownMaxDelayTimeOfPacketInQueue = new Comparator<Station>() {
        @Override
        public int compare(Station s1, Station s2) {
            return s2.getLastKnownMaxDelayTimeOfPacketInQueue() - s1.getLastKnownMaxDelayTimeOfPacketInQueue();
        }
    };


}
