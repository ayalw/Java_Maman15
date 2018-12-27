package maman15.prime_check;

public class PrimeCheckWorker implements Runnable {

    private String m_name;
    private NumbersRepository m_repository;

    public PrimeCheckWorker(NumbersRepository repository, String name) {
        m_name = name;
        m_repository = repository;
    }

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
