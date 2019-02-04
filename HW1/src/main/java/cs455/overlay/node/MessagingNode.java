package cs455.overlay.node;

import cs455.overlay.wireformats.*;
import java.io.IOException;
import java.net.*;
import cs455.overlay.transport.*;

public class MessagingNode implements Node{

  private String ipAddr;
  private TCPServerThread server; //This will spawn at runtime to accept messages from other nodes
  private TCPSender senderSocket; //This will be done to initate a message with the Registry
  //Data structure for the connections table


  public MessagingNode(String hostname, int port_number) throws UnknownHostException , IOException {
    this.ipAddr = InetAddress.getLocalHost().toString();
    this.server = new TCPServerThread(0); //TODO create constructor
    this.senderSocket = new TCPSender(new Socket(hostname, port_number));
  }

  public String getIpAddr(){
    return this.ipAddr;
  }

  public TCPSender getSenderSocket(){
    return this.senderSocket;
  }

  public void onEvent(Event e){

  }

  //TODO verify the input paramaters
  public static void main(String[] args){

    String hostname = ""; // default values that will need to be changed
    int port_number = 6003;
    MessagingNode node = null;

    try {
      node = new MessagingNode(hostname, port_number);

      //Create the TCPServer constructor
    }catch(UnknownHostException uhe){
      System.err.println("An unknown host exception occured in MessagingNode.java!!!\n" + uhe.getMessage());
      System.exit(-1);
    }catch(IOException ioe){
      System.err.println("An IOexception occured in MessagingNode.java!!!\n" + ioe.getMessage());
      System.exit(-1);
    }



  }
}
