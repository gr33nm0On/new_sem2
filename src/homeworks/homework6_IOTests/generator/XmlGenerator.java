package homeworks.homework6_IOTests.generator;

import homeworks.homework6_IOTests.model.Student;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XmlGenerator {
    public static void generateXml(List<Student> students, String filename) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("<students>\n");
            for (Student student: students) {
                writer.write("\t<student>\n");
                writer.write("\t\t<id>" + student.id + "</id>");
                writer.write("<fullName>" + student.fullName + "</fullName>");
                writer.write("<averageGrade>" + student.averageGrade + "</averageGrade>");
                writer.write("\n\t</student>\n");
            }
            writer.write("</students>\n");
            System.out.println("XML DONE" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
