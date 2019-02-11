package cs455.overlay.node;

import cs455.overlay.wireformats.*;
import java.io.IOException;
import java.net.*;
import cs455.overlay.transport.*;
import java.util.HashMap;
import java.util.Map;

//TODO refcator
public final class MessagingNode implements Node{

  private EventFactory eventFactory_instance;
  private String ipAddr;
  private int portnumber;
  private TCPServerThread server;
  private TCPRegularSocket registry_socket;
  public static Map<String, TCPRegularSocket> connections;

  public MessagingNode(String server_hostname, int server_portnumber) throws IOException {

    this.server = new TCPServerThread();
    this.ipAddr = InetAddress.getLocalHost().getHostAddress();
    this.portnumber = this.server.getPortnumber();

    String server_ip = InetAddress.getByName(server_hostname).getHostAddress();

    this.registry_socket = new TCPRegularSocket(new Socket(server_ip, server_portnumber));
    this.connections = new HashMap<String, TCPRegularSocket>();
    this.connections.put("REGISTRY", this.registry_socket);

    //The entries for the connections map will look like:
    //'REGISTRY' : registry_socket
    //'192.203.292.00:8088' : TCPRegularSocket

    this.eventFactory_instance = EventFactory.getInstance();
    eventFactory_instance.addListener(this); //This should correctly add the Messaging node to listen to the eventfactorys events
  }

  public String getIpAddr(){
    return this.ipAddr;
  }

  private void startup() throws IOException{
    new Thread(this.server).start();
    new Thread(this.registry_socket.getReceiverThread()).start();
    Event bootupEvent = new RegisterRequest(this.ipAddr, this.portnumber);
    this.registry_socket.getSender().sendData(bootupEvent.getBytes());
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
