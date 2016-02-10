package network;

import java.io.IOException;

/**
 * Created by hiran on 1/10/16.
 */
public class MessageTransfer {

    Receiver messageReceiver;
    Sender messageSender;
    Node myNode;

    public MessageTransfer(Node myNode){

        this.myNode = myNode;
        try{
            messageReceiver = new Receiver(Configuration.getMyPortNumber(),this);
            messageReceiver.start();
        }catch(IOException ex){

        }
        messageSender = new Sender();

    }

    public void sendMessage(Message message){

            if(message.msgType == MessageType.REG || message.msgType == MessageType.UNREG){

                //System.out.print(message.ip_to +" "+message.port_to);
                char[] respond = messageSender.sendTCP(message.toString(), message.ip_to, message.port_to);
                String msg = new String(respond);

                this.translateMessage(msg.trim());

            }else{
                messageSender.sendUDP(message.toString(), message.ip_to, message.port_to);
            }




    }

    public void translateMessage(String msg){


        String[] msg_data = msg.split(" ");

        Message translatedMsg;

        if(msg_data[1].equals("REGOK")){

            System.out.println("RECEIVED MESSAGE: "+ msg);

            translatedMsg = new REGOKMessage(msg);
            myNode.onMessageReceived(translatedMsg);

        }else if(msg_data[1].equals("UNROK")){

            System.out.println("RECEIVED MESSAGE: "+ msg);

            translatedMsg = new UNREGOKMessage(msg);
            myNode.onMessageReceived(translatedMsg);

        }else if(msg_data[1].equals("JOINOK")){



            translatedMsg = new JOINOKMessage(msg);
            myNode.onMessageReceived(translatedMsg);

        }else if(msg_data[1].equals("LEAVEOK")){

            translatedMsg = new LEAVEOKMessage(msg);
            myNode.onMessageReceived(translatedMsg);

        }else if(msg_data[1].equals("JOIN")){



            translatedMsg = new JOINMessage(msg_data[2], Integer.parseInt(msg_data[3]));
            System.out.println("JOIN message received: "+ translatedMsg.ip_to+":"+translatedMsg.port_to);
            myNode.onMessageReceived(translatedMsg);

        }else if(msg_data[1].equals("SER")){

            String[] tempstr = msg.split("\"");
            int finalInt = msg_data.length;

            translatedMsg = new SERMessage(tempstr[1], Integer.parseInt(msg_data[finalInt-1]), msg_data[2], Integer.parseInt(msg_data[3]));
            System.out.println("SEARCH message received: "+ translatedMsg.ip_from+":"+translatedMsg.port_from+" query: "+translatedMsg.query);
            myNode.onMessageReceived(translatedMsg);

        }else if(msg_data[1].equals("LEAVE")){

            translatedMsg = new LEAVEMessage(msg_data[2], Integer.parseInt(msg_data[3]));
            System.out.println("LEAVE message received: "+ translatedMsg.ip_to+":"+translatedMsg.port_to);
            myNode.onMessageReceived(translatedMsg);

        }else if(msg_data[1].equals("SEROK")){

            translatedMsg = new SEROKMessage(msg);
            myNode.onMessageReceived(translatedMsg);

        }else{
            System.out.println("RECEIVED MESSAGE: " + msg);
        }

    }
}
enum MessageType { REG, REGOK, UNROK, UNREG, JOINOK , JOIN, LEAVE, LEAVEOK, SER, SEROK};
