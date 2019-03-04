package cs455.scaling.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

//When we run this class the command line args will look like
//java cs455.scaling.client.Client server-host server-port message-rate
public class Client {

  private int msgRate; //This will have a value between 2 - 4
  private SocketChannel client;
  private SenderThread sender;
  //TODO add in the sender thread
  //TODO find an efficient means to store the previous messages sent to the server
  //TODO implement the statistics collector for the client

  public Client(String host_name, int port_number, int messageRate) throws IOException {
    this.client = SocketChannel.open(new InetSocketAddress(host_name, port_number));
    this.msgRate = messageRate;
  }

  public void sendMessages() {
    //TODO add the main message calculation and use the sender thread
    //TODO this class should send payload.getBytes to the server to recieve the msg NOTE msg size always 8 KB
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
      client = new Client(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    } catch (IOException ie) {
      System.err.println(
          "Client was unable to open connection with the Server. Recieved IOException: " + ie
              .getMessage());
      System.exit(-1);
    }

    //TODO start up the sender thread
    client.sendMessages();

  }

}
