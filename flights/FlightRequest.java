package maman15.flights;

public class FlightRequest {

    public FlightRequest(int flightNumber, FlightRequestTypeEnum requestType) {
        m_flightNumber = flightNumber;
        m_requestType = requestType;
    }

    private int m_flightNumber;

    private FlightRequestTypeEnum m_requestType;

    public int getFlightNumber() {
        return m_flightNumber;
    }

    public FlightRequestTypeEnum getRequestType() {
        return m_requestType;
    }
}
