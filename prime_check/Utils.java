package maman15.prime_check;

/**
 * Calculation utilities.
 */
public class Utils {

    /**
     * Prime check calculation (can be optimized for better performance, not in scope of this maman)
     * @param n
     * @return
     */
    public static boolean isPrime(int n) {
        for(int i=2;i<n;i++) {
            if(n%i==0)
                return false;
        }
        return true;
    }
}
