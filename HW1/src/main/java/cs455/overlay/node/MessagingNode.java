package cs455.overlay.node;

import cs455.overlay.transport.EventQueueThread;
import cs455.overlay.transport.TCPRegularSocket;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.util.StatisticsCollectorAndDisplay;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.EventFactory;
import cs455.overlay.wireformats.EventInstance;
import cs455.overlay.wireformats.LinkInfo;
import cs455.overlay.wireformats.Protocol;
import cs455.overlay.wireformats.RegisterRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public final class MessagingNode implements Node{

  private EventFactory eventFactory_instance;
  private String ipAddr;
  private int portnumber;

  private TCPRegularSocket registry_socket;

  private static EventQueueThread eventQueue;
  private static TCPServerThread server;

  //TODO I could add a thing here for actual connections
  //TODO and here would be the ones that where already conneceted due to the order of messages sent
  /**
   * Message type: Protocol.LinkWeights Number of links: connections.length() -1 because of the
   * registry entry LinkInfo1:  this.IP:this.Port -> connections[i] linkWeight
   *
   * Main node will have a map of the adjacent node and its weight This way the knowledge is only of
   * the neighbor nodes When it comes to sending rounds it will only be that of a neighboring node
   * due to the algorithm
   */

  private static ArrayList<LinkInfo> connectionsWeights;
  private static HashMap<String, TCPRegularSocket> connections; //This is the regular socket pairing
  private static HashMap<String, String> ServerToRegular; //The broadcasted IP:port paring to the regular socket for above
  private static ArrayList<String> connections_list; //This contaings the servers IPs that are present

  private static StatisticsCollectorAndDisplay statsCollector;

  public MessagingNode(String server_hostname, int server_portnumber) throws IOException {

    //The information derives from the server sockets IP and port
    server = new TCPServerThread();

    this.ipAddr = InetAddress.getLocalHost().getHostAddress();
    this.portnumber = server.getPortnumber();

    eventQueue = new EventQueueThread();

    String server_ip = InetAddress.getByName(server_hostname).getHostAddress();

    this.registry_socket = new TCPRegularSocket(new Socket(server_ip, server_portnumber));
    connections = new HashMap<String, TCPRegularSocket>();
    connections.put(server_ip + ":" + server_portnumber, this.registry_socket);

    ServerToRegular = new HashMap<>();
    connections_list = new ArrayList<>();

    connections_list.add(server_ip + ":" + server_portnumber);

    this.eventFactory_instance = EventFactory.getInstance();
    eventFactory_instance.addListener(
        this); //This should correctly add the Messaging node to listen to the eventfactorys events

    statsCollector = new StatisticsCollectorAndDisplay();

    connectionsWeights = new ArrayList<>();
  }

  //This adds the TCPSocket to the connections arrayList and the HashMap

  public static synchronized void receivedConnection(TCPRegularSocket inc_connection) {
    String regSocketKey = inc_connection.getIPPort();
    connections.put(regSocketKey, inc_connection);
  }
  //The incoming key will be the message body of the Register Request

  public static boolean isMessagingNodePresent(String key) {
    //TODO this is my issue with the exception
    try {
      if (ServerToRegular.containsKey(key)) {
        return true;
      }
      return false;
    } catch (NullPointerException ne) {
      return false;
    }
  }

  public static void addServerMapping(String serverIP, String regularIP) {
    ServerToRegular.put(serverIP, regularIP);
    connections_list.add(serverIP);
    System.out.println(
        "Registry successfully connected new node, number of peer nodes is :" + connections_list
            .size());

    printConnections();
  }

  public static TCPRegularSocket getTCPSocket(String socket_id) {
    String regular_id = ServerToRegular.get(socket_id);
    return connections.get(regular_id);
  }
  //This is being used as a tool to test

  public static void printConnections() {
    System.out.println("\n\nCurrent Connections: ");
    for (int i = 0; i < connections_list.size(); i++) {
      System.out.println(i + ") " + connections_list.get(i));
    }
    System.out.println("END OF CONNECTIONS LIST\n\n");
  }

  public String getIP(){return this.ipAddr;}

  public int getPortnumber(){return this.portnumber;}

  public static EventQueueThread getEventQueue(){return eventQueue;}

  public static TCPServerThread getServer(){return server;}

  public static void setPeerWeights(ArrayList<LinkInfo> connections) {
    connectionsWeights = (ArrayList<LinkInfo>) connections.clone();
  }

  //This is responsible for starting up the associated threads with the node
  private void startup() throws IOException{
    new Thread(server).start();
    new Thread(this.registry_socket.getReceiverThread()).start();
    new Thread(eventQueue).start();
    Event bootupEvent = new RegisterRequest(this.ipAddr, this.portnumber, Protocol.registry);
    this.registry_socket.getSender().sendData(bootupEvent.getBytes());
  }

  public void onEvent(EventInstance e){
    try {
      eventQueue.addEvent(e);
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
      System.err.println(
          "An IOexception occured in MessagingNode.java!!! ln170\n" + ioe.getMessage() + ioe
              .getLocalizedMessage());
      System.exit(-1);
    }

    System.exit(0);
  }

  public void printShortestPath(){System.out.println("Implement print shortest path");}

  //TODO this is something that should be focused on I might be neglecting a lot of issues if i dont finish this
  public void exitOverlay(){
    System.out.println("Implement exit overlay");
  }

  //Note that this function is strictly for testing purposes
  public static void printConnectionWeights() {
    for (LinkInfo link : connectionsWeights) {
      System.out.printf("MainNode: %s Connected to: %s Weight; %d\n", link.getSendingNode(),
          link.getReceivingNode(), link.getConnectionWeight());
    }
  }

}
