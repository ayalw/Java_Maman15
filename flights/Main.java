package maman15.flights;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Maman15 Airport!");
        Airport telAviv = new Airport("TLV", 3);
        Airport jfk = new Airport("JFK", 3);
        int numOfFlights = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numOfFlights);
        Airport origin;
        Airport destination;
        for (int i=0; i<numOfFlights; i++) {
            if (new Random().nextBoolean()) {
                origin = telAviv;
                destination = jfk;
            }
            else {
                origin = jfk;
                destination = telAviv;
            }
            Runnable worker = new Flight(i * 100, origin, destination);
            executor.execute(worker);
        }
    }
}
