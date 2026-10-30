package model;

import model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Repository<T>
{
    private final Map<String, T> storage = new HashMap<>();

    public T save(T entity)
    {
        if (entity == null)
        {
            return null;
        }

        String idValue = null;
        Class<?> clazz = entity.getClass();

        // Traverse class hierarchy to find @Id field (may be in parent class)
        while (clazz != null && clazz != Object.class)
        {
            for (Field field : clazz.getDeclaredFields())
            {
                if (field.isAnnotationPresent(Id.class))
                {
                    field.setAccessible(true);
                    try
                    {
                        Object value = field.get(entity);
                        if (value != null)
                        {
                            idValue = value.toString();
                        }
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException("Cannot access @Id field", e);
                    }
                    break;
                }
            }
            if (idValue != null)
            {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        if (idValue == null)
        {
            throw new IllegalArgumentException("No @Id field found or value is null");
        }

        storage.put(idValue, entity);
        return entity;
    }

    public T findById(String id)
    {
        return storage.get(id);
    }

    public ArrayList<T> findAll()
    {
        return new ArrayList<>(storage.values());
    }

    public boolean delete(String id)
    {
        return storage.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items)
    {
        for (T item : items)
        {
            save(item);
        }
    }

    public static <N extends Number> double average(N[] values)
    {
        if (values == null || values.length == 0)
        {
            return 0.0;
        }
        double sum = 0.0;
        for (N val : values)
        {
            sum += val.doubleValue();
        }
        return sum / values.length;
    }
}