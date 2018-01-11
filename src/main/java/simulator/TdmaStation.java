package simulator;

import java.util.LinkedList;
import java.util.Random;

/**
 * @author Spiros
 */
public class TdmaStation {
    // a queue of waiting packets of the AtdmaStation
    private LinkedList<Packet> packetList;

    // the max size that the packets' queue can reach
    private final int packetListMaxSize;

    /**
     * trivial constructor
     *
     * @param packetListMaxSize of the TdmaStation
     */
    public TdmaStation(int packetListMaxSize) {
        this.packetListMaxSize = packetListMaxSize;
        this.packetList = new LinkedList<>();
    }

    /**
     * @return a boolean value if the queue Of Packets is empty
     */
    public boolean isEmpty() {
        return packetList.isEmpty();
    }

    /**
     * creates a packets using the given pArrival
     *
     * @param pArrival is the probability of the arrival of a packet at the TdmaStation
     */
    public boolean createPacket(double pArrival) {
        Random rand = new Random();
        double random = rand.nextDouble();

        if (packetList.size() < packetListMaxSize) {
            if (random <= pArrival) {
                // we add a packet to the list
                packetList.add(new Packet());
                return true;
            }
        }
        return false;
    }

    /**
     * increases the delay time of every packet of the TdmaStation by 1
     */
    public void increaseDelayTimeOPackets() {
        for (Packet packet : packetList) {
            packet.increaseDelay();
        }
    }

    /**
     * removes the first packet of the waiting queue
     */
    public void removeFirstPacket() {
        packetList.removeFirst();
    }

    /**
     * @return the delay time of the first packet of the queue
     */
    public int getDelayTimeOfFirstPacket() {
        return packetList.getFirst().getDelayTime();
    }
}
