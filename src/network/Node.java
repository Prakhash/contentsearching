package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ruba on 1/8/2016.
 */
public class Node {

    public MessageTransfer myMsgTransfer;
    public Message lastMessage = new SERMessage("",0,"",0);

    public Node(String args[]){
        boolean configurationSuccessFull = Configuration.setConfiguration(args);
        if(!configurationSuccessFull){
            System.out.println("ERROR IN ARGUMENTS...");
            System.out.println("Argument Format: BS_IP BS_PORT NODE_NAME NODE_IP NODE_PORT");
            System.exit(0);
        }
        myMsgTransfer = new MessageTransfer(this);

    }

    public static void main(String args[]){

        Node myNetwork = new Node(args);
        myNetwork.run();



    }
    public void run(){

        this.registerServer();
        BufferedReader user_input = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            try {
                String cmd=user_input.readLine();
                if(cmd.equals("exit")) {

                    this.leaveDSSystem();
                    this.unregisterServer();
                    System.exit(1);
                }
                else if(cmd.equals("print files")) {this.showLocalFiles();}
                else if(cmd.equals("print neighbors")) {this.printConnectedNeighbors();}
                else if(cmd.equals("print backupneighbors")) {this.printBackUpNeighbors();}
                else if(cmd.length()> 7 && cmd.substring(0,6).equals("search")) this.searchFile(cmd.substring(7));
                else System.out.println(">>Unknown command");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void printConnectedNeighbors(){

        System.out.println("Connected Neighbors:");

        List<Neighbor> neighbors = Configuration.getNeighbors();

        Iterator<Neighbor> neighborsIterator = neighbors.iterator();
        while (neighborsIterator.hasNext()) {
            Neighbor temp = neighborsIterator.next();


            System.out.println(temp.toString());

        }

    }

    public void printBackUpNeighbors(){

        System.out.println("Backup Neighbors:");

        List<Neighbor> neighbors = Configuration.getBackUpNeighbors();

        Iterator<Neighbor> neighborsIterator = neighbors.iterator();
        while (neighborsIterator.hasNext()) {
            Neighbor temp = neighborsIterator.next();


            System.out.println(temp.toString());

        }

    }

    public void onMessageReceived(Message message){

        Message newMessage;

        switch (message.msgType){
            case REGOK:
                //System.out.println(message.toString());
                setNeighbours(message);
                break;
            case UNROK:
                //System.out.println(message.toString());
                break;
            case LEAVEOK:
                //System.out.println(message.toString());
                break;
            case JOINOK:
                //System.out.println(message.toString());
                break;
            case SER:
                sendSEROKMsg(message);
                break;
            case SEROK:
                showContainedFiles(message);
                break;
            case LEAVE:
                //System.out.println(message.toString());
                Configuration.removeNeighbor(message.ip_to, message.port_to);
                this.checkForZeroNeighbors();
                break;
            case JOIN:
                Configuration.setNeighbor(message.ip_to, message.port_to);
                newMessage = new JOINOKMessage(message.ip_to, message.port_to);
                myMsgTransfer.sendMessage(newMessage);
                break;

        }
    }


    public void checkForZeroNeighbors(){


        if(Configuration.getNeighbors().size()==0){
            if(Configuration.getBackUpNeighbors().size()>0){
                List<Neighbor> neighbors = Configuration.getBackUpNeighbors();
                Configuration.setNeighbor(neighbors.get(0).getIpAddress(), neighbors.get(0).getPortNumber());
            }else{
                System.out.println("This node has no connections.");
            }

        }
    }

    public void showContainedFiles(Message message){

        if(message.noFiles > 0){
            System.out.println("IP: "+message.ip_from+" PORT: "+message.port_from+" replied with files: Hop count:"+message.hops);
            Configuration.setBackUpNeighbor(message.ip_from, message.port_from);
            String[] names = message.message.split("\"");
            for(int x=0; x<message.noFiles; x++){

                System.out.println(names[x+1]);
                Configuration.addFile(names[x+1]);
            }
        }else{
            System.out.println("IP: " + message.ip_from + " PORT: " + message.port_from + " replied with no files.");
        }
    }

    public void setNeighbours(Message message){

        String msg = message.message;
        String[] msg_data = msg.split(" ");
        int neigh_count = Integer.parseInt(msg_data[2]);
        if(neigh_count == 9999 || neigh_count == 9998 || neigh_count == 9997 || neigh_count == 9996){

            System.out.println("Registration failed..!!!");
        }
        if(neigh_count == 1){

            Configuration.setNeighbor(msg_data[3], Integer.parseInt(msg_data[4]));
            joinWithNeighbor(msg_data[3], Integer.parseInt(msg_data[4]));

        }else if(neigh_count == 2){

            Configuration.setNeighbor(msg_data[3], Integer.parseInt(msg_data[4]));
            Configuration.setNeighbor(msg_data[6], Integer.parseInt(msg_data[7]));

            joinWithNeighbor(msg_data[3], Integer.parseInt(msg_data[4]));
            joinWithNeighbor(msg_data[6], Integer.parseInt(msg_data[7]));

        }else if(neigh_count > 2){

            int rand=(int) Math.floor(Math.random()*neigh_count);

            Configuration.setNeighbor(msg_data[3*rand+3], Integer.parseInt(msg_data[3*rand+4]));
            int neigh_port_1 = Integer.parseInt(msg_data[3*rand+4]), neigh_port_2;
            String neigh_ip_1 = msg_data[3*rand+3], neigh_ip_2;

            joinWithNeighbor(neigh_ip_1, neigh_port_1);

            while(true){
                rand=(int) Math.floor(Math.random()*neigh_count);
                neigh_ip_2 = msg_data[3 * rand + 3];
                neigh_port_2 = Integer.parseInt(msg_data[3 * rand + 4]);

                if(neigh_ip_1.equals(neigh_ip_2) && neigh_port_1==neigh_port_2) continue;
                else {
                    Configuration.setNeighbor(neigh_ip_2, neigh_port_2);
                    joinWithNeighbor(neigh_ip_2, neigh_port_2);
                    break;
                }
            }
            for(int x=0; x<neigh_count;x++){
                Configuration.setBackUpNeighbor(msg_data[3 * x + 3], Integer.parseInt(msg_data[3 * x + 4]));
            }

        }

    }



    public void joinWithNeighbor(String ip, int port){

        Message newMessage = new JOINMessage(ip, port);
        myMsgTransfer.sendMessage(newMessage);


    }

    public ArrayList<String> searchQueryInLocal(String query){

        ArrayList<String> files = Configuration.getMyFiles();
        ArrayList<String> output = new ArrayList<String>();

        String lowercase = query.toLowerCase();
        Iterator<String> fileIterator = files.iterator();
        while (fileIterator.hasNext()) {
            String temp = fileIterator.next();
            String lowercase2 = temp.toLowerCase();
            if(lowercase2.contains(lowercase)){
                output.add(temp);
            }
        }

        return output;
    }

    public void sendSEROKMsg(Message message){


        if(!lastMessage.isequal(message)){
            lastMessage = message;
            System.out.println("Searching file locally.");
            ArrayList<String> files = this.searchQueryInLocal(message.query);
            Configuration.setBackUpNeighbor(message.ip_from, message.port_from);
            if(files.size() > 0){
                System.out.println("Files found for query: "+message.query+", Sending reply...");
                Message serokMsg = new SEROKMessage(files, message.hops,message.ip_from, message.port_from);
                myMsgTransfer.sendMessage(serokMsg);
            }else{
                System.out.println("Files not found for query: "+message.query+", forwarding query...");
                System.out.println("Searching file globally.");
                forwardSerMsg(message);
            }

        }


    }

    public void searchFile(String filename){

        Message message = new SERMessage(filename, 0, Configuration.getMyIpAddress(), Configuration.getMyPortNumber());
        lastMessage = message;
        System.out.println("Searching file locally.");
        ArrayList<String> files = this.searchQueryInLocal(message.query);
        if(files.size() > 0){
            System.out.println("Locally found files:");
            Iterator<String> fileIterator = files.iterator();
            while (fileIterator.hasNext()) {
                String temp = fileIterator.next();
                System.out.println(temp);
            }

        }else{
            System.out.println("Searching file globally.");
            forwardSerMsg(message);
        }


    }

    public void forwardSerMsg(Message message){

        List<Neighbor> neighbors = Configuration.getNeighbors();
        Iterator<Neighbor> neighborsIterator = neighbors.iterator();
        while (neighborsIterator.hasNext()) {
            Neighbor temp = neighborsIterator.next();
            Message serMsg = new SERMessage(message.query, message.hops +1, message.ip_from, message.port_from, temp.getIpAddress(), temp.getPortNumber());
            myMsgTransfer.sendMessage(serMsg);
        }

    }

    public void unregisterServer(){
        Message unregMsg = new UNREGMessage();
        myMsgTransfer.sendMessage(unregMsg);
    }
    public void registerServer(){
        Message message = new REGMessage();
        myMsgTransfer.sendMessage(message);
    }

    public void leaveDSSystem(){
        List<Neighbor> neighbors = Configuration.getNeighbors();
        Iterator<Neighbor> neighborsIterator = neighbors.iterator();
        while (neighborsIterator.hasNext()) {
            Neighbor temp = neighborsIterator.next();
            Message leaveMsg = new LEAVEMessage(temp.getIpAddress(), temp.getPortNumber());
            myMsgTransfer.sendMessage(leaveMsg);
        }
    }
    public void showLocalFiles(){

        System.out.println("Local files:");
        ArrayList<String> files = Configuration.getMyFiles();

        Iterator<String> fileIterator = files.iterator();
        while (fileIterator.hasNext()) {
            String temp = fileIterator.next();

            System.out.println(temp);
        }

    }


}
