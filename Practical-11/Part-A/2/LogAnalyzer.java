import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class LogAnalyzer {
    public static void main(String[] args) {

        String keyword = "ERROR";

        Path[] files = {
            Paths.get("log1.txt"),
            Paths.get("log2.txt")
        };

        int totalLines = 0;
        int keywordLines = 0;

        try {
            for (Path file : files) {

                int fileLines = 0;
                int fileKeywordLines = 0;

                System.out.println("\nFile: " + file.getFileName());

                // Read file line by line
                List<String> lines = Files.readAllLines(file);

                for (String line : lines) {
                    fileLines++;
                    totalLines++;

                    if (line.contains(keyword)) {
                        fileKeywordLines++;
                        keywordLines++;
                    }
                }

                // Get file size
                long size = Files.size(file);

                System.out.println("Size: " + size + " bytes");
                System.out.println("Total lines: " + fileLines);
                System.out.println("Lines containing '" + keyword + "': "
                        + fileKeywordLines);
            }

            System.out.println("\n----- Overall Result -----");
            System.out.println("Total lines across all files: " + totalLines);
            System.out.println("Lines containing '" + keyword + "': "
                    + keywordLines);

        } catch (IOException e) {
            System.out.println("Error reading files: " + e.getMessage());
        }
    }
}