package classworks.classwork12.task3;

import org.junit.jupiter.api.Test;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

/*
ТЗ для студента:
Открылась продажа билетов на стадион (всего 100 000 мест). В первую секунду сервер получает миллионы запросов от фанатов.
Каждый запрос нужно верифицировать (проверить подпись токена — долгая операция). Если верификация успешна — нужно выдать место.
Напиши класс TicketInventory, защитив счетчик оставшихся билетов через ReentrantLock.
Реализуй многопоточную обработку запросов.
ХИТРОСТЬ: Если обернуть процесс верификации пользователя (токен) в Lock, очередь остановится и многопоточность станет медленнее однопотока. Правильно определи границы "Критической секции"!
 */

public class ConcertBookingTask {
    // Класс запроса
    static class BookingRequest {
        long userId;
        String token;
        public BookingRequest(long userId, String token) { this.userId = userId; this.token = token; }
    }

    // Имитация тяжелой проверки авторизации по сети
    private boolean verifyUserToken(BookingRequest req) {
        double dummy = req.userId;
        for (int i = 0; i < 100; i++) {
            dummy = Math.sin(dummy) + Math.cos(dummy);
        }
        return req.token.equals("VALID");
    }

    // ГЕНЕРАТОР ДАННЫХ
    public BookingRequest[] generateRequests(int count) {
        BookingRequest[] requests = new BookingRequest[count];
        for (int i = 0; i < count; i++) {
            // 90% запросов валидные, 10% хакеры
            String token = (i % 10 == 0) ? "INVALID" : "VALID";
            requests[i] = new BookingRequest(i, token);
        }
        return requests;
    }

    // (Касса с Lock)
    static class TicketInventory {
        private int availableSeats;
        private final Lock lock = new ReentrantLock();

        public TicketInventory(int totalSeats) { this.availableSeats = totalSeats; }
        public int getAvailableSeats() { return availableSeats; }

        public boolean tryBookSeat() {
            try {
                lock.lock();
                if (availableSeats > 0) {
                    availableSeats--;
                    return true;
                }
                else {
                    return false;
                }
            }
            finally {
                lock.unlock();
            }
        }
    }

    public int processSingleThread(BookingRequest[] requests, TicketInventory inventory) {
        int counter = 0;

        for (BookingRequest request: requests) {
            if (verifyUserToken(request) && inventory.tryBookSeat()) {
                counter++;
            }
        }
        return counter;
    }

    public int processMultiThread(BookingRequest[] requests, TicketInventory inventory, int threadsCount) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        Thread[] threads = new Thread[threadsCount];
        int range = (requests.length) / threadsCount;

        for (int i = 0; i < threadsCount; i++) {
            final int start = i * range;
            final int end = (i == threadsCount - 1) ? requests.length : start + range;

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (verifyUserToken(requests[j]) && inventory.tryBookSeat()) {
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

    // АВТОТЕСТ
    @Test
    void testBookingSystem() throws InterruptedException {
        int requestsCount = 3_000_000;
        int totalSeats = 100_000; // Мест сильно меньше, чем запросов
        BookingRequest[] requests = generateRequests(requestsCount);

        // Однопоточка
        TicketInventory inventorySingle = new TicketInventory(totalSeats);
        long startSingle = System.currentTimeMillis();
        int soldSingle = processSingleThread(requests, inventorySingle);
        long timeSingle = System.currentTimeMillis() - startSingle;

        // Многопоточка
        TicketInventory inventoryMulti = new TicketInventory(totalSeats);
        long startMulti = System.currentTimeMillis();
        int soldMulti = processMultiThread(requests, inventoryMulti, 6);
        long timeMulti = System.currentTimeMillis() - startMulti;

        System.out.println("Booking Single: " + timeSingle + " ms, Multi: " + timeMulti + " ms");


        assertEquals(totalSeats, soldSingle, "В однопотоке распроданы не все билеты!");
        assertEquals(totalSeats, soldMulti, "Гонка данных! Продано билетов больше/меньше, чем есть!");
        assertEquals(0, inventoryMulti.getAvailableSeats(), "Счетчик билетов не равен 0!");

        // Главная проверка на правильный размер критической секции
        assertTrue(timeMulti < timeSingle, "Многопоточная касса работает медленнее! Ты заблокировал тяжелую операцию?");
    }
}
