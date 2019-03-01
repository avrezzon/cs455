package cs455.scaling.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

//
//java cs455.scaling.server.Server portnum thread-pool-size batch-size batch-time
public class Server {

  private ServerSocketChannel serverSocket;
  private Selector selector;
  private int port_number;
  private int threadPoolSize;
  private int batchSize;
  private int batchTime;
  //TODO needs a structure to store all of the keys registered with the server

  public Server(int port_number, int threadPoolSize, int batchSize, int batchTime)
      throws IOException {

    this.port_number = port_number;
    this.threadPoolSize = threadPoolSize;
    this.batchSize = batchSize;
    this.batchTime = batchTime;

    this.selector = Selector.open();
    this.serverSocket = ServerSocketChannel.open();
    this.serverSocket.bind(new InetSocketAddress(port_number));
    this.serverSocket.configureBlocking(false);
    this.serverSocket.register(this.selector,
        SelectionKey.OP_ACCEPT); //TODO I need to figure out what this means

    //TODO here I need to pre initialize the threads including the thread pool with the worker threads\
  }

  public static void main(String[] args){

      Server server= null;

      if(args.length != 4){
          System.err.println("Error: Recieved " + args.length + " args. Expected 4");
          System.err.println("java cs455.scaling.server.Server portnum thread-pool-size batch-size batch-time");
          System.exit(-1);
      }

      try {
          server = new Server(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]) );
      }catch(IOException ie){
          System.err.println("Unable to create the server: " + ie.getMessage());
          System.exit(-1);
      }
  }

}
