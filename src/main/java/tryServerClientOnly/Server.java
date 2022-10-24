package tryServerClientOnly;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {
    private static List<Thread> serverList = new LinkedList<>();
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9999);
        Socket client = server.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

        Thread serverThread = new Thread(
                () -> {
                    String response;
                    while (true) {
                        try {
                            System.out.println("Жду сообщения от клиента");
                            response = in.readLine();
                            System.out.println("Клиент сказал : " + response);
                            out.write(String.format("Сервер получил -> %s\n", response));
                            out.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        serverThread.start();
    }
}
