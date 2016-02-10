package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Sender {
    public char[] sendTCP(String msg, String ip, int port){
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(ip, port);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            out.println(msg);
            char[] buf=new char[1000];
            in.read(buf);
            out.close();
            in.close();
            echoSocket.close();
            return buf;
        } catch (Exception e) {
            System.err.println("Error Connecting to: IP- " + ip+" PORT: "+port);
            Configuration.removeNeighbor(ip, port);
        }
        return null;
    }

    public void sendUDP(String msg, String ip, int port){
        try{
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(ip);

            byte[] sendData = new byte[1000];

            sendData = msg.getBytes();
            DatagramPacket sendPacket =new DatagramPacket(sendData, sendData.length, IPAddress, port);

            clientSocket.send(sendPacket);
        }catch(Exception e){
            System.err.println("Error Connecting to: IP- " + ip+" PORT: "+port);
            Configuration.removeNeighbor(ip, port);
        }
    }

}