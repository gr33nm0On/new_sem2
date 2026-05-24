package independent_works.independent_work_4;

public class Main {

    public static void main(String[] args) {

        Casier c = new Casier();

        Account a1 = new Account("111", "lexa", new SimpleBank());
        Account a2 = new Account("222", "vanya", new PremiumBank());

        String[] files1 = {
                "products1.json",
                "products2.json"
        };
        String[] files2 = {
                "products3.json"
        };

        Thread t1 = new Thread(() -> c.order(a1, files1));
        Thread t2 = new Thread(() -> c.order(a2, files2));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        c.terminal();
        c.terminal();
    }
}