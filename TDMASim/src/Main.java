import grapher.App;
import simulator.TdmaSimulator;

public class Main {
    public static void main(String[] args) {
        //TODO Launch simulator here
        TdmaSimulator tdma = new TdmaSimulator (8, 12, 1000000);
        tdma.runSimulator(0.18);
    }
}
