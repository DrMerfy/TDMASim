package simulator;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static simulator.Station.LastKnownMaxDelayTimeOfPacketInQueue;
import static simulator.Station.LastKnownQueueOfPacketsSize;

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
    private int totalTimeSlots;

    // a counter of the transmitted packages
    private int packetsTransmitted;

    // a counter of the generated packets
    private int generatedPackets;

    // a counter of the lost packets because the ready queue of a station was full
    private int packetsLost;

    // the average delay Time slots of a simulation
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

        this.stations = new ArrayList<>();
    }

    /**
     * The function that starts a noiseless  based on a uniform probability traffic simulation of the TDMA Mac protocol.
     *
     * @param pArrival is the probability of the arrival of a packet at every Station
     */
    public void runUniformTdmaSimulation(double pArrival) {
        // start of initializations
        packetsTransmitted = 0;
        generatedPackets = 0;
        packetsLost = 0;
        stations.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackets = new ArrayList<>();

        //  the stations
        for (int i = 0; i < numberOfStations; i++) {
            stations.add(new Station(i, maxSizeOfPacketsList));
        }

        int time = 0;
        totalTimeSlots = numberOfStations * numberOfCircles;
        // end of initializations

        while (time < totalTimeSlots) {
            //  For every station
            for (Station station : stations) {
                //Increase the delay time of each packet of each Station by 1
                station.increaseDelayTimeOPackets();

                /* create a packet for each Station with probability equal to pArrival, get the packet generations state
                 * and try to increase the amount of the lost packets
                 */
                tryToIncreaseLostPackets(station.createPacket(pArrival));
            }

            // Find which station is going to transmit
            int idOfTransmittingStation = time % stations.size();

            // If the selected station has something to transmit, it transmits the first packet of the Station.
            if (stations.get(idOfTransmittingStation).getQueueOfPacketsSize() > 0) {
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
     * The function that starts a noiseless, with bursty traffic, simulation of the TDMA mac protocol.
     * The bursty traffic is based on the section 3 of the paper IEEE transactions on communications, Vol 51,
     * No 4 April 2003 Georgios I. Papadimitriou, Senior Member. IEEE, and Andreas S. Pomportsis
     *
     * @param R                is the percentage of slots with packet generation
     * @param meanBurstLengths is an array that includes the mean burst length of every Station
     */
    public void runBurstyTdmaSimulation(double R, ArrayList<Integer> meanBurstLengths) {
        // start of initializations
        packetsTransmitted = 0;
        generatedPackets = 0;
        packetsLost = 0;
        stations.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackets = new ArrayList<>();

        //  the stations
        for (int i = 0; i < numberOfStations; i++) {
            stations.add(new Station(i, maxSizeOfPacketsList, meanBurstLengths.get(i)));
        }

        int time = 0;
        totalTimeSlots = numberOfStations * numberOfCircles;
        // end of initializations

        while (time < totalTimeSlots) {
            //  For every station
            for (Station station : stations) {
                //Increase the delay time of each packet of each Station by 1
                station.increaseDelayTimeOPackets();

                /* for every Station act according to it's State, get the packet generations state
                 * and try to increase the amount of the lost packets
                 */
                tryToIncreaseLostPackets(station.actAccordingToState(R, stations.size()));
            }

            // Find which station is going to transmit
            int idOfTransmittingStation = time % stations.size();

            // If the selected station has something to transmit, it transmits the first packet of the Station.
            if (stations.get(idOfTransmittingStation).getQueueOfPacketsSize() > 0) {
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
     * The function that starts a noiseless, with bursty traffic, simulation of the Extended-header-TDMA mac protocol
     * which is like the standard TDMA just with the addition of either the size of the queue of a station or the packet
     * the with the maximum delay-time, into the header of a packet which helps in ordering the stations according
     * their queue size or max packet delay time.
     * <p>
     * The bursty traffic is based on the section 3 of the paper IEEE transactions on communications, Vol 51,
     * No 4 April 2003 Georgios I. Papadimitriou, Senior Member. IEEE, and Andreas S. Pomportsis
     *
     * @param R                is the percentage of slots with packet generation
     * @param meanBurstLengths is an array that includes the mean burst length of every Station
     * @param comparator       is the comparator which is is used to order
     */
    public void runBurstyEHTdmaSimulation(double R, ArrayList<Integer> meanBurstLengths, Comparator<Station> comparator) {
        // start of initializations
        packetsTransmitted = 0;
        generatedPackets = 0;
        packetsLost = 0;
        stations.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackets = new ArrayList<>();

        // the stations
        for (int i = 0; i < numberOfStations; i++) {
            stations.add(new Station(i, maxSizeOfPacketsList, meanBurstLengths.get(i)));
        }

        int time = 0;
        totalTimeSlots = numberOfStations * numberOfCircles;
        // end of initializations

        while (time < totalTimeSlots) {
            /* every frame (every # slots which is equal to the amount of stations) sort the stations in
             * in descending order according to their lastKnownQueueOfPacketsSize
             */
            if (time % stations.size() == 0) {
                stations.sort(Collections.reverseOrder(comparator));
            }

            //  For every station
            for (Station station : stations) {
                //Increase the delay time of each packet of each Station by 1
                station.increaseDelayTimeOPackets();

                /* for every Station act according to it's State, get the packet generations state
                 * and try to increase the amount of the lost packets
                 */
                tryToIncreaseLostPackets(station.actAccordingToState(R, stations.size()));
            }

            // Find which station is going to transmit
            int idOfTransmittingStation = time % stations.size();

            // If the selected station has something to transmit, it transmits the first packet of the Station.
            if (stations.get(idOfTransmittingStation).getQueueOfPacketsSize() > 0) {
                delayTimeOfTransmittedPackets.add(stations.get(idOfTransmittingStation).getDelayTimeOfFirstPacket());

                transmit(stations.get(idOfTransmittingStation));
            }
            // we set the lastKnownQueueOfPacketsSize of the station
            stations.get(idOfTransmittingStation).setLastKnownQueueOfPacketsSize();
            stations.get(idOfTransmittingStation).setLastKnownMaxDelayTimeOfPacketInQueue();
            // Go to the next time slot.
            time++;
        }

        // calculation of the average delay time-slots
        averageDelayTimeSlots = calculationOfAverageDelayTimeSlots(delayTimeOfTransmittedPackets);
    }

    /**
     * The function that starts a noiseless, with bursty traffic, simulation of the Active-Stations-Ordering-Every-N-Circles
     * TDMA mac protocol, which differs from the standard TDMA. The simulator, every n circles wastes a time-slot to ask
     * the stations their queue size. If they have something to transmit, they are gonna be included into the active
     * stations set, and those will be ordered according to the size of their queue or the packet with the maximum delay-time.
     * if they don't they will be asked again after n active-stations circles.
     * <p>
     * The bursty traffic is based on the section 3 of the paper IEEE transactions on communications, Vol 51,
     * No 4 April 2003 Georgios I. Papadimitriou, Senior Member. IEEE, and Andreas S. Pomportsis
     *
     * @param R                          is the percentage of slots with packet generation
     * @param meanBurstLengths           is an array that includes the mean burst length of every Station
     * @param numberOfCirclesForOrdering is the number of circles that will finish in order to ask for the appropriate info based on the comparator
     * @param comparator                 is the comparator which is is used to order
     */
    public void runBurstyASOENCTdmaSimulation(double R, ArrayList<Integer> meanBurstLengths, int numberOfCirclesForOrdering, Comparator<Station> comparator) {
        // start of initializations
        packetsTransmitted = 0;
        generatedPackets = 0;
        packetsLost = 0;
        stations.clear();
        ArrayList<Integer> delayTimeOfTransmittedPackets = new ArrayList<>();

        //  the stations
        for (int i = 0; i < numberOfStations; i++) {
            stations.add(new Station(i, maxSizeOfPacketsList, meanBurstLengths.get(i)));
        }

        //  the active stations
        ArrayList<Station> activeStations = new ArrayList<>();

        int circles = 0;
        int time = 0;
        totalTimeSlots = numberOfStations * numberOfCircles;
        // end of initializations

        while (time < totalTimeSlots) {
            for (int timeSlots = 0; timeSlots < 1 + numberOfCirclesForOrdering * activeStations.size() &&
                    time < totalTimeSlots; timeSlots++, time++) {

                //  For every station
                for (Station station : stations) {
                    //Increase the delay time of each packet of each Station by 1
                    station.increaseDelayTimeOPackets();

                    /* for every Station act according to it's State, get the packet generations state
                     * and try to increase the amount of the lost packets
                     */
                    tryToIncreaseLostPackets(station.actAccordingToState(R, stations.size()));
                }

                //  get the active stations and order them based on the given comparator
                if (timeSlots == 0) {
                    activeStations.clear();
                    for (Station station : stations) {
                        if (station.getQueueOfPacketsSize() > 0) {
                            activeStations.add(station);
                        }
                    }
                    activeStations.sort(Collections.reverseOrder(comparator));
                }

                // Go to the next circle.
                if (timeSlots > 0) {
                    if ((timeSlots - 1) % activeStations.size() == activeStations.size() - 1) {
                        circles++;
                    }

                    // Find which station is going to transmit
                    int idOfTransmittingStation = (timeSlots - 1) % activeStations.size();

                    // If the selected station has something to transmit, it transmits the first packet of the Station.
                    if (activeStations.get(idOfTransmittingStation).getQueueOfPacketsSize() > 0) {
                        delayTimeOfTransmittedPackets.add(activeStations.get(idOfTransmittingStation).getDelayTimeOfFirstPacket());

                        transmit(activeStations.get(idOfTransmittingStation));
                    }
                }
            }
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
     * The station acts according to the packetGenerationState
     * <p>
     * if packetGenerationState = -1, the station did not generate a packet.
     * else if packetGenerationState = 0, the station generated a packet, but there was a packet loss.
     * else if packetGenerationState = 1, the station generated a packet and stored it into the queue of packets.
     *
     * @param packetGenerationState is the packet generation state
     */
    private void tryToIncreaseLostPackets(int packetGenerationState) {
        // if there was a packet loss, increase the amount of lost packets and the amount of the generated ones
        if (packetGenerationState == 0) {
            packetsLost++;
            generatedPackets++;
        } else if (packetGenerationState == 1) { // else just increase the amount of the generated packets
            generatedPackets++;
        }

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
        return packetsTransmitted / (totalTimeSlots * 1.0);
    }

    /**
     * @return the Packet Loss Ratio of the Simulation
     */
    public double getPacketLossRatio() {
        return packetsLost / (generatedPackets * 1.0);
    }

    /**
     * @return the Avarage Delay time of all the transmitted packets
     */
    public double getAverageDelayTimeSlots() {
        return averageDelayTimeSlots;
    }

    /**
     * @return the total time slots of the simulation
     */
    public int getTotalTimeSlots() {
        return totalTimeSlots;
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

