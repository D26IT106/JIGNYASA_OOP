package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import model.annotation.MaxLength;
import model.annotation.Positive;

public class AnnotationValidator {

    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();

        if (obj == null) {
            errors.add("Object cannot be null");
            return errors.toArray(new String[0]);
        }

        Class<?> currentClass = obj.getClass();

        // Inspect the object's class and its parent classes.
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)) {
                        Positive annotation = field.getAnnotation(Positive.class);

                        if (value instanceof Number) {
                            Number number = (Number) value;
                            if (number.doubleValue() <= 0) {
                                errors.add(field.getName() + " " + annotation.message());
                            }
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {
                        MaxLength annotation = field.getAnnotation(MaxLength.class);

                        if (value instanceof String) {
                            String text = (String) value;
                            if (text.length() > annotation.value()) {
                                errors.add(field.getName() + " must contain at most "
                                        + annotation.value() + " characters");
                            }
                        }
                    }
                } catch (IllegalAccessException | SecurityException e) {
                    errors.add("Cannot access field: " + field.getName());
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
