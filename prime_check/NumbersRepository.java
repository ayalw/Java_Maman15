package maman15.prime_check;

import java.util.HashMap;
import java.util.Map;

public class NumbersRepository {

    private Map<Integer, PrimeCheckStatusEnum> m_numbers;

    public NumbersRepository(int range) {
        m_numbers = new HashMap<>();
        for (int i=1; i<=range; i++) {
            m_numbers.put(i, PrimeCheckStatusEnum.NOT_STARTED);
        }
    }

    public synchronized int getNumberToCheck(String workerName) {
        for (int num : m_numbers.keySet()) {
            if (m_numbers.get(num) == PrimeCheckStatusEnum.NOT_STARTED) {
                m_numbers.put(num, PrimeCheckStatusEnum.IN_PROGRESS);
                //System.out.println("Assigning number "+ num+ " to worker "+workerName);
                return num;
            }
        }
        return -1;
    }

    public synchronized void updateWorkDone(int number, boolean isPrime, String workerName) {
        PrimeCheckStatusEnum result = isPrime ?
                PrimeCheckStatusEnum.FINISHED_RESULT_PRIME
                :
                PrimeCheckStatusEnum.FINISHED_RESULT_NOT_PRIME;
        //System.out.println("got result from worker " + workerName + " for number " + number + ": " + result);
        m_numbers.put(number, result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int num : m_numbers.keySet()) {
            sb.append("[");
            sb.append(num);
            sb.append("]");
            sb.append("[");
            sb.append(m_numbers.get(num));
            sb.append("]");
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
