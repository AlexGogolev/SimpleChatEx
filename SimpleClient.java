package SimpleChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 1. create GUI
 * 2. create setting network
 * 3.
 */

public class SimpleClient {

    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    JTextArea textArea;
    JTextField textField;


    public void go() {
        //create GUI
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        textArea = new JTextArea(20, 30);
        textField = new JTextField(50);
        JButton button = new JButton("Send");

        panel.add(textArea);
        panel.add(textField);
        panel.add(button);
        frame.add(panel);

        button.addActionListener(new SendListener());

        networking();

        Thread t = new Thread(new Runn());//create input thread
        t.start();


    }

    public void networking() {

        try {
            socket = new Socket("localhost", 50000);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //send message in server
            String message = textField.getText();
            writer.println(message);
            System.out.println("client to: " + message);
        }
    }

    public class Runn implements Runnable {
        @Override
        public void run() {
            String message;

            try {
                while ((message = reader.readLine()) != null) {
                    textArea.append(message);
                    System.out.println("server to: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SimpleClient().go();

    }
}
