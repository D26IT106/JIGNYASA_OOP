package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportGenerator
{
    public static void generateReport(Path logsDir, Path reportFile) throws IOException
    {
        long totalDeposits = 0;
        long totalWithdrawals = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log"))
        {
            for (Path logFile : stream)
            {
                // File details
                BasicFileAttributes attrs = Files.readAttributes(logFile, BasicFileAttributes.class);
                long size = attrs.size();
                String lastModified = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(attrs.lastModifiedTime().toMillis()));

                System.out.println("Processing: " + logFile.getFileName() + " (Size: " + size + " bytes, Modified: " + lastModified + ")");

                // Read log lines
                try (BufferedReader reader = Files.newBufferedReader(logFile))
                {
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        if (line.startsWith("DEPOSIT "))
                        {
                            totalDeposits += Long.parseLong(line.substring(8).trim());
                        }
                        else if (line.startsWith("WITHDRAW "))
                        {
                            totalWithdrawals += Long.parseLong(line.substring(9).trim());
                        }
                    }
                }
            }
        }

        // Write report
        StringBuilder report = new StringBuilder();
        report.append("=== End-of-Day Summary Report ===\n");
        report.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        report.append("Total Deposits:   ").append(totalDeposits).append("\n");
        report.append("Total Withdrawals: ").append(totalWithdrawals).append("\n");
        report.append("Net Change:       ").append(totalDeposits - totalWithdrawals).append("\n");
        report.append("===================================\n");

        Files.writeString(reportFile, report.toString());
        System.out.println("Report written to: " + reportFile);
    }
}