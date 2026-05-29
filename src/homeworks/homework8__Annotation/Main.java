package homeworks.homework8__Annotation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationProcessorTest {
    @Test
    void defaultValueTest() throws Exception {
        Person person = new Person();
        AnnotationProcessor.process(person);
        assertNotNull(person.getName());
    }

    @Test
    void maxValueValidTest() throws Exception {
        Person person = new Person("Ivan", 119);
        assertThrows(IllegalArgumentException.class, () -> AnnotationProcessor.process(person));
    }

    @Test
    void maxValueShouldThrowTest() {
        Person person = new Person("Ivan", 150);
        assertThrows(IllegalArgumentException.class,
                () -> AnnotationProcessor.process(person));
    }

    @Test
    void defaultValueAppliedTest() throws Exception {
        Person person = new Person();
        AnnotationProcessor.process(person);
        assertEquals("Noname", person.getName());
    }
}