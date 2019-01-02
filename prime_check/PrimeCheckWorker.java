package maman15.prime_check;

/**
 * Represents the worker thread.
 */
public class PrimeCheckWorker implements Runnable {

    private String m_name;
    private NumbersRepository m_repository;

    /**
     * Constructor
     * @param repository
     * @param name
     */
    public PrimeCheckWorker(NumbersRepository repository, String name) {
        m_name = name;
        m_repository = repository;
    }

    /**
     * Work method.
     * The thread will ask for numbers to check until it receives -1 which mean 'no more numbers to check'.
     */
    @Override
    public void run() {
        int num = m_repository.getNumberToCheck(m_name);
        while (num != -1) {
            boolean isPrime = Utils.isPrime(num);
            m_repository.updateWorkDone(num, isPrime, m_name);
            num = m_repository.getNumberToCheck(m_name);
        }
    }
}
