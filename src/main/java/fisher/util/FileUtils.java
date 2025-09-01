package fisher.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileUtils {

    private static final java.nio.file.Path baseDir = java.nio.file.Paths
            .get("C:\\Users\\Matthew\\Desktop\\Minecraft\\auto\\fabric\\logs\\v2");
    private static final String filename = "logs-"
            + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("uuuu-MM-dd")) +
            ".txt";
    private static final Path logPath = baseDir.resolve(filename);
    private static final java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter
            .ofPattern("uuuu-MM-dd HH:mm:ss.SSS");

    public static void initialize() {
        try {
            java.nio.file.Files.createDirectories(baseDir);
            if (java.nio.file.Files.exists(logPath)) {
                java.nio.file.Files.delete(logPath);
            }
            java.nio.file.Files.createFile(logPath);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Append content to a file safely.
     * Creates the file if it does not exist.
     */
    public static void writeStringToFile(String content) {
        synchronized (FileUtils.class) {
            String line = String.format(
                    "[%s] %s\n",
                    fmt.format(java.time.LocalDateTime.now()),
                    content);
            System.out.println(line);
            try {
                Files.writeString(logPath, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
