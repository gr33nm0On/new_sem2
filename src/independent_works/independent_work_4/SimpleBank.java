package independent_works.independent_work_4;

public class SimpleBank implements IBank {

    @Override
    public boolean pay(Check check) {
        System.out.println(check);
        return true;
    }
}