package tryServerClientOnly;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 9999);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader consoleScan = new BufferedReader(new InputStreamReader(System.in));

        Thread pushMessage = new Thread(
                () -> {
                    String response;
                    while (true) {
                        try {
                            System.out.println("Введите сообщение серверу: ");
                            response = consoleScan.readLine();
                            out.write(response + '\n');
                            out.flush();
                        } catch (IOException e) {
                            e.getMessage();
                        }
                    }
                }
        );

        Thread getMessage = new Thread(
                () -> {
                    String response;
                    while (true) {
                        try {
                            response = in.readLine();
                            System.out.println(response);
                        } catch (IOException e) {
                            e.getMessage();
                        }
                    }
                }
        );

        getMessage.start();
        pushMessage.start();
    }
}
