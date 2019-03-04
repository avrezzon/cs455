package cs455.scaling.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class MessageRelayerThread implements Runnable {

  private ServerSocketChannel serverSocket;

  public MessageRelayerThread(ServerSocketChannel serverSocket) {
    this.serverSocket = serverSocket;
  }

  //This would be a function where the thread adds the registration RQ to the task queue

  public void run() {
    while (true) {
      try {
        Iterator<SelectionKey> iter = Server.getKeys().iterator();
        while (iter.hasNext()) {
          SelectionKey key = iter.next();
          if (key.isValid() == false) {
            continue;
          }
          if (key.isAcceptable()) {
            //TODO worker thread needs to be able to register the clients
            //This task will include the key for the worker thread to have context
          }
          // Previous connection has data to read
          if (key.isReadable()) {
            //TODO this is where this thread needs to append the task to the queue for the threadPool
            //readAndRespond(key);
          }

          // Remove it from our set
          iter.remove();

        }
      } catch (IOException ie) {
        System.out.println(ie.getMessage());
      }
    }
  }
}
