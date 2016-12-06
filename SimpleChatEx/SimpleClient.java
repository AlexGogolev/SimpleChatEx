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
    JFrame frame;
    JPanel panel;
    JTextArea textArea;
    JTextField textField;
    JButton button;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    public void go(){
        textArea = new JTextArea(100,100);
        textField = new JTextField(50);
        button = new JButton("Send");

        JScrollPane qScroller = new JScrollPane(textArea);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(qScroller);
        panel.add(textField);
        panel.add(button);
        frame.add(panel);

        button.addActionListener(new buttonSendListener());

        networking();

        Thread t1 = new Thread(new Task()); //create stream which get message from server
        t1.start();

        frame.setSize(500,500);
        frame.setVisible(true);

    }

    public void networking(){
        try {
            System.out.println("ip server: "+InetAddress.getByName("prog-01").getHostAddress());
            socket = new Socket(InetAddress.getByName("prog-01").getHostAddress(),50000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println("connection to server successfully");
        } catch (Exception e) {
            e.toString();
            //e.printStackTrace();
        }
    }

    public class Task implements Runnable{
        @Override
        public void run() {
           String message;

            try {
                while ((message = reader.readLine())!=null){
                    textArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class buttonSendListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            //create output stream ...
            String namePC;
            try {
                namePC = InetAddress.getLocalHost().toString();
            } catch (UnknownHostException e1) {
                namePC = "some_name";
                e1.printStackTrace();
            }

            String message;
            message = textField.getText();
            writer.println(namePC + ": " + message);

        }
    }


    public static void main(String[] args) {
        new SimpleClient().go();
    }
}
