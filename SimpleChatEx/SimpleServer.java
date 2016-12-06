package SimpleChatEx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class SimpleServer {

    private ArrayList<PrintWriter> clientsConnected;

    public class ClientRunnable implements Runnable {
        BufferedReader reader;

        private ClientRunnable(Socket socket) {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("create new Thread");
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    sendEveryone(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendEveryone(String message) {
        Iterator it = clientsConnected.iterator();
        while (it.hasNext()) {
            PrintWriter writer = (PrintWriter) it.next();
            writer.println(message);
            writer.flush();
        }
    }

    private void go() {
        clientsConnected = new ArrayList<PrintWriter>();

        try {
            ServerSocket serverSocket = new ServerSocket(50000);

            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                clientsConnected.add(writer);

                Thread threadClient = new Thread(new ClientRunnable(socket));
                threadClient.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new SimpleServer().go();
    }


}
