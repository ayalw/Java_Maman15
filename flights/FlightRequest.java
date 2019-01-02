package maman15.flights;

public class FlightRequest {

    /**
     * This class is to be used in FIFO queues to denote the order of requests.
     * @param flightNumber
     * @param requestType
     */
    public FlightRequest(int flightNumber, FlightRequestTypeEnum requestType) {
        m_flightNumber = flightNumber;
        m_requestType = requestType;
    }

    private int m_flightNumber;

    private FlightRequestTypeEnum m_requestType;

    public int getFlightNumber() {
        return m_flightNumber;
    }

}
