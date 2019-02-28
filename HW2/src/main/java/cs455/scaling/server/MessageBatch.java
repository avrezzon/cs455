package cs455.scaling.server;

import cs455.scaling.wireformats.Payload;
import java.util.LinkedList;

//This is stored within the client node and is what the worker thread will append the new message contents to
public class MessageBatch {

  private int batchSize;
  private volatile double maxBatchTime; //This is declared as volatile so that the time is synchronized among the threads
  private LinkedList<Payload> messages;
  //TODO this class could raise an event that would trigger the java nio if the thing has reached its time

  public MessageBatch(int batchSize, double maxBatchTime) {
    this.batchSize = batchSize;
    this.maxBatchTime = maxBatchTime;
  }

}
