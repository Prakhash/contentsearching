package network;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by hiran on 1/10/16.
 */
public class Message {
    public MessageType msgType;
    public String ip_from, ip_to;
    public int port_from, port_to;
    public String username;
    public String message;
    public String query;
    public int hops;
    public int noFiles = 0;

    public Message() {
        ip_from = Configuration.getMyIpAddress();
        port_from = Configuration.getMyPortNumber();
    }

    public String getMsgLength(String msg) {
        return String.format("%04d", msg.length() + 5);
    }

    public boolean isequal(Message message1){

        return true;
    }
}



class REGMessage extends Message {

    public REGMessage() {
        this.msgType = MessageType.REG;
        this.username = Configuration.getMyUserName();
        ip_to = Configuration.getServerIpAddress();
        port_to = Configuration.getServerPortNumber();
    }

    public String toString() {
        String temp = "REG " + ip_from + " " + port_from + " " + username;
        return getMsgLength(temp) +" "+ temp;
    }
}

class REGOKMessage extends Message {


    public REGOKMessage(String msg) {
        msgType = MessageType.REGOK;
        message = msg;


        ip_from = Configuration.getServerIpAddress();
        port_from = Configuration.getServerPortNumber();
    }
    public String toString() {
        String temp = "REGOK " + ip_from + " " + port_from;
        return getMsgLength(temp) +" "+ temp;
    }

}

class UNREGMessage extends Message {

    public UNREGMessage() {
        this.username = Configuration.getMyUserName();
        this.msgType = MessageType.UNREG;
        ip_to = Configuration.getServerIpAddress();
        port_to = Configuration.getServerPortNumber();
    }

    public String toString() {
        String temp = "UNREG " + ip_from + " " + port_from + " " + username;
        return getMsgLength(temp) + " " +temp;
    }
}

class UNREGOKMessage extends Message {
    public int status;

    public UNREGOKMessage(String msg) {
        msgType = MessageType.UNROK;
        String[] msg_data = msg.split(" ");
        status = Integer.parseInt(msg_data[2]);
    }

}

class JOINMessage extends Message{
    public JOINMessage(String ip, int port){
        this.msgType = MessageType.JOIN;
        this.ip_to = ip;
        this.port_to = port;
    }

    public String toString(){
        String temp="JOIN " + ip_from + " " + port_from;
        return getMsgLength(temp)+" "+temp;
    }
}

class JOINOKMessage extends Message{
    public int status;
    public JOINOKMessage(String ip, int port){
        msgType = MessageType.JOINOK;
        status = 0;
        ip_to = ip;
        port_to = port;
    }

    public JOINOKMessage(String msg){
        msgType = MessageType.JOINOK;
        String[] msg_data = msg.split(" ");
        status=Integer.parseInt(msg_data[2]);
    }
    public String toString(){
        String temp="JOINOK " + status;
        return getMsgLength(temp)+" "+temp;
    }

}

class LEAVEMessage extends Message{

    public LEAVEMessage(String ip, int port){
        msgType = MessageType.LEAVE;
        this.ip_to = ip;
        this.port_to = port;

    }
    public String toString(){
        String temp="LEAVE " + ip_from + " " + port_from;
        return getMsgLength(temp)+" "+temp;
    }
}

class LEAVEOKMessage extends Message{
    public int status;

    public LEAVEOKMessage(String msg){
        msgType = MessageType.LEAVEOK;
        String[] msg_data=msg.split(" ");
        status=Integer.parseInt(msg_data[2]);
    }
}

class SERMessage extends Message{

    public SERMessage(String query, int hops, String ipfrom, int portfrom, String ipto, int portto){
        this.msgType = MessageType.SER;
        this.query = query;
        this.hops = hops;
        this.ip_from = ipfrom;
        this.port_from = portfrom;
        this.ip_to = ipto;
        this.port_to = portto;
    }

    public SERMessage(String query, int hops, String ipfrom, int port){
        this.msgType = MessageType.SER;
        this.query = query;
        this.hops = hops;
        this.ip_from = ipfrom;
        this.port_from = port;
    }
    public String toString(){
        String temp="SER " + ip_from + " " + port_from +" \""+query+"\" "+ hops;
        return getMsgLength(temp)+" "+temp;
    }
    public boolean isequal(Message message1){

        if(this.query.equals(message1.query) && this.ip_from.equals(message1.ip_from) && this.port_from == message1.port_from){
            return true;
        }else{
            return false;
        }
    }
}

class SEROKMessage extends Message{

    public String filename = "";


    public SEROKMessage(ArrayList<String> files, int hops, String ip, int port){

        this.msgType = MessageType.SEROK;
        this.hops = hops;
        this.ip_to = ip;
        this.port_to = port;
        noFiles = files.size();

        Iterator<String> fileIterator = files.iterator();
        while (fileIterator.hasNext()) {
            filename = filename+" \""+fileIterator.next()+"\"";
        }



    }
    public SEROKMessage(String msg){
        this.msgType = MessageType.SEROK;
        String[] msg_data = msg.split(" ");
        //length SEROK no_files IP port hops filename1 filename2 ... ...
        this.ip_from = msg_data[3];
        this.port_from = Integer.parseInt(msg_data[4]);
        this.hops = Integer.parseInt(msg_data[5]);
        this.message = msg;
        this.noFiles = Integer.parseInt(msg_data[2]);


    }


    public String toString(){

        String temp="SEROK " +noFiles+" "+ ip_from + " " + port_from +" "+ this.hops + filename;
        return getMsgLength(temp)+" "+temp;
    }
}



