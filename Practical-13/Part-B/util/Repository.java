package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import model.annotation.Id;

public class Repository<T> {
    private final HashMap<String, T> storage = new HashMap<>();

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null.");
        }
        String id = extractId(entity);
        storage.put(id, entity);
        return entity;
    }

    public T findById(String id) {
        return storage.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean delete(String id) {
        return storage.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    private String extractId(T entity) {
        Class<?> current = entity.getClass();
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        if (value == null) {
                            throw new IllegalArgumentException("@Id field cannot be null.");
                        }
                        return value.toString();
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(
                                "Unable to read @Id field: " + field.getName(), e);
                    }
                }
            }
            current = current.getSuperclass();
        }
        throw new IllegalArgumentException(
                "No field annotated with @Id found for " + entity.getClass().getName());
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values must not be null or empty.");
        }
        double total = 0.0;
        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values must not contain null.");
            }
            total += value.doubleValue();
        }
        return total / values.length;
    }
}
