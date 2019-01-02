package maman15.flights;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This class represents an airport.
 * Its public methods can be called by multiple threads.
 */
public class Airport {

    /**
     * Airport name
     */
    private String m_name;

    /**
     * Number of runways to allocate, when they are all occupied, a queue will be used for FIFO allocation.
     */
    private int m_numOfRunways;

    /**
     * Keeps track of flights which are active in any runway. -1 means this flight is not using any runway.
     */
    private Map<Integer, Integer> m_flightsToRunways;

    /**
     * Keeps track of the runways. -1 means free.
     */
    private Map<Integer, Integer> m_runwaysToFlights;

    /**
     * FIFO queue for flights which requested a runway when runways are occupied.
     */
    private BlockingQueue<FlightRequest> m_pendingFlights;

    /**
     * Constructor
     * @param name
     * @param numOfRunways
     */
    public Airport(String name, int numOfRunways) {
        m_name = name;
        m_numOfRunways = numOfRunways;
        m_flightsToRunways = new HashMap<>();
        m_runwaysToFlights = new HashMap<>();
        m_pendingFlights = new LinkedBlockingDeque<>();
        for (int i=1; i<m_numOfRunways; i++) {
            m_runwaysToFlights.put(i, -1);
        }
        print("Created with " + m_numOfRunways + " runways.");
    }

    public String getName() {
        return m_name;
    }

    /**
     * This method is to be called by a Flight object asking to use a runway for take off.
     * @param flightNumber
     * @return runway number
     */
    public synchronized int depart(int flightNumber) {
        print("Got request to DEPART from flight number " + flightNumber);

        // Check if there is a free runway, if found, return it
        for (int runway : m_runwaysToFlights.keySet()) {
            if (m_runwaysToFlights.get(runway) == -1) {
                    print("Runway " + runway + " is free, allocating to flight " + flightNumber);
                    m_runwaysToFlights.put(runway, flightNumber);
                    m_flightsToRunways.put(flightNumber, runway);
                    return runway;
            }
        }

        // No free runway, we need to add this flight to queue and wait for a runway to be free.
        m_pendingFlights.add(new FlightRequest(flightNumber, FlightRequestTypeEnum.DEPART));

        if (!m_flightsToRunways.containsKey(flightNumber)) { //This is first time flight number appears
            m_flightsToRunways.put(flightNumber, -1);
        }
        try {
            while (m_flightsToRunways.get(flightNumber) == -1) {
                wait();
            }

            // We get here when a runway has been cleared and it's this flight's turn.
            return m_flightsToRunways.get(flightNumber);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * This method is to be called by a Flight object asking to use a runway for landing.
     * @param flightNumber
     * @return runway number
     */
    public synchronized int land(int flightNumber) {
        print("Got request to LAND from flight number " + flightNumber);

        // Check if there is a free runway, if found, return it
        for (int runway : m_runwaysToFlights.keySet()) {
            if (m_runwaysToFlights.get(runway) == -1) {
                    m_runwaysToFlights.put(runway, flightNumber);
                    m_flightsToRunways.put(flightNumber, runway);
                    return runway;
            }
        }

        // No free runway, we need to add this flight to queue and wait for a runway to be free.
        m_pendingFlights.add(new FlightRequest(flightNumber, FlightRequestTypeEnum.LAND));

        if (!m_flightsToRunways.containsKey(flightNumber)) { //This is first time flight number appears
            m_flightsToRunways.put(flightNumber, -1);
        }
        try {
            while (m_flightsToRunways.get(flightNumber) == -1) {
                wait();
            }

            // We get here when a runway has been cleared and it's this flight's turn.
            return m_flightsToRunways.get(flightNumber);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * This method is to be called by a Flight object asking to clear a runway after take off or landing.
     * @param flightNumber
     */
    public synchronized void freeRunway(int flightNumber, int runwayNumber) {
        print("Flight " + flightNumber + " has reported that runway " + runwayNumber + " is now free.");
        m_runwaysToFlights.put(runwayNumber, -1);
        m_flightsToRunways.put(flightNumber, -1);
        try {
            if (m_pendingFlights.isEmpty()) {
                print("No pending flights!");
                return;
            }
            FlightRequest request = m_pendingFlights.take(); //Blocking!
            print("Assigning runway " + runwayNumber + " to flight " + request.getFlightNumber());
            m_flightsToRunways.put(request.getFlightNumber(), runwayNumber);
            m_runwaysToFlights.put(runwayNumber, request.getFlightNumber());
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void print(String str) {
        System.out.println("[" + m_name + "] " + str);
    }

}
