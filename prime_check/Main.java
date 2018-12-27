package maman15.prime_check;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Maman 15.");
        System.out.println("Please insert a value for 'm' (the number up to which you want to check the prime numbers)");
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        System.out.println("Please insert a value for 'n' (the number of threads)");
        int n = scanner.nextInt();
        System.out.println("Using values: m="+m+", n="+n);
        NumbersRepository repository = new NumbersRepository(m);
        ExecutorService executor = Executors.newFixedThreadPool(n);
        for (int i=0; i<n; i++) {
            String name = "Minion_" + i;
            Runnable worker = new PrimeCheckWorker(repository, name);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads! Printing repository:");
        System.out.println(repository);


    }
}
