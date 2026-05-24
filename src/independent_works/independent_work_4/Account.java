package independent_works.independent_work_4;

import java.util.Objects;

public class Account {

    String card;
    String name;
    IBank bank;

    public Account(String card, String name, IBank bank) {
        this.card = card;
        this.name = name;
        this.bank = bank;
    }

    public IBank getBank() {
        return bank;
    }

    public boolean pay(Check check) {
        return bank.pay(check);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Account a &&
                Objects.equals(card, a.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card);
    }

    @Override
    public String toString() {
        return name;
    }
}