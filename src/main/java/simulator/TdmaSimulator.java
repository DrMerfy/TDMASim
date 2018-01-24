package simulator;


import java.util.ArrayList;

/**
 * @author Spiros
 */
public class TdmaSimulator {
    // variables that are given by the user
    private int numberOfStations;
    private int maxSizeOfPacketsList;
    private int numberOfCircles;

    // an ArrayList which contains the Stations for every Simulation
    private ArrayList<Station> stations;

    // variable which is calculated from the user given variables
    private final int maxTime;

    // variables which are calculated after the end of the Simulation
    private int packetsTransmitted;
    private double averageDelayTimeSlots;

    /**
     * The constructor of the Simulator
     *
     * @param numberOfStations     that we assign for the simulation
     * @param maxSizeOfPacketsList that we assign for the simulation
     * @param numberOfCircles      that we assign for the simulation
     */
    public TdmaSimulator(int numberOfStations, int maxSizeOfPacketsList, int numberOfCircles) {
        this.numberOfStations = numberOfStations;
        this.maxSizeOfPacketsList = maxSizeOfPacketsList;
        this.numberOfCircles = numberOfCircles;

        this.maxTime = numberOfStations * numberOfCircles;

        this.stations = new ArrayList<>();
    }

    /**
     * The function that starts a noiseless, based on a uniform probability, Simulation of the TDMA Mac protocol
     *
     * @param pArrival is the probability of the arrival of a packet at every Station
     */
    public void runTdmaSimulation(double pArrival) {
        // start of initializations
        packetsTransmitted = 0;

        stations.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackets = new ArrayList<>();

        for (int i = 0; i < maxSizeOfPacketsList; i++) {
            stations.add(new Station(maxSizeOfPacketsList));
        }

        int time = 0;
        // end of initializations

        while (time < maxTime) {
            /* Increase the delay time of each packet of each Station by 1 and
             * Create a packet for each Station with probability equal to pArrival
             */
            for (Station station : stations) {
                station.increaseDelayTimeOPackets();
                station.createPacket(pArrival);
            }

            // Find which station is going to transmit
            int idOfTransmittingStation = time % stations.size();

            // If the selected station has something to transmit, it transmits the first packet of the Station.
            if (!stations.get(idOfTransmittingStation).isEmpty()) {
                delayTimeOfTransmittedPackets.add(stations.get(idOfTransmittingStation).getDelayTimeOfFirstPacket());

                transmit(stations.get(idOfTransmittingStation));
            }
            // Go to the next time slot.
            time++;
        }

        // calculation of the average delay time-slots
        averageDelayTimeSlots = calculationOfAverageDelayTimeSlots(delayTimeOfTransmittedPackets);
    }

    /**
     * The function that starts a noiseless bursty simulation of the TDMA Mac protocol
     * According to the section 3 of the paper IEEE transactions on communications, Vol 51, No 4 April 2003
     * Georgios I. Papadimitriou, Senior Member. IEEE, and Andreas S. Pomportsis
     *
     * @param R                is the percentage of slots with packet generation
     * @param meanBurstLengths is an array that includes the mean burst length of every Station
     */
    public void runBurstyTdmaSimulator(double R, ArrayList<Integer> meanBurstLengths) {
        // start of initializations
        packetsTransmitted = 0;

        stations.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackets = new ArrayList<>();

        for (int i = 0; i < maxSizeOfPacketsList; i++) {
            stations.add(new Station(maxSizeOfPacketsList, meanBurstLengths.get(i)));
        }

        int time = 0;
        // end of initializations

        while (time < maxTime) {
            /* Increase the delay time of each packet of each Station by 1 and
             * for every Station act according to it's State
             */
            for (Station station : stations) {
                station.increaseDelayTimeOPackets();
                station.actAccordingToState(R, stations.size());
            }

            // Find which station is going to transmit
            int idOfTransmittingStation = time % stations.size();

            // If the selected station has something to transmit, it transmits the first packet of the station.
            if (!stations.get(idOfTransmittingStation).isEmpty()) {
                delayTimeOfTransmittedPackets.add(stations.get(idOfTransmittingStation).getDelayTimeOfFirstPacket());

                transmit(stations.get(idOfTransmittingStation));
            }
            // Go to the next time slot.
            time++;
        }

        // calculation of the average delay time-slots
        averageDelayTimeSlots = calculationOfAverageDelayTimeSlots(delayTimeOfTransmittedPackets);
    }

    /**
     * @param delayTimeOfTransmittedPackets an ArrayList that contains the amount of time slots that every transmitted
     *                                      packet was waiting until it was finally transmitted
     * @return the average delay time of every transmitted packet
     */
    private double calculationOfAverageDelayTimeSlots(ArrayList<Integer> delayTimeOfTransmittedPackets) {
        int sumDelays = 0;
        for (Integer delay : delayTimeOfTransmittedPackets) {
            sumDelays += delay;
        }
        return sumDelays / (packetsTransmitted * 1.0);
    }

    /**
     * This function removes the first packet from the Station that has the right to transmit (if there is a
     * packet to transmit), increases the total amount of packets that has been transmitted and completes
     * the transmission of the packet
     *
     * @param station that has the right to transmit
     */
    private void transmit(Station station) {
        station.removeFirstPacket();
        packetsTransmitted++;
    }


    /**
     * @return the Throughput of the Simulation
     */
    public double getThroughput() {
        return packetsTransmitted / (numberOfStations * numberOfCircles * 1.0);
    }

    /**
     * @return the Avarage Delay time of all the transmitted packets
     */
    public double getAverageDelayTimeSlots() {
        return averageDelayTimeSlots;
    }

    /**
     * @return the number of stations that we set in order to run the simulation
     */
    public int getNumberOfStations() {
        return numberOfStations;
    }

    /**
     * @return the max size of the Packet List that we set in order to run the simulation
     */
    public int getMaxSizeOfPacketsList() {
        return maxSizeOfPacketsList;
    }

    /**
     * @return the number of circles that we set in order the run the Simulation
     */
    public int getNumberOfCircles() {
        return numberOfCircles;
    }
}

