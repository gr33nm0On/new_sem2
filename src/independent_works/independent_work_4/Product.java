package independent_works.independent_work_4;

import java.util.Objects;

public class Product {

    int id;
    String name;
    String cat;

    public Product(int id, String name, String cat) {
        this.id = id;
        this.name = name;
        this.cat = cat;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Product p && id == p.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " x" + id;
    }
}