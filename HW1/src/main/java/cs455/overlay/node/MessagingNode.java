package cs455.overlay.node;

import cs455.overlay.wireformats.*;
import java.io.IOException;
import java.net.*;
import cs455.overlay.transport.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//TODO refcator
public final class MessagingNode implements Node{

  private EventFactory eventFactory_instance;
  private String ipAddr;
  private int portnumber;
  private TCPServerThread server;
  private EventQueueThread eventQueue;
  private TCPRegularSocket registry_socket;
  private static ArrayList<String> connections_list;
  private static Map<String, TCPRegularSocket> connections; //Defined as static so that the
  //Other classes especially the EventQueue can access the critical info

  public MessagingNode(String server_hostname, int server_portnumber) throws IOException {

    this.server = new TCPServerThread();
    this.ipAddr = InetAddress.getLocalHost().getHostAddress();
    this.portnumber = this.server.getPortnumber();

    this.eventQueue = new EventQueueThread();

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

  public static void addConnection(String key, TCPRegularSocket socket){
    connections_list.add(key);
    connections.put(key, socket);
  }

  public static void removeConnection(String key){
    connections_list.remove(key);
    TCPRegularSocket socket = connections.remove(key);
    socket.kilSocket();
  }

  public String getIP(){return this.ipAddr;}

  public int getPortnumber(){return this.portnumber;}

  public static ArrayList<String> getConnectionsList(){return connections_list;}

  public static Map<String, TCPRegularSocket> getConnections(){return connections;}

  public String getIpAddr(){
    return this.ipAddr;
  }

  private void startup() throws IOException{
    new Thread(this.server).start();
    new Thread(this.registry_socket.getReceiverThread()).start();
    new Thread(this.eventQueue).start();
    Event bootupEvent = new RegisterRequest(this.ipAddr, this.portnumber);
    this.registry_socket.getSender().sendData(bootupEvent.getBytes());
  }

  public void onEvent(Event e){
    try {
      this.eventQueue.addEvent(e);
    }catch(InterruptedException ie){
      System.err.println(ie.getMessage());
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
      Scanner scnr = new Scanner(System.in);
      String input;
      String[] input_split;

      while(true){
        input = scnr.nextLine();
        input_split = input.split(" ");  //Check to make sure this means
        //"setup-etc 9" evals. ["setup-etc", "9"]
        if(input_split[0].equals("print-shortest-path")){
          node.printShortestPath();
        }else if(input_split[0].equals("exit-overlay")){
          node.exitOverlay();
        }else {
          System.err.println("Please enter in a valid command");
        }
      }

    }catch(UnknownHostException uhe){
      System.err.println("An unknown host exception occured in MessagingNode.java!!!\n" + uhe.getMessage());
      System.exit(-1);
    }catch(IOException ioe){
      System.err.println("An IOexception occured in MessagingNode.java!!!\n" + ioe.getMessage());
      System.exit(-1);
    }

    System.exit(0);
  }

  public void printShortestPath(){System.out.println("Implement print shortest path");}

  public void exitOverlay(){
    System.out.println("Implement exit overlay");
    TCPRegularSocket registry = MessagingNode.getConnections().get("REGISTRY");
    try {
      registry.getSender().sendData(new DeregisterRequest(this.ipAddr, this.portnumber).getBytes());
    }catch(IOException ie){
      System.err.println(ie.getMessage());
    }
  }

}
