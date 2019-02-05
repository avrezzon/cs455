package cs455.overlay.node;

import cs455.overlay.wireformats.*;
import java.io.IOException;
import java.net.*;
import cs455.overlay.transport.*;

public class MessagingNode implements Node{

  private EventFactory eventFactory_instance;
  private String ipAddr;
  private TCPServerThread server; //This will spawn at runtime to accept messages from other nodes
  private TCPSender sender;
  private TCPReceiverThread receiver;
  //Data structure for the connections table


  public MessagingNode(String hostname, int port_number) throws UnknownHostException , IOException {
    this.ipAddr = InetAddress.getLocalHost().toString();
    this.server = new TCPServerThread(0); //TODO create constructor
    this.sender = new TCPSender(new Socket(hostname, port_number));
    this.receiver = new TCPReceiverThread(new Socket(hostname, port_number));
    this.eventFactory_instance = EventFactory.getInstance();
    eventFactory_instance.addListener(this); //This should correctly add the Messaging node to listen to the eventfactorys events
  }

  public String getIpAddr(){
    return this.ipAddr;
  }

  public TCPSender getSenderSocket(){
    return this.sender;
  }

  public void onEvent(Event e){
    switch (e.getType()){
      case Protocol.REGISTER_RQ:
        break;
      case Protocol.REGISTER_RS:
        break;
      case Protocol.DEREGISTER_RQ:
        break;
      case Protocol.DEREGISTER_RS:
        break;
      default:
        System.err.println("RECEIVED AN EVENT OF TYPE " + e.getType());
        break;
    }
  }

  //TODO verify the input paramaters
  public static void main(String[] args){

    String hostname = "RezzLinux"; // default values that will need to be changed
    int port_number = 8088;
    MessagingNode node = null;

    try {
      node = new MessagingNode(hostname, port_number);
      new Thread(node.receiver).run();
      new Thread(node.server).run();

      //TODO run a mock connection to see if i can transport data
      //Event e = new RegisterRequest(node.ipAddr,8088);
      //node.sender.sendData(e.getBytes());

    }catch(UnknownHostException uhe){
      System.err.println("An unknown host exception occured in MessagingNode.java!!!\n" + uhe.getMessage());
      System.exit(-1);
    }catch(IOException ioe){
      System.err.println("An IOexception occured in MessagingNode.java!!!\n" + ioe.getMessage());
      System.exit(-1);
    }



  }
}
