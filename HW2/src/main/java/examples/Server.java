package examples;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class Server {

  public static void main(String[] args) throws IOException {
    // Open the selector
    Selector selector = Selector.open();

    // Create our input channel
    ServerSocketChannel serverSocket = ServerSocketChannel.open();
    serverSocket.bind(new InetSocketAddress("localhost", 5001));
    serverSocket.configureBlocking(false);

    // Register our channel to the selector
    serverSocket.register(selector, SelectionKey.OP_ACCEPT);

    // Loop on selector
    while (true) {
      System.out.println("Listening for new connections or new messages.");

      // Block here
      selector.select();
      System.out.println("\tActivity on selector!");

      // Key(s) are ready
      Set<SelectionKey> selectedKeys = selector.selectedKeys();

      // Loop over ready keys
      Iterator<SelectionKey> iter = selectedKeys.iterator();
      while (iter.hasNext()) {

        // Grab current key
        SelectionKey key = iter.next();

        // Optional
        if (key.isValid() == false) {
          continue;
        }

        // New connection on serverSocket
        if (key.isAcceptable()) {
          register(selector, serverSocket);
        }

        // Previous connection has data to read
        if (key.isReadable()) {
          readAndRespond(key);
        }

        // Remove it from our set
        iter.remove();
      }
    }
  }

  private static void register(Selector selector, ServerSocketChannel serverSocket)
      throws IOException {
    // Grab the incoming socket from the serverSocket
    SocketChannel client = serverSocket.accept();
    // Configure it to be a new channel and key that our selector should monitor
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_READ);
    System.out.println("\t\tNew client registered.");
  }

  private static void readAndRespond(SelectionKey key) throws IOException {
    // Create a buffer to read into
    ByteBuffer buffer = ByteBuffer.allocate(8000);

    // Grab the socket from the key
    SocketChannel client = (SocketChannel) key.channel();
    // Read from it
    int bytesRead = client.read(buffer);
    //Handle a closed connection
    if (bytesRead == -1) {
      client.close();
      System.out.println("\t\tClient disconnected.");
    } else {
      // Return their message to them
      System.out.println("\t\tReceived: " + new String(buffer.array()));
      // Flip the buffer to now write
      //buffer.flip();
      //client.write(buffer);
      // Clear the buffer
      buffer.clear();
    }

  }


}