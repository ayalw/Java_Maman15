package maman15.flights;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Airport {//implements Runnable {

    private String m_name;
    private int m_numOfRunways;
    //private Map<Integer, Integer> m_runwaysToFlights;
    private Map<Integer, Integer> m_flightsToRunways;
    private BlockingQueue<FlightRequest> m_pendingFlights;

    public Airport(String name, int numOfRunways) {
        m_name = name;
        m_numOfRunways = numOfRunways;
        // = new HashMap<>();
        m_flightsToRunways = new HashMap<>();
//        for (int i=1; i<=m_numOfRunways; i++) {
//            m_runwaysToFlights.put(i, -1);
//        }
        m_pendingFlights = new LinkedBlockingDeque<>();
    }

    public int depart(int flightNumber) {
        m_pendingFlights.add(new FlightRequest(flightNumber, FlightRequestTypeEnum.DEPART));
        if (!m_flightsToRunways.containsKey(flightNumber)) { //This is first time flight number appears
            m_flightsToRunways.put(flightNumber, -1);
        }
        try {
            while (m_flightsToRunways.get(flightNumber) == -1) {
                wait();
            }
            return m_flightsToRunways.get(flightNumber);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int land(int flightNumber) {
        m_pendingFlights.add(new FlightRequest(flightNumber, FlightRequestTypeEnum.LAND));
        if (!m_flightsToRunways.containsKey(flightNumber)) { //This is first time flight number appears
            m_flightsToRunways.put(flightNumber, -1);
        }
        try {
            while (m_flightsToRunways.get(flightNumber) == -1) {
                wait();
            }
            return m_flightsToRunways.get(flightNumber);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void freeRunway(int flightNumber, int runwayNumber) {
        try {
            FlightRequest request = m_pendingFlights.take(); //Blocking!
            //m_runwaysToFlights.put(runwayNumber, request.getFlightNumber());
            m_flightsToRunways.put(request.getFlightNumber(), runwayNumber);
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void run() {
//        while (true) {
//            try {
//                FlightRequest request = m_pendingFlights.take();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
