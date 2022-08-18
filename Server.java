import java.net.*;
import java.util.ArrayList;
import java.io.*;

/*
THINGS TO FIX:
- when multiple clients are started without name init, the name won't be inint
 */

public class Server {
    private ServerSocket serverSocket;
    private ArrayList<ConnectionHandler> connections;

    public Server(int port) throws IOException {
        connections = new ArrayList<>();
        this.serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port + "...");
        System.out.println("Listening for connections...");
        while (!serverSocket.isClosed()) {
            ConnectionHandler handler = new ConnectionHandler(serverSocket.accept());
            connections.add(handler);
            Thread thread = new Thread(handler);
            thread.start();
        }
    }

    public void broadcast(String message, String sender) {
        for (ConnectionHandler ch : connections) {
            if (ch.nickname != null) {
                if (!ch.nickname.equals(sender)) {
                    ch.sendMessage(message, sender);
                }
            }
        }
    }

    class ConnectionHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String nickname;
        private String message = "";

        public ConnectionHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                String temp;
                out.println("What's your username?");
                String res = in.readLine();
                nickname = res;
                System.out.println(res + " has joined the chat");
                broadcast(res + " has joined the chat", "Server");
                while (!clientSocket.isClosed()) {
                    message = in.readLine();
                    if (message.startsWith("/name")) {
                        temp = this.nickname;
                        this.nickname = message.split(" ")[1];
                        broadcast(temp + " has changed the username to " + this.nickname, "Server");
                    } else if (message.equals("/quit")) {
                        stop();
                    } else {
                        broadcast(message, this.nickname);
                    }
                }
                broadcast(nickname + " has left the chat", "Server");
            } catch (IOException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        public void stop() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
            System.out.println(nickname + "has stopped the connection");
        }

        public void sendMessage(String message, String sender) {
            out.println(sender + ": " + message);
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(3000);
        System.out.println("Server closed");
    }
}

/*
 * this.clientSocket = serverSocket.accept();
 * out = new PrintWriter(clientSocket.getOutputStream(), true);
 * in = new BufferedReader(new
 * InputStreamReader(clientSocket.getInputStream()));
 * while (!message.equals("/quit")) {
 * this.message = in.readLine();
 * if (message.startsWith("/name")) {
 * this.nickname = message.split(" ")[1];
 * out.println("Greetings " + nickname);
 * } else {
 * out.println("Your message was: " + message);
 * }
 * }
 */