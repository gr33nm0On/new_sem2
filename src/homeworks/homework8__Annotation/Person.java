package homeworks.homework8__Annotation;

public class Person {

    @DefaultValue(
            value = "Noname",
            type = String.class
    )
    private String name;

    @DefaultValue(
            value = "0",
            type = Integer.class
    )
    @MaxValue(
            value = 100
    )
    private Integer age;

    public Person() {}
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }
}