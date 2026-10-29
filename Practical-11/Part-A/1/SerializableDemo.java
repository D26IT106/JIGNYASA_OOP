import java.io.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    int id;
    String name;
    transient String password;

    Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    void display() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Password: " + password);
        System.out.println();
    }
}

public class SerializableDemo {
    public static void main(String[] args) {

        Student[] students = {
            new Student(1, "Jignyasa", "abc123"),
            new Student(2, "Maitri", "xyz456"),
            new Student(3, "Alice", "pass789")
        };

        String fileName = "students.dat";

        try {
            // Save objects to file
            ObjectOutputStream out =
                    new ObjectOutputStream(new FileOutputStream(fileName));

            out.writeObject(students);
            out.close();

            System.out.println("Objects saved successfully.\n");

            // Load objects from file
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(fileName));

            Student[] loadedStudents = (Student[]) in.readObject();
            in.close();

            System.out.println("Objects loaded successfully.\n");

            // Display loaded objects
            for (Student student : loadedStudents) {
                student.display();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}