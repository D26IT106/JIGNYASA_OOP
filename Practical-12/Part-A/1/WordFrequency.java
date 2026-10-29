import java.util.*;

public class WordFrequency {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter a paragraph:");
        String paragraph = sc.nextLine().toLowerCase();

        String[] words = paragraph.split("\\s+");

        HashMap<String, Integer> map = new HashMap<>();

        for (String word : words) {
            word = word.replaceAll("[^a-z0-9]", "");

            if (!word.isEmpty()) {
                map.put(word, map.getOrDefault(word, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(map.entrySet());

        list.sort((a, b) ->
                Integer.compare(b.getValue(), a.getValue()));

        System.out.println("Word Frequencies:");

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(
                    entry.getKey() + " : " + entry.getValue());
        }

        sc.close();
    }
}