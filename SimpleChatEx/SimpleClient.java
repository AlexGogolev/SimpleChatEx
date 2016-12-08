package SimpleChatEx;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient {
    //private JFrame frame;
    JPanel panel;
    JTextArea textArea;
    JTextField textField;
    JButton button;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    public void go() {
        JFrame frame = new JFrame();
        panel = new JPanel();
        textArea = new JTextArea(20, 20);
        textField = new JTextField(15);
        button = new JButton("Send");

        //frame.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //set up vertical orientation elements in layout

        JScrollPane qScroller = new JScrollPane(textArea);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(qScroller);
        panel.add(textField);
        panel.add(button);
        frame.add(panel);

        networking();

        button.addActionListener(new buttonSendListener());

        Thread t1 = new Thread(new Task()); //create stream which get message from server
        t1.start();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.pack();
        frame.setVisible(true);

    }

    public void networking() {
        try {
            String serviceMessage = "ip server: " + InetAddress.getByName("prog-01").getHostAddress() + "\n";
           // System.out.println(serviceMessage);
            textArea.append(serviceMessage);
            socket = new Socket(InetAddress.getByName("prog-01").getHostAddress(), 50000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            // System.out.println("connection to server successfully");
            textArea.append("connection to server successfully \n");
        } catch (Exception e) {
            //System.out.println("server isn't available");
            textArea.append("server isn't available!!! \n");
            e.toString();
            //e.printStackTrace();
        }
    }

    public class Task implements Runnable {
        @Override
        public void run() {
            String message;

            try {
                while ((message = reader.readLine()) != null) {
                    textArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class buttonSendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //create output stream ...
            String namePC;
            try {
                namePC = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e1) {
                namePC = "some_name";
                e1.printStackTrace();
            }

            String message;
            message = textField.getText();
            writer.println(namePC + ": " + message);
            writer.flush();

            textField.setText("");
            textField.requestFocus();

        }
    }

    public static void main(String[] args) {
        new SimpleClient().go();
    }
}
