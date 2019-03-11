package cs455.scaling.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

//
//java cs455.scaling.server.Server portnum thread-pool-size batch-size batch-time
public class Server {

  private static Selector selector;
  private static ServerSocketChannel serverSocket;
  public static ServerStatistics stats;

  private ThreadPoolManagerThread threadPoolManagerThread; //This is where the majority of the data is

  public Server(int port_number, int threadPoolSize, int batchSize, int batchTime)
      throws IOException {

    selector = Selector.open();

    serverSocket = ServerSocketChannel.open();
    serverSocket.bind(new InetSocketAddress(port_number));
    serverSocket.configureBlocking(false);
    serverSocket.register(selector,
        SelectionKey.OP_ACCEPT); //This adds the channel to the the selecctor and it knows its capable of accepting things

    this.threadPoolManagerThread = new ThreadPoolManagerThread(threadPoolSize, batchSize,
        batchTime);
    stats = new ServerStatistics();

  }

  //This method will startup the thread pool and select from the selector to grab the keys
  public void startup() {
    new Thread(new StatsThread()).start();
    new Thread(threadPoolManagerThread).start();
    try {
      while (true) {
        selector.select();
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> iter = selectedKeys.iterator();
        while (iter.hasNext()) {
          SelectionKey key = iter.next();
          threadPoolManagerThread.addPendingTask(key);
          iter.remove();
        }
      }
    } catch (IOException ie) {
      System.err.println(ie.getMessage());
    }
  }

  //This will only be called from the worker thread to register the client
  public static void register(SelectionKey key) throws IOException {
    if (key.attachment() != null) {
      SocketChannel socket = serverSocket.accept();
      socket.configureBlocking(false);
      socket.register(selector, SelectionKey.OP_READ);
      key.attach(null);
      stats.addConnection();
    }
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

    server.startup();

  }
}
