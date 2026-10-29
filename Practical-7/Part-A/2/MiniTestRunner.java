import java.lang.annotation.*;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

class MyTests {

    @Run
    public void testAddition() {
        int result = 10 + 20;
        System.out.println("Test Addition: " +
                (result == 30 ? "PASSED" : "FAILED"));
    }

    @Run
    public void testSubtraction() {
        int result = 20 - 10;
        System.out.println("Test Subtraction: " +
                (result == 10 ? "PASSED" : "FAILED"));
    }

    public void normalMethod() {
        System.out.println("This method will not run.");
    }

    @Run
    public void testMultiplication() {
        int result = 5 * 4;
        System.out.println("Test Multiplication: " +
                (result == 20 ? "PASSED" : "FAILED"));
    }
}

public class MiniTestRunner {

    public static void main(String[] args) {

        MyTests testObject = new MyTests();
        int count = 0;

        Method[] methods =
                testObject.getClass().getDeclaredMethods();

        for (Method method : methods) {

            if (method.isAnnotationPresent(Run.class)
                    && method.getParameterCount() == 0) {

                try {
                    method.invoke(testObject);
                    count++;
                } catch (Exception e) {
                    System.out.println(
                            "Test execution failed: " + method.getName());
                }
            }
        }

        System.out.println("Total tests run: " + count);
    }
}