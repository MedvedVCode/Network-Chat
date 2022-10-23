import java.io.File;

public class StartClient02 {
    public static final String PATH_TO_SETTINGS = "src" + File.separator + "main" +
            File.separator + "resources" +
            File.separator + "settings.txt";
    public static void main(String[] args) {
        new Client(PATH_TO_SETTINGS);
    }
}
