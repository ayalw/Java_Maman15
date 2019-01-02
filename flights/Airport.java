package maman15.flights;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Airport {

    private String m_name;
    private int m_numOfRunways;
    private Map<Integer, Integer> m_flightsToRunways;
    private Map<Integer, Integer> m_runwaysToFlights;
    private BlockingQueue<FlightRequest> m_pendingFlights;

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

    public synchronized boolean areAllRunwaysFree() {
        boolean foundBusyRunway = false;
        for (int runway : m_runwaysToFlights.keySet()) {
            if (m_runwaysToFlights.get(runway) != -1) {
                foundBusyRunway = true;
            }
        }
        return !foundBusyRunway;
    }

}
