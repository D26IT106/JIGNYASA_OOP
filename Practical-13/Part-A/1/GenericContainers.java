class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class GenericContainers {

    public static <T> T firstElement(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {

        Box<Integer> b1 = new Box<>();
        b1.set(100);
        System.out.println("Integer Box: " + b1.get());

        Box<String> b2 = new Box<>();
        b2.set("Hello Java");
        System.out.println("String Box: " + b2.get());

        Pair<String, Integer> p1 =
                new Pair<>("Alice", 20);

        System.out.println("Name: " + p1.getFirst());
        System.out.println("Age: " + p1.getSecond());

        String[] names = {"Amit", "Riya", "Raj"};
        Integer[] numbers = {10, 20, 30};

        System.out.println("First name: "
                + firstElement(names));

        System.out.println("First number: "
                + firstElement(numbers));
    }
}