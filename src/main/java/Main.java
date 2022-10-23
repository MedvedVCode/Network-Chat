import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static final int PORT_SERVER = 9999;
    public static final String HOST_ADDRESS = "localhost";
    public static final String PATH_TO_SETTINGS = "src" + File.separator + "main" +
            File.separator + "resources" +
            File.separator + "settings.txt";

    public static final Logger LOGGER = Logger.getInstance();

    public static final String SERVER_NAME = "Server";

    public static void main(String[] args) throws IOException {
        List<ServerThread> serverList = new LinkedList<>();
        String message;
        ServerSocket serverSocket = new ServerSocket(PORT_SERVER);
        if (setSettingsToTxt(PORT_SERVER)) {
            LOGGER.log(new Date(), SERVER_NAME, "Собрали settings.txt");
        } else {
            LOGGER.log(new Date(), SERVER_NAME, "Неудача при создании settings.txt");
            serverSocket.close();
        }
        message = LOGGER.log(new Date(), SERVER_NAME, "Сервер запущен и ждет клиентов!");
        System.out.println(message);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            serverList.add(new ServerThread(clientSocket, serverList, SERVER_NAME));
        }
    }

    private static boolean setSettingsToTxt(int localPort) {
        try (FileOutputStream outputTxt =
                     new FileOutputStream(PATH_TO_SETTINGS, false)) {
            byte[] bytesSettings = new StringBuilder()
                    .append(localPort)
                    .append("\n")
                    .append(HOST_ADDRESS)
                    .toString()
                    .getBytes();
            outputTxt.write(bytesSettings, 0, bytesSettings.length);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

