import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Logger {
    private  static final Logger logger = new Logger();
    private String logPath = "src" + File.separator + "main" +
            File.separator + "resources" +
            File.separator + "file.log";

    private Logger() {
    }
    public static Logger getInstance(){
        return logger;
    }
    public void log(Date date, String name, String message) {
        byte[] messageInBytes = new StringBuilder()
                .append(date)
                .append("_")
                .append(name)
                .append(": ")
                .append(message)
                .append('\n')
                .toString()
                .getBytes();
        try (FileOutputStream outputLog = new FileOutputStream(logPath, true)) {
            outputLog.write(messageInBytes, 0, messageInBytes.length);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
