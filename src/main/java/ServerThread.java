import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public class ServerThread extends Thread {
    private Socket clientSocket;
    private List<ServerThread> serverList;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName;
    private String serverName;
    private static final Logger LOGGER = Logger.getInstance();

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getServerName() {
        return serverName;
    }

    public ServerThread(Socket clientSocket, List<ServerThread> serverList, String serverName) throws IOException {
        this.clientSocket = clientSocket;
        this.serverList = serverList;
        this.serverName = serverName;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.start();
    }

    @Override
    public void run() {
        String response;
        System.out.println("Run запустился");
        try {
            System.out.println("Прошли try");
            ServerThread.this.clientName = ServerThread.this.in.readLine();
            System.out.println("Прошли readline");
            response = LOGGER.log(new Date(), ServerThread.this.serverName, "К нам присоединился " + getClientName());
            sendToAll(response);
            response = LOGGER.log(new Date(), getServerName(), "Привет, " + getClientName());
            send(response);
            while (true) {
                System.out.println("Вошли в while");
                response = in.readLine();
                System.out.println("Прошли readLine");
                if (response.equals("/exit")) {
                    this.sendToAll(LOGGER.log(new Date(), getServerName(), getClientName() + "уходит от нас"));
                    this.downClient();
                    break;
                }
                this.sendToAll(response);
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
                serverList.remove(this);
                this.interrupt();
            } catch (IOException e) {
                e.getMessage();
            }
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
            for (ServerThread serverThread : serverList) {
                if (serverThread == this) {
                    continue;
                }
                serverThread.out.write(message + '\n');
                serverThread.out.flush();
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }


}
