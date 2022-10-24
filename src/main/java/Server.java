import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public class Server extends Thread {
    private Socket clientSocket;
    private List<Server> serverList;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName;
    private String serverName;
    private static final Logger LOGGER = Logger.getInstance();

    public Server(Socket clientSocket, List<Server> serverList, String serverName) throws IOException {
        this.clientSocket = clientSocket;
        this.serverList = serverList;
        this.serverName = serverName;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String response;
        try {
            //получаем имя клиента
            clientName = in.readLine();
            response = LOGGER.log(new Date(), serverName, "К нам присоединился " + clientName);
            sendToAll(response);
            response = LOGGER.log(new Date(), serverName, "Привет, " + clientName);
            send(response);
            //ждем сообщения и переправляем всем
            while (true) {
                response = in.readLine();
                if (response.equals("/exit")) {
                    sendToAll(LOGGER.log(new Date(), serverName, clientName + " уходит от нас"));
                    downClient();
                    break;
                }
                sendToAll(response);
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void downClient() {
        if (!clientSocket.isClosed()) {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.getMessage();
            }
            serverList.remove(this);
            this.interrupt();
        }
    }

    private void send(String message) {
        try {
            out.write(message + '\n');
            out.flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void sendToAll(String message) {
        try {
            for (Server server : serverList) {
                if (server == this) {
                    continue;
                }
                server.out.write(message + '\n');
                server.out.flush();
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }


}
