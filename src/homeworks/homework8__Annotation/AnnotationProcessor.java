package homeworks.homework8__Annotation;

import java.lang.reflect.Field;

public class AnnotationProcessor {

    public static void process(Object obj) throws Exception {

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            Object currentValue = field.get(obj);
            Class<?> type = field.getType();

            if (field.isAnnotationPresent(DefaultValue.class)) {

                DefaultValue annotation = field.getAnnotation(DefaultValue.class);

                if (isDefault(type, currentValue)) {
                    field.set(obj, parse(type, annotation.value()));
                }
            }

            if (field.isAnnotationPresent(MaxValue.class)) {

                MaxValue annotation = field.getAnnotation(MaxValue.class);

                if (currentValue instanceof Number number) {
                    if (number.doubleValue() > annotation.value()) {
                        throw new IllegalArgumentException(
                                field.getName() + " exceeds max value"
                        );
                    }
                }
            }
        }
    }

    private static Object parse(Class<?> type, String value) {

        return switch (type.getName()) {
            case "java.lang.String" -> value;
            case "int", "java.lang.Integer" -> Integer.parseInt(value);
            case "double", "java.lang.Double" -> Double.parseDouble(value);
            case "boolean", "java.lang.Boolean" -> Boolean.parseBoolean(value);
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    private static boolean isDefault(Class<?> type, Object value) {

        return switch (type.getName()) {
            case "java.lang.String" -> value == null;
            case "int" -> (int) value == 0;
            case "double" -> (double) value == 0.0;
            case "boolean" -> !(boolean) value;
            default -> value == null;
        };
    }
}