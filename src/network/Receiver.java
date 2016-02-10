package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receiver extends Thread {
    DatagramSocket listener;
    MessageTransfer myMsgTransfer;


    public Receiver(int port, MessageTransfer myMsgTransfer) throws IOException{

        this.myMsgTransfer = myMsgTransfer;
        listener = new DatagramSocket(port);
        System.out.println("Listening to port: "+port);

    }

    public void run(){
        try {
            while (true) {

                byte[] receiveData = new byte[300];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                listener.receive(receivePacket);
                String msg = new String(receivePacket.getData());
                myMsgTransfer.translateMessage(msg.trim());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}