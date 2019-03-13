package cs455.scaling.server;


import cs455.scaling.protocol.ClientMessage;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.LinkedList;

public class BatchMessages {

  private LinkedList<Batch> batchLL;
  private volatile int headNodePtr;
  private int batchSize;
  private int batchTime;

  public BatchMessages(int batchSize, int batchTime) {

    this.batchLL = new LinkedList<>();
    this.batchLL.add(new Batch(batchSize, batchTime));

    this.headNodePtr = 0;
    this.batchSize = batchSize;
    this.batchTime = batchTime;
  }

  public void append(SelectionKey key) {

    Batch currentBatch = null;
    synchronized (batchLL) {
      currentBatch = this.batchLL.get(headNodePtr);
      if (currentBatch.readyToDispatch()) {

        //create the new batch that the next current key will be able to append to
        batchLL.add(new Batch(batchSize, batchTime));

        currentBatch = this.batchLL.removeFirst();
        ;
        try {
          Iterator<ClientMessage> messages = currentBatch.getBatchMessages();
          while (messages.hasNext()) {
            ClientMessage clientMessage = messages.next();
            clientMessage.sendMsgToSender();
            messages.remove();
          }
        } catch (IOException ie) {
          System.out.println(
              "Encountered IOEXCEPTION while trying to send message back to the client: " + ie
                  .getMessage());
        }

      }

      //Add the current key now to the selected batch that
      currentBatch.startTimer();
      currentBatch.append(key);
    }
  }

  //TODO clean this up if this next push works, instead of puting the batch into the task queue we will just process the messages right now why wait cha feel
  //This method will be invoked once the task that contains the dispatch info calls the .resolve()
  private Batch getDispatchBatch() {
    Batch dispatchBatch = null;
    //All that I need to do is retrieve the batch and decrement the headNode
    dispatchBatch = this.batchLL.removeFirst();
    //headNodePtr = headNodePtr - 1;
    return dispatchBatch;
  }

}
