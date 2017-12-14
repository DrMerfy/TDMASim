package simulator;

/**
 *
 * @author spiros
 */
public class Package {
    private final double packageData;
    private int packagetransmittedDelayTime;
    
    Package() {
        // we create a trivial package which contains a random value between 0 and 1000000;
        double ran = Math.random();
        this.packageData = (1000000)*ran;
        
        packagetransmittedDelayTime = 0;
    }
    
    public double getPackageData() {
        return packageData;
    }
    
    public void increaseDelay() {
        packagetransmittedDelayTime++;
    }
    
    public int getDelayTime() {
        return packagetransmittedDelayTime;
    }
}
