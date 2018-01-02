package simulator;

/**
 * @author spiros
 */
public class Package {
    // the data of the package
    private final double packageData;

    // the time that the package is waiting int the node queue
    private int packageDelayTime;

    /**
     * trivial constructor
     */
    Package() {
        // we create a trivial package which contains a random value between 0 and 1000000;
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