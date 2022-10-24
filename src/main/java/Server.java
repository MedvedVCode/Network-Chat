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
    public Logger logger = Logger.getInstance();

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
            logger.log(new Date(), serverName, "Новый участник " + clientName + "!");
            sendToAll(String.format("%s: К нам присоединился %s!", serverName, clientName));
            send(String.format("%s: Привет, %s!", serverName, clientName));
            //ждем сообщения и переправляем всем
            while (true) {
                response = in.readLine();
                if (response.equals("/exit")) {
                    logger.log(new Date(), serverName, clientName + " ушел");
                    sendToAll(String.format("%s: %s уходит от нас", serverName, clientName));
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
        // закрываем связь сервер-клиент
        if (!clientSocket.isClosed()) {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        // удаляем из списка серверов
        serverList.remove(this);
        this.interrupt();
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
