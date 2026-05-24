package independent_works.independent_work_4;

import java.util.Map;

public class Check {

    Account account;
    Map<Product, Integer> products;

    public Check(Account account, Map<Product, Integer> products) {
        this.account = account;
        this.products = products;
    }

    @Override
    public String toString() {
        return account + " " + products;
    }
}