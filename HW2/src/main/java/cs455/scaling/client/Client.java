package cs455.scaling.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

//When we run this class the command line args will look like
//java cs455.scaling.client.Client server-host server-port message-rate
public class Client {

  private int msgRate; //This will have a value between 2 - 4
  private SocketChannel server;
  private SenderThread senderThread;
  private static ByteBuffer buffer;

  private static LinkedList<String> sentMessages; //This is where all of the hashed messages that the sender sends
  private static volatile boolean shutdown; //Shuts down the sender thread once the server has closed down

  public static ClientStatistics StatsCollector;
  private ClientStatsThread stats;

  public Client(String host_name, int port_number, int messageRate) throws IOException {

    this.server = SocketChannel.open(new InetSocketAddress(host_name, port_number));
    this.msgRate = messageRate;
    this.senderThread = new SenderThread(this.server, messageRate);

    buffer = ByteBuffer.allocate(8000);
    shutdown = false;

    sentMessages = new LinkedList<>();
    StatsCollector = new ClientStatistics();
    stats = new ClientStatsThread();

  }

  //This is invoked when the sender thread writes to a broken pipe or if another IOException occurs
  public static void shutdown() {
    shutdown = true;
  }

  //this will append the hash of the message that it sent to the server
  public static void addSentPayload(String sentHash) {

    //Acquire a lock on sent messages
    synchronized (sentMessages) {
      sentMessages.add(sentHash);
    }
  }

  //Starts the sender thread up
  public void sendMessages() {
    new Thread(this.senderThread).start();
    new Thread(this.stats).start();
  }

  //This will read from the buffer the hashed messages sent back from the server.
  //This will remove the found message from the linkedList of the String representation of the hash
  public void readMessages() {
    String response = null;
    boolean success;

    while (true) {
      try {
        if (shutdown) {
          System.exit(0);
        }
        server.read(buffer);
        response = new String(buffer.array()).trim();
        synchronized (sentMessages) {
          success = sentMessages.remove(response);
        }
        if (success) {
          StatsCollector.receivedMsg();
        } else {
          StatsCollector.updateMismatch();
        }
        buffer.clear();

      } catch (IOException e) {
        shutdown();
      }
    }
  }

  public static void main(String[] args) {

    Client client = null;

    if (args.length != 3) {
      System.err.println("Invalid command line args. Received " + args.length + " args.");
      System.err.println(
          "Expected: java cs455.scaling.client.Client server-host server-port message-rate");
      System.exit(-1);
    }

    try {
      //Here the client registers with the server, see the ctor
      client = new Client(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));

    } catch (IOException ie) {
      System.err.println(
          "Client was unable to open connection with the Server. Recieved IOException: " + ie
              .getMessage());
      System.exit(-1);
    }

    //This will start up the sender thread and the statistics collector thread
    client.sendMessages();

    //Client will now listen back and read the response from the buffer
    client.readMessages();
  }

}
