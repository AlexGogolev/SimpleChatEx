package SimpleChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

    ServerSocket serverSocket;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;


    public void go(){
        String message;
        while (true){
            try {
                serverSocket = new ServerSocket(50000);
                socket = serverSocket.accept();
                message = reader.readLine();
                System.out.println("from client: "+message);
                writer.println(message);

                //надо сделать поток!!! и обрабатыватть в потоках!!!

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SimpleServer().go();
    }
}
