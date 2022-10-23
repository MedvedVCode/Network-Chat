import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Logger {
    private String logPath = "src" + File.separator + "main" +
            File.separator + "resources" +
            File.separator + "file.log";

    private static Logger logger;

    private Logger() {
    }

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public String log(Date date, String name, String message) {
        String formatMsg = new StringBuilder()
                .append(date)
                .append("\t")
                .append(name.toUpperCase())
                .append(": ")
                .append(message)
                .toString();
        try (FileOutputStream outputLog = new FileOutputStream(logPath, true)) {
            outputLog.write(formatMsg.getBytes(), 0, formatMsg.getBytes().length);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return formatMsg;
    }
}
