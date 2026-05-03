package classworks.classwork12.task1;

import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoMinerTask {

    public static boolean isValidNonce(long nonce) {
        long hash = nonce;
        for (int i = 0; i < 200; i++) {
            hash ^= (hash << 21);
            hash ^= (hash >>> 14);
            hash ^= (hash << 4);
        }
        return Math.abs(hash % 1000) == 0;
    }

    public long mineSingleThread(long maxNonce) {
        long counter = 0;
        for (int nonce = 1; nonce < maxNonce; nonce++) {
            if (isValidNonce(nonce)) {
                counter++;
            }
        }
        return counter;
    }

    public long mineMultiThread(long maxNonce, int threadsCount) throws InterruptedException {
        AtomicLong counter = new AtomicLong(0);
        Thread[] threads = new Thread[threadsCount];
        int minNonce = 1;

        long range = (maxNonce - minNonce) / threadsCount;

        for (int i = 0; i < threadsCount; i++) {
            final long start = (i == 0) ? minNonce: i * range;
            final long end = (i == threadsCount - 1) ? maxNonce : start + range;

            threads[i] = new Thread(() -> {
                for (long nonce = start; nonce < end; nonce++) {
                    if (isValidNonce(nonce)) {
                        counter.incrementAndGet();
                    }
                }
            });

            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        return counter.get();
    }


    @Test
    void testMiningPerformance() throws InterruptedException {
        long maxNonce = 10_000_000;
        int threads = 4;

        long startSingle = System.currentTimeMillis();
        long singleResult = mineSingleThread(maxNonce);
        long timeSingle = System.currentTimeMillis() - startSingle;

        long startMulti = System.currentTimeMillis();
        long multiResult = mineMultiThread(maxNonce, threads);
        long timeMulti = System.currentTimeMillis() - startMulti;

        System.out.println("Miner Single: " + timeSingle + " ms, Multi: " + timeMulti + " ms");

        assertEquals(singleResult, multiResult, "Количество валидных блоков не совпадает!");
        assertTrue(multiResult > 0, "Не найдено ни одного блока, алгоритм сломан!");
        assertTrue(timeMulti < timeSingle, "Многопоточный майнер должен работать быстрее!");
    }
}
