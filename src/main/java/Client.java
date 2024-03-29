import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private BufferedReader consoleReader;
    private int port;
    private String host;
    private String nickname;
    private Thread readMsg;
    private Thread writeMsg;
    public Logger logger = Logger.getInstance();

    public Client(String settingsPath) {
        setSettingsFromTxt(settingsPath);
        try {
            this.socket = new Socket(host, port);
        } catch (IOException e) {
            e.getMessage();
        }
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            setNickname();
        } catch (IOException e) {
            e.getMessage();
        }
        readMsg = new Thread(() -> {
            String response;
            try {

                while (true) {
                    response = in.readLine();
                    if (response != null) {
                        System.out.println(response);
                    }
                }
            } catch (IOException e) {
                e.getMessage();
            }
        });

        writeMsg = new Thread(() -> {
            String message;
            try {
                while (true) {
                    message = consoleReader.readLine();
                    if (message.equalsIgnoreCase("/exit")) {
                        out.write(message + '\n');
                        out.flush();
                        shutdownClient();
                        break;
                    }
                    logger.log(new Date(), nickname, message);
                    out.write(String.format("%s: %s\n", nickname, message));
                    out.flush();
                }
            } catch (IOException e) {
                e.getMessage();
            }
        });
        readMsg.start();
        writeMsg.start();
    }

    private void shutdownClient() {
        readMsg.interrupt();
        writeMsg.interrupt();
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void setNickname() {
        System.out.println("Введите ваш имя в чате: ");
        try {
            nickname = consoleReader.readLine();
            out.write(nickname + '\n');
            out.flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void setSettingsFromTxt(String settingsPath) {
        try (FileInputStream inputTxt = new FileInputStream(settingsPath)) {
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = inputTxt.read()) != (int) '\n') {
                sb.append((char) i);
            }
            port = Integer.parseInt(sb.toString());
            sb.setLength(0);
            while ((i = inputTxt.read()) != -1) {
                sb.append((char) i);
            }
            host = sb.toString();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
