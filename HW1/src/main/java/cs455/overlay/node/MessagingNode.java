package cs455.overlay.node;

import cs455.overlay.wireformats.*;
import java.io.IOException;
import java.net.*;
import cs455.overlay.transport.*;

//TODO refcator
public class MessagingNode implements Node{

  private EventFactory eventFactory_instance;
  private String ipAddr;
  private int portnumber;
  private TCPServerThread server; //This will spawn at runtime to accept messages from other nodes
  private TCPSender sender;
  private TCPReceiverThread receiver;
  //Data structure for the connections table


  public MessagingNode(String server_hostname, int server_portnumber) throws IOException {
    this.ipAddr = InetAddress.getLocalHost().getHostAddress();
    this.server = new TCPServerThread();
    this.portnumber = this.server.getPortnumber();

    this.sender = new TCPSender(new Socket(server_hostname, server_portnumber));
    this.receiver = new TCPReceiverThread(new Socket(server_hostname, server_portnumber));
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
        RegisterResponse rrs = (RegisterResponse) e;
        System.out.println("RECIEVED RESPONSE: " + rrs.getStatus());
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

  private void startup() throws IOException{
    new Thread(this.receiver).start();
    new Thread(this.server).start();
    Event bootupEvent = new RegisterRequest(this.ipAddr, this.portnumber);
    this.sender.sendData(bootupEvent.getBytes());
  }

  public static void main(String[] args){

    MessagingNode node;
    String registry_hostname;
    int registry_portnunmber;

    if(args.length != 2){
      System.err.println("Recieved " + args.length + " arguements.");
      System.err.println("Usage: cs455.overlay.node.MessagingNode [hostname] [portnumber]");
      System.exit(-1);
    }

    registry_hostname = args[0];
    registry_portnunmber = Integer.parseInt(args[1]);

    try {
      node = new MessagingNode(registry_hostname, registry_portnunmber);
      node.startup();

    }catch(UnknownHostException uhe){
      System.err.println("An unknown host exception occured in MessagingNode.java!!!\n" + uhe.getMessage());
      System.exit(-1);
    }catch(IOException ioe){
      System.err.println("An IOexception occured in MessagingNode.java!!!\n" + ioe.getMessage());
      System.exit(-1);
    }
  }
}
