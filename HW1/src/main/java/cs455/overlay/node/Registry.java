package cs455.overlay.node;

import cs455.overlay.transport.EventQueueThread;
import cs455.overlay.transport.TCPRegularSocket;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.util.OverlayCreator;
import cs455.overlay.wireformats.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class Registry implements Node {


    private TCPServerThread server;
    private static EventQueueThread event_queue;

    private EventFactory eventFactory_instance;

    private static Map<String, TCPRegularSocket> connections; //This is the regular socket pairing
    private static Map<String, String> ServerToRegular; //The broadcasted IP:port paring to the regular socket for above
    private static ArrayList<String> connections_list; //This contaings the servers IPs that are present

    /**
     * In this section Im going to need some stuff set up for the registry with the overlay
     * I will also need a table of all of the weights
     * Something else TODO look into this
     * */

    private OverlayCreator overlay;

    public Registry(int port_number) throws IOException {
        server = new TCPServerThread(port_number);
        event_queue = new EventQueueThread();

        eventFactory_instance = EventFactory.getInstance();
        eventFactory_instance.addListener(this);

        connections = new HashMap<>();
        ServerToRegular = new HashMap<>();
        connections_list = new ArrayList<>();

        System.out.println("The IP address for the registry server socket is: " + server.getIP() + ":" + server.getPortnumber());
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

            while(true){
                input = scnr.nextLine();
                input_split = input.split(" ");  //Check to make sure this means
                //"setup-etc 9" evals. ["setup-etc", "9"]
                if(input_split[0].equals("list-messaging-nodes")){
                    registry.listMessagingNodes();
                }else if(input_split[0].equals("list-weights")){
                    registry.listWeights();
                }else if(input_split[0].equals("setup-overlay") ){
                    registry.setupOverlay(Integer.parseInt(input_split[1]));
                }else if(input_split[0].equals("send-overlay-link-weights")){
                    registry.sendOverlayLinkWeights();
                }else if(input_split[0].equals("start")){
                    registry.startNumRound(0);
                }else{
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
        }catch(InterruptedException ie){
            System.err.println(ie.getMessage());
        }
    }

  //This adds the TCPSocket to the connections arrayList and the HashMap
  public static synchronized void receivedConnection(TCPRegularSocket inc_connection){
    String regSocketKey = inc_connection.getIPPort();
    connections.put(regSocketKey, inc_connection);
  }

    //The incoming key will be the message body of the Register Request
    public static boolean isMessagingNodePresent(String key){
        if(ServerToRegular.containsKey(key)){
            return true;
        }
        return false;
    }

    public static void addServerMapping(String serverIP, String regularIP){
        ServerToRegular.put(serverIP, regularIP);
        connections_list.add(serverIP);
        System.out.println("Registry successfully connected new node, number of connections is :" + connections_list.size());
    }

    public static TCPRegularSocket getTCPSocket(String socket_id){
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

    public void listMessagingNodes(){
        System.out.println("Create the list messaging nodes fn");
    }

    public void listWeights() {System.out.println("Create the list weights fn");}

    //TODO finish this
    public void setupOverlay(int num_connections){

      ArrayList<String> peerConnections;
      TCPRegularSocket socket;
      MessagingNodeList msl;

      //Create the full overlay for the nodes
      //this comes back as a HashMap<String, ArrayList<String>> with the IP:Port server id and the peer nodes
      this.overlay = new OverlayCreator(connections_list, num_connections);

      for (String mainNode : this.overlay.getFullOverlay().keySet()) {

        peerConnections = this.overlay.getFullOverlay().get(mainNode);
        msl = new MessagingNodeList(peerConnections);
        socket = this.connections.get(this.ServerToRegular.get(mainNode));
        try {
          socket.getSender().sendData(msl.getBytes());
        }catch(IOException ie){
          System.err.println("An IOException occurred at Registry ln 166 " + ie.getMessage());
        }
      }
    }

    public void sendOverlayLinkWeights(){System.out.println("Create sendoverlayLinkWeights fn");}

    public void startNumRound(int rounds){System.out.println("Create start number of rounds");}
}
