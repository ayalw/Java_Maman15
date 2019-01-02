package maman15.flights;

public class Flight implements Runnable {

    private int m_flightNumber;
    private Airport m_departureAirport;
    private Airport m_landingAirport;

    public Flight(int flightNumber, Airport departureAirport, Airport landingAirport) {
        m_flightNumber = flightNumber;
        m_departureAirport = departureAirport;
        m_landingAirport = landingAirport;
    }

    @Override
    public void run() {

        // Request take off - blocking!
        print("Requesting permission for take off from " + m_departureAirport.getName());
        int departureRunway = m_departureAirport.depart(m_flightNumber);
        print("Got permission to take off from runway " + departureRunway + ", starting take off!");

        // Simulate take off
        sleepShort();

        // Finished take off, notify airport that runway is now free
        print("Finished take off and continuing to destination. No need for runway " + departureRunway + " anymore!");
        m_departureAirport.freeRunway(m_flightNumber, departureRunway);

        // Simulate actual flight
        sleepLong();

        // Request landing - blocking!
        print("Requesting permission to land at " + m_landingAirport.getName());
        int landingRunway = m_landingAirport.land(m_flightNumber);
        print("Got permission to land at runway " + landingRunway + ", starting descent!");

        // Simulate landing
        sleepShort();

        // Finished landing, notify airport that runway is now free
        print("Finished landing at destination! No need for runway " + landingRunway + " anymore!");
        m_landingAirport.freeRunway(m_flightNumber, landingRunway);
        print("*** FINISHED WORK ***");
    }

    private void sleepShort() {
        try {
            Thread.sleep((long)(Math.random() * 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleepLong() {
        try {
            Thread.sleep((long)(Math.random() * 20000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void print(String str) {
        System.out.println("["+m_flightNumber +"|"+m_departureAirport.getName()+"->"+m_landingAirport.getName()+"] " + str);
    }
}
