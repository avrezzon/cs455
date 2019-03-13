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
    private static LinkedList<String> sentMessages;
    public static ClientStatistics StatsCollector;
    private static ByteBuffer buffer;
    private ClientStatsThread stats;
  private static volatile boolean shutdown;

    //The ctor holds the registration process for the client
    public Client(String host_name, int port_number, int messageRate) throws IOException {

        this.server = SocketChannel.open(new InetSocketAddress(host_name, port_number));
        this.msgRate = messageRate;
        buffer = ByteBuffer.allocate(8000);
        this.senderThread = new SenderThread(this.server, messageRate);
        sentMessages = new LinkedList<>();
        StatsCollector = new ClientStatistics();
        stats = new ClientStatsThread();
      this.shutdown = false;

    }

  public static void shutdown() {
    shutdown = true;
  }

    public static void addSentPayload(String sentHash) {
        synchronized (sentMessages) {
            sentMessages.add(sentHash);
        }
    }

    public void sendMessages() {
        new Thread(this.senderThread).start();
        new Thread(this.stats).start();
        String response = null;
        while (true) try {
          if (shutdown) {
            System.exit(0);
          }
            server.read(buffer); //Blocking call
            response = new String(buffer.array()).trim();
            boolean success = sentMessages.remove(response);
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

        client.sendMessages();

    }

}
