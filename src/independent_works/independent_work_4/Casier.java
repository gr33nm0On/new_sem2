package independent_works.independent_work_4;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Casier {

    private final Map<Account, Map<Product, Integer>> orders = new ConcurrentHashMap<>();

    public void order(Account account, String[] files) {

        Map<Product, Integer> products = new HashMap<>();

        for (String file : files)
            for (Product p : Parser.parse(file)) {
                products.merge(p, 1, Integer::sum);
            }

        synchronized (orders) {
            if (!orders.containsKey(account)) {
                orders.put(account, new HashMap<>());
            }

            Map<Product, Integer> existing = orders.get(account);

            for (Map.Entry<Product, Integer> e : products.entrySet()){
                existing.merge(e.getKey(), e.getValue(),Integer::sum);
            }
        }
    }

    public ArrayList<Check> terminal() {

        ArrayList<Check> checks = new ArrayList<>();

        synchronized (orders) {

            for (Map.Entry<Account, Map<Product, Integer>> e : orders.entrySet()) {
                checks.add(new Check(e.getKey(), e.getValue()));
            }
            orders.clear();
        }

        for (Check c : checks) {
            c.account.pay(c);
            Printer.save(c);
        }

        return checks;
    }
}