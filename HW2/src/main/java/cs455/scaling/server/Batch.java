package cs455.scaling.server;

import java.nio.channels.SelectionKey;
import java.util.LinkedList;

//NOTE that this class should be thread safe due to the fact that a worker thread is ONLY allowed to modify the batch of
//  the lock of the client node to be able to access this class
//This is stored within the client node and is what the worker thread will append the new message contents to
public class Batch {

    public int length;
    private int maxBatchSize;
    private double maxBatchTime; //This is declared as volatile so that the time is synchronized among the threads
    private double timesUp;
  private LinkedList<SelectionKey> clientMessages;
  private BatchTimer batchTimer; //This is the thread that will be created to dispatch the thread constant r

    public Batch(int batchSize, double maxBatchTime) {
        this.maxBatchSize = batchSize;
        this.maxBatchTime = maxBatchTime;
      this.timesUp = System.currentTimeMillis() + this.maxBatchTime;
      this.clientMessages = new LinkedList<>();

    }


    //NOTE: after this function is called a new batch is created
    //TODO determine who will be accessing this guy
    public LinkedList<SelectionKey> detach() {
      LinkedList<SelectionKey> linkedMsg = (LinkedList<SelectionKey>) clientMessages.clone();
        return linkedMsg;
    }

  public void append(SelectionKey key) {
    clientMessages.add(key);
    }

}
