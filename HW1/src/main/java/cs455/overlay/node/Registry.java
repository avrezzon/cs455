package cs455.overlay.node;

import cs455.overlay.transport.EventQueueThread;
import cs455.overlay.transport.TCPRegularSocket;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.wireformats.*;

import java.io.IOException;
import java.util.Dictionary;

public class Registry implements Node {


    private TCPServerThread server;
    private EventQueueThread event_queue;
    private EventFactory eventFactory_instance;
    public static Dictionary<String, TCPRegularSocket> connections; //Defined as static so that the
    //Other classes especially the EventQueue can access the critical info

    public Registry(int port_number) throws IOException {
        server = new TCPServerThread(port_number);
        event_queue = new EventQueueThread();
        eventFactory_instance = EventFactory.getInstance();
        eventFactory_instance.addListener(this);
        connections = new Dictionary<String, TCPRegularSocket>();
    }

    //onEvent should add stuff to the event queue so that the queue can process the events in a seperate thread
    public void onEvent(Event event) {
        try {
            event_queue.addEvent(event);
        }catch(InterruptedException ie){
            System.err.println(ie.getMessage());
        }
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        //TODO this portion will be dedicated to receiving input from the console

    }
}
