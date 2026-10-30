package model;

import model.annotation.MaxLength;
import model.annotation.Positive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationValidator
{
    public static String[] validate(Object obj)
    {
        if (obj == null)
        {
            return new String[] { "Object is null" };
        }

        List<String> errors = new ArrayList<>();
        Class<?> clazz = obj.getClass();

        // Traverse class hierarchy to include inherited fields
        while (clazz != null && clazz != Object.class)
        {
            for (Field field : clazz.getDeclaredFields())
            {
                field.setAccessible(true);

                // Check @Positive
                if (field.isAnnotationPresent(Positive.class))
                {
                    Positive annotation = field.getAnnotation(Positive.class);
                    try
                    {
                        Object value = field.get(obj);
                        if (value instanceof Number number)
                        {
                            if (number.longValue() <= 0)
                            {
                                errors.add(field.getName() + " " + annotation.message());
                            }
                        }
                    }
                    catch (IllegalAccessException e)
                    {
                        errors.add("Cannot access field: " + field.getName());
                    }
                }

                // Check @MaxLength
                if (field.isAnnotationPresent(MaxLength.class))
                {
                    MaxLength annotation = field.getAnnotation(MaxLength.class);
                    try
                    {
                        Object value = field.get(obj);
                        if (value instanceof String str)
                        {
                            if (str.length() > annotation.value())
                            {
                                errors.add(field.getName() + " exceeds max length of " + annotation.value());
                            }
                        }
                    }
                    catch (IllegalAccessException e)
                    {
                        errors.add("Cannot access field: " + field.getName());
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}