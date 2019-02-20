package cs455.overlay.node;

import cs455.overlay.transport.EventQueueThread;
import cs455.overlay.transport.TCPRegularSocket;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.util.OverlayCreator;
import cs455.overlay.wireformats.EventFactory;
import cs455.overlay.wireformats.EventInstance;
import cs455.overlay.wireformats.LinkInfo;
import cs455.overlay.wireformats.LinkWeights;
import cs455.overlay.wireformats.MessagingNodeList;
import cs455.overlay.wireformats.TaskInitiate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public final class Registry implements Node {

  private EventFactory eventFactory_instance;

  private TCPServerThread server;
  private static EventQueueThread event_queue;

  private static Map<String, TCPRegularSocket> connections; //This is the regular socket pairing
  private static Map<String, String> ServerToRegular; //The broadcasted IP:port paring to the regular socket for above
  private static ArrayList<String> connections_list; //This contaings the servers IPs that are present
  private static HashMap<String, ArrayList<LinkInfo>> connectionsWeights;
  private OverlayCreator overlay;

  public Registry(int port_number) throws IOException {
    server = new TCPServerThread(port_number);
    event_queue = new EventQueueThread();

    eventFactory_instance = EventFactory.getInstance();
    eventFactory_instance.addListener(this);

    connections = new HashMap<>();
    ServerToRegular = new HashMap<>();
    connections_list = new ArrayList<>();
    connectionsWeights = new HashMap<>();
  }

  //This is responsible for spinning up the threads that are used within the registry
  private void startup() {
    new Thread(this.server).start();
    new Thread(this.event_queue).start();
  }

  public static void main(String[] args) {

    Registry registry = null;
    int registry_portnumber;

    if (args.length != 1) {
      System.err.println("ERROR: Recieved " + args.length + " args. Expected 1");
      System.err.println("Usage: cs455.overlay.node.Registry [portnumber]");
    }

    registry_portnumber = Integer.parseInt(args[0]);

    try {
      registry = new Registry(registry_portnumber);
      registry.startup();
      Scanner scnr = new Scanner(System.in);
      String input;
      String[] input_split;

      while (true) {
        input = scnr.nextLine();
        input_split = input.split(" ");  //Check to make sure this means
        //"setup-etc 9" evals. ["setup-etc", "9"]
        if (input_split[0].equals("list-messaging-nodes")) {
          registry.listMessagingNodes();
        } else if (input_split[0].equals("list-weights")) {
          registry.listWeights();
        } else if (input_split[0].equals("setup-overlay")) {
          if (input_split.length == 2) {
            //TODO make an assertion about what needs to happen
            registry.setupOverlay(Integer.parseInt(input_split[1]));
          }
        } else if (input_split[0].equals("send-overlay-link-weights")) {
          registry.sendOverlayLinkWeights();
        } else if (input_split[0].equals("start")) {
          registry.startNumRound(0);  //TODO change this to something else
        } else if (input_split[0].equals("exit")) {
          System.exit(0);
        } else if (input_split[0].equals("help")) {
          System.out.println("Here are your possible commands: ");
          System.out.println("list-messaging-nodes");
          System.out.println("list-weights");
          System.out.println("setup-overlay [number of connections]");
          System.out.println("send-overlay-link-weights");
          System.out.println("start");
          System.out.println("exit");
          System.out.println("\n\n");
        } else {
          System.err.println("Please enter in a valid command");
        }
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  //onEvent should add stuff to the event queue so that the queue can process the events in a seperate thread
  public void onEvent(EventInstance event) {
    try {
      event_queue.addEvent(event);
    } catch (InterruptedException ie) {
      System.err.println(ie.getMessage());
    }
  }

  //TODO this is where a null pointer exception is happening and I believe that has something to do with all of the
  //origin type protocol that I tried to implement
  //This adds the TCPSocket to the connections arrayList and the HashMap
  public static synchronized void receivedConnection(TCPRegularSocket inc_connection) {
    //FIXME This is the null ptr excpetion
    String regSocketKey = inc_connection.getIPPort();
    connections.put(regSocketKey, inc_connection);
  }

  public static synchronized void removeConnection(String key){
    connections_list.remove(key);
    ServerToRegular.remove(key);
    System.out.println("Removed " + key + " from the connections table, size is now {" + connections_list.size() + "}.");
  }

  //The incoming key will be the message body of the Register Request
  public static boolean isMessagingNodePresent(String key) {
    if (ServerToRegular.containsKey(key)) {
      return true;
    }
    return false;
  }

  public static void addServerMapping(String serverIP, String regularIP) {
    ServerToRegular.put(serverIP, regularIP);
    connections_list.add(serverIP);
    System.out.println(
        "Registry successfully connected new node, number of connections is :" + connections_list
            .size());
  }

  public static TCPRegularSocket getTCPSocket(String socket_id) {
    String regular_id = ServerToRegular.get(socket_id);
    return connections.get(regular_id);
  }

  //  This is being used as a tool to test
  public static void printConnections() {
    System.out.println("\n\nCurrent Connections: ");
    for (int i = 0; i < connections_list.size(); i++) {
      System.out.println(i + ") " + connections_list.get(i));
    }
    System.out.println("END OF CONNECTIONS LIST\n\n");
  }

  //Lists all of the messaging nodes within the registry's connections
  public void listMessagingNodes() {

    int idx = 1;

    if (connections_list.size() == 0) {
      System.out.println("Currently there are no Messaging Nodes connected");
      return;
    }

    System.out.println("Current Messaging Nodes Registered: ");
    for (String IP_port : connections_list) {
      System.out.printf("Connection #%d: %s\n", idx, IP_port);
      idx += 1;
    }
    System.out.println(" ");
  }

  //Lists the main node and the connections that it has with the associated weights
  public void listWeights() {

    ArrayList<LinkInfo> links;
    int counter = 1;
    System.out.println("The weights for the nodes in the overlay: ");

    for (String key : connectionsWeights.keySet()) {
      links = connectionsWeights.get(key);
      System.out.printf("Connections for node %s\n", key);
      for (LinkInfo link : links) {
        System.out
            .printf("\tConnection #%d to %s with weight of %d\n", counter, link.getReceivingNode(),
                link.getConnectionWeight());
        counter += 1;
      }
      System.out.println(" ");
      counter = 1;
    }
  }

  //Establishes the setup of the overlay in the overlay obj
  public void setupOverlay(int num_connections) {

    ArrayList<String> peerConnections;
    TCPRegularSocket socket;
    MessagingNodeList msl;

    //Create the full overlay for the nodes
    //this comes back as a HashMap<String, ArrayList<String>> with the IP:Port server id and the peer nodes
    this.overlay = new OverlayCreator(connections_list, num_connections);

    for (String mainNode : this.overlay.getFullOverlay().keySet()) {

      peerConnections = this.overlay.getFullOverlay().get(mainNode);
      msl = new MessagingNodeList(peerConnections);
      socket = connections.get(ServerToRegular.get(mainNode));
      try {
        socket.getSender().sendData(msl.getBytes());
      } catch (IOException ie) {
        System.err.println("An IOException occurred at Registry ln 166 " + ie.getMessage());
      }
    }
  }

  //Sends the distributed weights to all of the nodes
  public void sendOverlayLinkWeights() {

    ArrayList<String> peerNodes;
    ArrayList<LinkInfo> peerNodesWeights; //FIXME this should only be of length k connections

    HashMap<String, ArrayList<LinkInfo>> overlayWeights = new HashMap<>();
    Random rand = new Random();

    int weight = 0;

    LinkWeights linkWeights;
    TCPRegularSocket socket;

    //PreInitialize that array lists have been mapped
    for (int i = 0; i < connections_list.size(); i++) {
      overlayWeights.put(connections_list.get(i), new ArrayList<>());
    }


    for (String mainNode : overlay.getFullOverlay().keySet()) {
      //Here we are going to iterate over all of the keys in the overlay map
      peerNodes = overlay.getFullOverlay().get(mainNode);
      peerNodesWeights = overlayWeights.get(mainNode);

      for (String connectingNode : peerNodes) {

        //A{B:8, C:2}
        //B{A:8, C:3}   translates to mainNode.put((RecievingNode, weight))
        //C{A:2, B:3}                 ReceivingNode.put(());

        weight = rand.nextInt(10) + 1;

        if (!containsNode(peerNodesWeights,
            connectingNode)) {//Conecting node doesn't exist within the connections list for the main node
          overlayWeights.get(mainNode).add(new LinkInfo(mainNode, connectingNode, weight));
          overlayWeights.get(connectingNode).add(new LinkInfo(connectingNode, mainNode, weight));
        }

      }
    }

    //Appropriately sets the weights and allows the Registry to create the routing cache
    connectionsWeights = (HashMap<String, ArrayList<LinkInfo>>) overlayWeights
        .clone(); //TODO double check if this is the correct data tstructure to use

    for (String key : overlayWeights.keySet()) {
      peerNodesWeights = overlayWeights.get(key);
      linkWeights = new LinkWeights(peerNodesWeights);

      //This is where i need to select the node that needs to be sending the message to
      socket = connections.get(ServerToRegular.get(key));
      try {
        socket.getSender().sendData(linkWeights.getBytes());
      } catch (IOException ie) {
        System.err.println("An IOException occurred at Registry ln 235 " + ie.getMessage());
      }

    }


  }

  //TODO I need to implement Dijkstra's algorithim and need to determine a way to dispurse the
  //Routing cache to all of the messaging nodes --> Idea is that this could also be a singleton instance that I could reference values from

  //FIXME this is the issue
  //This will be used to determine if a connection already exists within an arraylist by value of sinnkNode of the linkInfo
  public boolean containsNode(final ArrayList<LinkInfo> links, final String sinkNode) {
    return links.stream().filter(o -> o.getReceivingNode().equals(sinkNode)).findFirst()
        .isPresent();
  }

  public void startNumRound(int rounds) {
    System.out.println("Create start number of rounds");
      TaskInitiate ti = new TaskInitiate(0);

    //TODO Lets verify that we can send messages before we even start thinking\
    for(String serverIP : connections_list){
        TCPRegularSocket socket = Registry.getTCPSocket(serverIP);
        try {
            socket.getSender().sendData(ti.getBytes());
        }catch(IOException ie){
            System.err.println("Had an issue with sending a task initiate message: " + ie.getMessage());
        }
    }
  }
}
