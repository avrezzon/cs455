package cs455.scaling.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

//
//java cs455.scaling.server.Server portnum thread-pool-size batch-size batch-time
public class Server {

  private static Selector selector;
  private static ServerSocketChannel serverSocket;
  private ThreadPoolManager threadPoolManager; //This is where the majority of the data is
  private MessageRelayerThread messageRelayer;

  public Server(int port_number, int threadPoolSize, int batchSize, int batchTime)
      throws IOException {

    this.selector = Selector.open();

    this.serverSocket = ServerSocketChannel.open();
    this.serverSocket.bind(new InetSocketAddress(port_number));
    this.serverSocket.configureBlocking(false);
    this.serverSocket.register(this.selector,
        SelectionKey.OP_ACCEPT); //This adds the channel to the the selecctor and it knows its capable of accepting things

    this.threadPoolManager = new ThreadPoolManager(threadPoolSize, batchSize, batchTime);
    this.messageRelayer = new MessageRelayerThread(this.serverSocket);
  }

  //This will only be called from the worker thread FIXME
  public static void register() throws IOException {
    SocketChannel socket = serverSocket.accept();
    socket.configureBlocking(false);
    socket.register(selector, SelectionKey.OP_READ);
  }

  public static Set<SelectionKey> getKeys() throws IOException {
    selector.select();
    return selector.selectedKeys();
  }

  public static void main(String[] args) {

    Server server = null;

    if (args.length != 4) {
      System.err.println("Error: Recieved " + args.length + " args. Expected 4");
      System.err.println(
          "java cs455.scaling.server.Server portnum thread-pool-size batch-size batch-time");
      System.exit(-1);
    }

    try {
      server = new Server(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
          Integer.parseInt(args[2]), Integer.parseInt(args[3]));
    } catch (IOException ie) {
      System.err.println("Unable to create the server: " + ie.getMessage());
      System.exit(-1);
    }

  }
}
