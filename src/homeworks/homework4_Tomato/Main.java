import java.util.*;

public class Main {
    static long N, X, K;
    static long[] t;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new java.io.File("input.txt"));

        N = sc.nextLong();
        X = sc.nextLong();
        K = sc.nextLong();

        t = new long[(int) N];
        for (int i = 0; i < N; i++) {
            t[i] = sc.nextLong();
        }

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        long sortStart = System.nanoTime();
        Arrays.sort(t); // O(n*log n) M(log n), думаю, предположительно используется quicksort, в документации вроде Dual-Pivot Quicksort
        long sortTime = (System.nanoTime() - sortStart) / 1000000;

        long left = 0;
        long right = (long) 2e18;

        long binaryStart = System.nanoTime();
        while (left < right) { // O(log n) M(1) | ищем такое время, чтобы оно удовлетворяло услови, что к его моменту прозвенело больше или равно K будильников
            long mid = (left + right) / 2;

            if (count(mid) >= K) { // count() O(n) M(1) 
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        long binaryTime = (System.nanoTime() - binaryStart) / 1000000;
        long memoryAfterBinary = runtime.totalMemory() - runtime.freeMemory();

        System.out.println(left + 1);
        // итоговая оценка: O(n*log n) M(log n), без учета входного массива
        // но сделал в Gen.java генератор файла с нагрузкой, можешь запустить и тестить
               
        System.out.printf("время: всего=%d мс%n", sortTime + binaryTime);
        System.out.printf("память: %d кб", (memoryAfterBinary - memoryBefore) / 1024);
    }

    static long count(long T) { // O(n) M(1) | считает сколько будильников прозвонило к времени T
        long cnt = 0;

        for (int i = 0; i < N; i++) {
            if (T >= t[i]) {
                cnt += (T - t[i]) / X + 1;
                if (cnt >= K) return cnt; // чуть оптимизируем
            }
        }

        return cnt;
    }
}