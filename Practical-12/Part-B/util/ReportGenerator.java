package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

public final class ReportGenerator {
    private ReportGenerator() {}

    public static String generate(Path logsFolder, Path reportFile)
            throws IOException {
        long totalDeposits = 0L;
        long totalWithdrawals = 0L;
        List<String> fileDetails = new ArrayList<>();

        Files.createDirectories(logsFolder);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsFolder)) {
            for (Path logFile : stream) {
                if (!Files.isRegularFile(logFile)) {
                    continue;
                }

                try (BufferedReader reader =
                             Files.newBufferedReader(logFile, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Log format: TYPE,ACCOUNT_NUMBER,AMOUNT
                        String[] parts = line.split(",");
                        if (parts.length >= 3) {
                            try {
                                long amount = Long.parseLong(parts[2].trim());
                                if ("DEPOSIT".equalsIgnoreCase(parts[0].trim())) {
                                    totalDeposits += amount;
                                } else if ("WITHDRAW".equalsIgnoreCase(parts[0].trim())) {
                                    totalWithdrawals += amount;
                                }
                            } catch (NumberFormatException ignored) {
                                // Ignore malformed log entries.
                            }
                        }
                    }
                }

                java.nio.file.attribute.BasicFileAttributes attrs =
                        Files.readAttributes(logFile,
                                java.nio.file.attribute.BasicFileAttributes.class);
                fileDetails.add(logFile.getFileName()
                        + " | size=" + attrs.size() + " bytes"
                        + " | last-modified=" + attrs.lastModifiedTime());
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("MINIBANK END-OF-DAY REPORT").append(System.lineSeparator());
        report.append("==========================").append(System.lineSeparator());
        report.append("Total deposits: Rs. ").append(totalDeposits)
                .append(System.lineSeparator());
        report.append("Total withdrawals: Rs. ").append(totalWithdrawals)
                .append(System.lineSeparator());
        report.append("Net transaction amount: Rs. ")
                .append(totalDeposits - totalWithdrawals)
                .append(System.lineSeparator());
        report.append(System.lineSeparator()).append("LOG FILE DETAILS")
                .append(System.lineSeparator());

        if (fileDetails.isEmpty()) {
            report.append("No log files found.").append(System.lineSeparator());
        } else {
            for (String details : fileDetails) {
                report.append(details).append(System.lineSeparator());
            }
        }

        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(reportFile, report.toString().getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return report.toString();
    }
}
