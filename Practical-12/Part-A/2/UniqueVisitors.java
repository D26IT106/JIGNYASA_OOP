import java.util.*;
public class UniqueVisitors {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of login names:");
        int n = sc.nextInt();
        HashSet<String> set = new HashSet<>();
        LinkedHashSet<String> orderedSet = new LinkedHashSet<>();
        System.out.println("Enter login names:");
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            set.add(name);
            orderedSet.add(name);
        }
        System.out.println("Unique visitors: " + set.size());
        System.out.println("First three distinct names:");
        int count = 0;
        for (String name : orderedSet) {
            if (count == 3) {
                break;
            }
            System.out.println(name);
            count++;
        }
        sc.close();
    }
}