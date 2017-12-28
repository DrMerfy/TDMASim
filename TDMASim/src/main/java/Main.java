import grapher.App;
import simulator.TdmaSimulator;

public class Main {
    public static void main(String[] args) {
        App.Launch(args);
        /*
        for (int i = 0; i < tdma.getDelayTimes().size(); i++) {
            System.out.println("\n Delay time of " + (i+1) + "nd sent package = " + tdma.getDelayTimes().get(i));
        }
        //*/
    }
}
