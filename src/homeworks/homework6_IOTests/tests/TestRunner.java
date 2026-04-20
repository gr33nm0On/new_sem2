package homeworks.homework6_IOTests.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestRunner {
    public static void runTests(Class<?> testClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Method method: testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(FileTest.class)) {
                Object instance = testClass.getDeclaredConstructor().newInstance();
                FileTest annotation = method.getAnnotation(FileTest.class);
                method.invoke(instance, annotation.filename());
            }
        }
    }
}
