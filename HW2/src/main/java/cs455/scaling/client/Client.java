package cs455.scaling.client;

import cs455.scaling.wireformats.Payload;
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
  private static LinkedList<Payload> sentMessages;
  private static ClientStatistics StatsCollector;
  private static ByteBuffer buffer;

  //The ctor holds the registration process for the client
  public Client(String host_name, int port_number, int messageRate) throws IOException {

    this.server = SocketChannel.open(new InetSocketAddress(host_name, port_number));
    this.msgRate = messageRate;
    this.senderThread = new SenderThread(this.server, messageRate);
    sentMessages = new LinkedList<Payload>();
    StatsCollector = new ClientStatistics();
    buffer = ByteBuffer.allocate(8000);

  }

  public void sendMessages() {
    new Thread(this.senderThread).start();
  }

  //FIXME for the correct functionality
  public void readMessages() {
    String response = null;
    try {
      buffer.clear();
      server.read(buffer);
      response = new String(buffer.array()).trim();
      System.out.println("Server responded with: " + response);
      buffer.clear();
    } catch (IOException e) {
      e.printStackTrace();
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

    //This will start up the sender thread and it will start to
    client.sendMessages();

    while (true) {
      client.readMessages();
      //TODO add all of the extra things for the stats collector
    }

  }

}
