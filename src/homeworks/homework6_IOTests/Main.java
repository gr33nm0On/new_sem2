package homeworks.homework6_IOTests;

import homeworks.homework6_IOTests.generator.XmlGenerator;
import homeworks.homework6_IOTests.model.Student;
import homeworks.homework6_IOTests.pars.StreamParser;
import homeworks.homework6_IOTests.tests.TestRunner;
import homeworks.homework6_IOTests.tests.StudentFileTests;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Student> students = Arrays.asList(
                new Student("1", "Петров Петр Петрович", 4.5),
                new Student("2", "Иванов Иван Иванович", 4.9),
                new Student("3", "Помидоров Огурчик Кунжутович", 3.2)
        );

        XmlGenerator.generateXml(students, "src/homeworks/homework6_IOTests/students.xml");

        StreamParser.convertXmlToJson("src/homeworks/homework6_IOTests/students.xml", "src/homeworks/homework6_IOTests/students.json");

        TestRunner.runTests(StudentFileTests.class);
    }
}
