package independent_works.independent_work_4;

public class PremiumBank implements IBank {

    @Override
    public boolean pay(Check check) {
        System.out.println("premium " + check);
        return true;
    }
}