package cs455.scaling.server;

import cs455.scaling.protocol.ClientMessage;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.LinkedList;

//NOTE that this class should be thread safe due to the fact that a worker thread is ONLY allowed to modify the batch of
//  the lock of the client node to be able to access this class
//This is stored within the client node and is what the worker thread will append the new message contents to
public class Batch {

  private int maxBatchSize;
  private int maxBatchTime; //This is declared as volatile so that the time is synchronized among the threads
  private long dispatchTime;
  private LinkedList<ClientMessage> clientMessages;

  public Batch(int batchSize, int maxBatchTime) {
    this.maxBatchSize = batchSize;
    this.maxBatchTime = maxBatchTime;
    this.dispatchTime = System.currentTimeMillis() / 1000 + this.maxBatchTime;
    this.clientMessages = new LinkedList<>();
  }


  //This is the method that will add the new key into the current head of the batch
  public void append(SelectionKey key) {
    synchronized (key) {
      try {
        clientMessages.add(new ClientMessage(key));
      } catch (IOException ie) {
        System.out.println("Encountered IOException: " + ie.getMessage());
      } catch (NullPointerException ne) {
        //The message has already been read
      }
    }
  }

  //This method will return an iterable of the Selection keys back to the task -->Task will have ea batch attached so I can call this
  public Iterator<ClientMessage> getBatchMessages() {
    return clientMessages.iterator();
  }

  //This method is called to determine the state of the batch whether or not is should dispatch
  public boolean readyToDispatch() {
    long now = System.currentTimeMillis() / 1000;
    if (now == dispatchTime || clientMessages.size() == maxBatchSize) {
      return true;
    }
    return false;
  }


}
