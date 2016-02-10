package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by prakhash on 10/01/16.
 */
public class Socket {

    private DatagramSocket sock = null;
    private DatagramPacket incoming = null;

    private String connectionType = "UDP";

    private String inIP = "0";
    private int inPort = 0;

    public int init (int port) {
        try {
            // creating a server socket, parameter is local port number
            if (port == 0) {
                sock = new DatagramSocket();
            } else {
                sock = new DatagramSocket(port);
            }
            port = sock.getLocalPort();

            // buffer to receive incoming data
            byte[] buffer = new byte[65536];
            incoming = new DatagramPacket(buffer, buffer.length);

            // wait for an incoming data
            echo("Server socket created at " + port + ". Waiting for incoming data...");
        } catch (IOException e) {
            System.err.println("IOException " + e);
        }
        return port;
    }

    // simple function to echo data to terminal
    private static void echo(String msg) {
        System.out.println(msg);
    }


    public void send (String messsage, String ip, int port) {
        try {
            DatagramPacket dp2 = new DatagramPacket(messsage.getBytes(), messsage.getBytes().length, InetAddress.getByName(ip), port);
            sock.send(dp2);
        } catch (IOException e){
            System.err.println("IOException " + e);
        }
    }


    public String recieve () {
        String inMes = "ERROR";
        try {
            sock.receive(incoming);
            byte[] data = incoming.getData();
            inMes = new String(data, 0, incoming.getLength());

            // echo the details of incoming data - client ip : client port - client message
            echo(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + inMes);
            inIP = incoming.getAddress().getHostAddress();
            inPort = incoming.getPort();
        } catch (IOException e){
            System.err.println("IOException " + e);
        }
        return inMes;
    }
}