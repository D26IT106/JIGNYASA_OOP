package service;

import model.Account;

import java.io.*;
import java.nio.file.Path;

public class StatePersister
{
    public static void save(Account[] accounts, Path file)
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file.toFile())))
        {
            oos.writeObject(accounts);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to save accounts", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Account[] load(Path file)
    {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.toFile())))
        {
            return (Account[]) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Failed to load accounts", e);
        }
    }
}