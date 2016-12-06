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

    ArrayList<PrintWriter> clientOutputStreams; //Why then create ArrayList output socket streams


    public class ClientHandler implements Runnable {
        //class - the task for stream (thread)
        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            } catch(Exception ex) {ex.printStackTrace();}
        } // close constructor

        public void run() {
            String message;

            try {

                while ((message = reader.readLine()) != null) {

                    System.out.println("read " + message);
                    tellEveryone(message);

                } // close while
            } catch(Exception ex) {ex.printStackTrace();}
        } // close run
    } // close inner class




    public static void main (String[] args) {
        new SimpleServer().go();
    }

    public void go() {
        clientOutputStreams = new ArrayList<PrintWriter>();

        try {
            ServerSocket serverSock = new ServerSocket(50000);

            while(true) {
                Socket clientSocket = serverSock.accept();//whaiting client socket
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();

                System.out.println("got a connection");
            }
            // now if I get here I have a connection

        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void tellEveryone(String message) {
        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch(Exception ex) {
                ex.printStackTrace();
            }

        } // end while

    } // close tellEveryone


}
