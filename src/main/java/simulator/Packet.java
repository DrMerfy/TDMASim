package simulator;


/**
 * @author Spiros
 */
public class Packet {
    // the data of the package
    private final double packageData;

    // the time slots that the packet is waiting in the queue of waiting packets of the Station
    private int packageDelayTime;

    /**
     * trivial constructor of a packet which contains a random value between 0 and 1000000;
     */
    Packet() {
        //
        double ran = Math.random();
        this.packageData = (1000000) * ran;

        packageDelayTime = 0;
    }

    /**
     * @return the data that are include in the package
     */
    public double getPackageData() {
        return packageData;
    }

    /**
     * Increases the delay time of the package by 1
     */
    public void increaseDelay() {
        packageDelayTime++;
    }

    /**
     * @return the delay time of the package
     */
    public int getDelayTime() {
        return packageDelayTime;
    }
}