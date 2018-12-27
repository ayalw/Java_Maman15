package maman15.prime_check;

public class Utils {
    //checks whether an int is prime or not.
    public static boolean isPrime(int n) {
        for(int i=2;i<n;i++) {
            if(n%i==0)
                return false;
        }
        return true;
    }
}
