package homeworks.homework7__Multithreading.task2;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.jupiter.api.Assertions.*;

public class LogTask {
    private static final String FILE_NAME = "server_logs.txt";
    private static final String POISON_PILL = "EOF";

    private boolean isError(String logLine) {
        int hash = 7;

        for (int i = 0; i < 100; i++) {
            hash = hash * 31;

            for (int j = 0; j < 10; j++) {
                hash ^= (hash << 5);
                hash ^= (hash >> 3);
                hash ^= (hash << 7);
            }
        }

        String[] parts = logLine.split(" ");
        for (String part : parts) {
            if (part.equals("ERROR")) return true;
        }
        return false;
    }

    public void generateLogFile(int linesCount) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 1; i <= linesCount; i++) {
                if (i % 100 == 0) bw.write("2023-10-10 12:00:00 [Thread-" + i + "] ERROR Something failed\n");
                else bw.write("2023-10-10 12:00:00 [Thread-" + i + "] INFO Everything is fine\n");
            }
        }
    }

    static class BoundedQueue {
        private final Queue<String> queue = new LinkedList<>();
        private final int capacity = 1000;

        public synchronized void put(String s) throws InterruptedException {
            while (queue.size() >= capacity) {
                wait();
            }

            queue.add(s);
            notifyAll();
        }

        public synchronized String take() throws InterruptedException {
            while (queue.isEmpty()) {
                wait();
            }

            String value = queue.poll();
            notifyAll();
            return value;
        }
    }

    public long processSingleThread() throws IOException {
        long count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (isError(line)) {
                    count++;
                }
            }
        }

        return count;
    }

    public long processMultiThread(int consumerCount) throws Exception {
        BoundedQueue queue = new BoundedQueue();
        AtomicLong counter = new AtomicLong(0);

        Thread producer = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;

                while ((line = br.readLine()) != null) {
                    queue.put(line);
                }

                for (int i = 0; i < consumerCount; i++) {
                    queue.put(POISON_PILL);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Thread[] consumers = new Thread[consumerCount];

        for (int i = 0; i < consumerCount; i++) {
            consumers[i] = new Thread(() -> {
                try {
                    while (true) {
                        String line = queue.take();

                        if (POISON_PILL.equals(line)) {
                            break;
                        }

                        if (isError(line)) {
                            counter.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        producer.start();

        for (Thread consumer : consumers) {
            consumer.start();
        }

        producer.join();

        for (Thread consumer : consumers) {
            consumer.join();
        }

        return counter.get();
    }

    @Test
    void testLogProcessing() throws Exception {
        int totalLines = 2_000_000;
        generateLogFile(totalLines);
        int expectedErrors = totalLines / 100;

        long startSingle = System.currentTimeMillis();
        long singleResult = processSingleThread();
        long timeSingle = System.currentTimeMillis() - startSingle;

        long startMulti = System.currentTimeMillis();
        long multiResult = processMultiThread(3);
        long timeMulti = System.currentTimeMillis() - startMulti;

        System.out.println("IO Single: " + timeSingle + " ms, IO Multi: " + timeMulti + " ms");

        new File(FILE_NAME).delete();

        assertEquals(expectedErrors, singleResult, "Однопоточный посчитал неверно!");
        assertEquals(singleResult, multiResult, "Многопоточный потерял данные!");
        assertTrue(timeMulti < timeSingle, "Многопоточная обработка должна быть быстрее!");
    }
}