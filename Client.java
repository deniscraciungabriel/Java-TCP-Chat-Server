import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String nickname;
    private Scanner scanner = new Scanner(System.in);

    public Client(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        start();
    }

    public void start() throws IOException {
        String input = "";
        Listen listen = new Listen(this.clientSocket);
        Thread thread = new Thread(listen);
        thread.start();
        while (!input.equals("/quit")) {
            input = scanner.nextLine();
            out.println(input);
        }
        stopConnection();
    }

    public class Listen implements Runnable {

        private Socket clientSocket;

        public Listen(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                String res;
                while (!this.clientSocket.isClosed()) {
                    res = in.readLine();
                    System.out.println(res);
                }
            } catch (IOException e) {

            }
        }
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        scanner.close();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1", 3000);
        System.out.println("Client closed");
    }
}
